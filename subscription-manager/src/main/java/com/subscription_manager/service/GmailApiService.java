package com.subscription_manager.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.*;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.UserCredentials;
import com.subscription_manager.model.Subscription;
import com.subscription_manager.utils.EmailParser;
import com.subscription_manager.utils.HtmlExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GmailApiService {

    @FunctionalInterface
    public interface ProgressListener {
        void onProgress(int scannedCount, int totalCount);
    }

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private final EmailParser emailParser;
    private final HtmlExtractor htmlExtractor;

    @Autowired
    public GmailApiService(EmailParser emailParser, HtmlExtractor htmlExtractor) {
        this.emailParser = emailParser;
        this.htmlExtractor = htmlExtractor;
    }

    /**
     * Initializes and returns a Gmail service instance.
     *
     * @param clientId     Google API client ID
     * @param clientSecret Google API client secret
     * @param accessToken  OAuth2 access token
     * @param refreshToken OAuth2 refresh token
     * @return Initialized Gmail service
     * @throws GeneralSecurityException If a security error occurs
     * @throws IOException              If an I/O error occurs
     */
    public Gmail getGmailService(
            String clientId, String clientSecret,
            String accessToken, String refreshToken
    ) throws GeneralSecurityException, IOException {
        UserCredentials credentials = UserCredentials.newBuilder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setAccessToken(new AccessToken(accessToken, null))
                .setRefreshToken(refreshToken)
                .build();

        return new Gmail.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                new HttpCredentialsAdapter(credentials)
        )
                .setApplicationName("Subscription Manager")
                .build();
    }

    /**
     * Retrieves the total number of messages in the user's mailbox.
     *
     * @param service Initialized Gmail service
     * @return Total number of messages
     * @throws IOException If an I/O error occurs
     */
    public int getMailboxTotalCount(Gmail service) throws IOException {
        Profile profile = service.users().getProfile("me").execute();
        return profile.getMessagesTotal();
    }

    /**
     * Scans the mailbox to identify unique subscriptions with unsubscribe links.
     *
     * @param service          Initialized Gmail service
     * @param totalEmails      Total number of emails in the mailbox
     * @param progressListener Callback for reporting scan progress
     * @return List of unique subscriptions
     * @throws IOException If an I/O error occurs
     */
    public List<Subscription> getUniqueSubscriptionsWithLinks(
            Gmail service,
            int totalEmails,
            ProgressListener progressListener
    ) throws IOException {

        Map<String, Subscription> subscriptionMap = new HashMap<>();
        String nextPageToken = null;
        int scannedCount = 0;
        long startTime = System.currentTimeMillis();

        do {
            ListMessagesResponse messagesResponse = service.users()
                    .messages()
                    .list("me")
                    .setMaxResults(50L)
//                    .setQ("newer_than:2y")
                    .setPageToken(nextPageToken)
                    .execute();

            List<Message> messages = messagesResponse.getMessages();
            if (messages != null) {
                for (Message msgRef : messages) {
                    scannedCount++;
                    Message fullMsg = service.users().messages()
                            .get("me", msgRef.getId())
                            .setFormat("full")
                            .execute();

                    if (emailParser.isLikelySubscription(fullMsg)) {
                        // 1) Try extracting a link from the header
                        String link = extractOneHttpLink(fullMsg);

                        // 2) If not found, scan the email body
                        if (link == null) {
                            link = htmlExtractor.parsePartsForLinks(fullMsg.getPayload());
                        }

                        if (link != null) {
                            String from = emailParser.extractHeader(fullMsg, "From");
                            if (from != null) {
                                var pf = emailParser.parseFrom(from);
                                String brandKey = pf.getBrandName().toLowerCase();

                                Subscription sub = subscriptionMap.get(brandKey);
                                if (sub == null) {
                                    sub = new Subscription(pf.getBrandName(), pf.getFromAddress());
                                    subscriptionMap.put(brandKey, sub);
                                }
                                if (sub.getUnsubscribeLink() == null) {
                                    sub.setUnsubscribeLink(link);
                                }
                                String subject = emailParser.extractHeader(fullMsg, "Subject");
                                if (subject != null) {
                                    sub.setLastSubject(subject);
                                }
                            }
                        }
                    }
                    progressListener.onProgress(scannedCount, totalEmails);
                }
            }
            nextPageToken = messagesResponse.getNextPageToken();
        } while (nextPageToken != null);

        long endTime = System.currentTimeMillis();
        System.out.println("Scan completed in " + (endTime - startTime) / 60000.0 + " min");


        return new ArrayList<>(subscriptionMap.values());
    }

    /**
     * Extracts a single HTTP(S) link from the List-Unsubscribe header.
     *
     * @param msg Email message
     * @return Unsubscribe link or null if not found
     */
    private String extractOneHttpLink(Message msg) {
        String unsubHeader = emailParser.extractHeader(msg, "List-Unsubscribe");
        if (unsubHeader == null) return null;

        Matcher m = Pattern.compile("<([^>]+)>").matcher(unsubHeader);
        while (m.find()) {
            String candidate = m.group(1).trim();
            if (candidate.startsWith("http://") || candidate.startsWith("https://")) {
                return candidate;
            }
        }
        return null;
    }
}
