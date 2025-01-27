package com.subscription_manager.controller;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.gmail.Gmail;
import com.subscription_manager.configuration.GoogleConfig;
import com.subscription_manager.model.Subscription;
import com.subscription_manager.service.GmailApiService;
import com.subscription_manager.service.GmailTokenService;
import com.subscription_manager.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;

/**
 * GmailController handles HTTP requests related to Gmail operations.
 */
@RestController
@RequestMapping("/api")
public class GmailController {

    private static final Logger logger = LoggerFactory.getLogger(GmailController.class);

    private final GmailApiService gmailApiService;
    private final GmailTokenService tokenService;
    private final CacheManager cacheManager;

    // Prevent concurrent scans globally using a simple object lock
    // (To prevent two user to use the gmail API)
    private final Object scanLock = new Object();
    private final GoogleConfig googleConfig;

    @Autowired
    public GmailController(
            GmailApiService gmailApiService,
            GmailTokenService tokenService,
            GoogleConfig googleConfig,
            CacheManager cacheManager
    ) {
        this.gmailApiService = gmailApiService;
        this.tokenService = tokenService;
        this.googleConfig = googleConfig;
        this.cacheManager = cacheManager;
    }

    /**
     * SSE endpoint to fetch subscriptions.
     * - If the user is already in the cache and it is still valid, we skip the full scan.
     * - Otherwise, we scan Gmail for possible subscription emails.
     *
     * @param authentication OAuth2 authentication token
     * @return SseEmitter streaming the subscription data and progress
     */
    // TODO
    // LOCAL: http://localhost:5173
    // PRODUCTION: subscription-manager-ten.vercel.app
    @GetMapping(value = "/emails", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @CrossOrigin(origins = "subscription-manager-ten.vercel.app", allowCredentials = "true")
    public SseEmitter listEmails(OAuth2AuthenticationToken authentication) {

        logger.info("[SERVER] /api/emails SSE invoked.");
        SseEmitter emitter = new SseEmitter(0L);

        // Using authentication.getName() as the user key
        final String userKey = authentication.getName();

        new Thread(() -> {
            synchronized (scanLock) {
                try {
                    emitter.send(Map.of("info", "SSE started"));

                    // 1) Check the cache
                    List<Map<String, Object>> cachedEmails = cacheManager.getCachedSubscriptions(userKey);
                    if (cachedEmails != null) {
                        // The cache is valid => directly return it
                        logger.info("[SERVER] Using cached subscriptions for user {}", userKey);
                        emitter.send(Map.of("emails", cachedEmails));
                        emitter.complete();
                        return;
                    }

                    // 2) Otherwise, connect to Gmail
                    String clientId     = googleConfig.getClientId();
                    String clientSecret = googleConfig.getClientSecret();
                    String accessToken  = tokenService.getAccessToken(authentication);
                    String refreshToken = tokenService.getRefreshToken(authentication);

                    Gmail gmail = gmailApiService.getGmailService(
                            clientId, clientSecret, accessToken, refreshToken
                    );

                    // 3) Count total emails )
                    int totalCount = gmailApiService.getMailboxTotalCount(gmail);
                    logger.info("[SERVER] Total mailbox = {}", totalCount);

                    // 4) Scan the mailbox for subscriptions
                    List<Subscription> subs = gmailApiService.getUniqueSubscriptionsWithLinks(
                            gmail,
                            totalCount,
                            (scanned, total) -> {
                                try {
                                    emitter.send(Map.of("scanned", scanned, "total", total));
                                } catch (IOException e) {
                                    logger.error("[SERVER] Error sending progress update", e);
                                    emitter.completeWithError(e);
                                }
                            }
                    );

                    // 5) Build a JSON list (we do NOT store email content here)
                    List<Map<String, Object>> emailList = getMaps(subs);

                    // 6) Store in cache
                    cacheManager.cacheSubscriptions(userKey, emailList);
                    logger.info("[SERVER] Updated cache for user {} => {} items.", userKey, emailList.size());

                    // 7) Send via SSE
                    emitter.send(Map.of("emails", emailList));
                    logger.info("[SERVER] SSE complete => {} items found for user {}.", emailList.size(), userKey);
                    emitter.complete();

                } catch (GoogleJsonResponseException e) {
                    handleGoogleJsonResponseException(e, authentication, emitter);
                } catch (Exception e) {
                    logger.error("[SERVER] Error processing request", e);
                    try {
                        emitter.send(Map.of("error", "An error occurred while processing your request."));
                    } catch (IOException ex) {
                        logger.error("Error sending error message via SSE", ex);
                    }
                    emitter.completeWithError(e);
                }
            }
        }).start();

        return emitter;
    }

    private void handleGoogleJsonResponseException(GoogleJsonResponseException e, OAuth2AuthenticationToken authentication, SseEmitter emitter) {
        if (e.getStatusCode() == 429) {
            logger.error("Gmail API rate limit exceeded for user {}: {}", authentication.getName(), e.getMessage());
            try {
                emitter.send(Map.of("error", "Gmail API rate limit exceeded. Please try again later."));
            } catch (IOException ex) {
                logger.error("Error sending rate limit error message via SSE", ex);
            }
            emitter.completeWithError(e);
        } else {
            logger.error("[SERVER] Error processing request", e);
            try {
                emitter.send(Map.of("error", "An error occurred while processing your request."));
            } catch (IOException ex) {
                logger.error("Error sending generic error message via SSE", ex);
            }
            emitter.completeWithError(e);
        }
    }

    private static List<Map<String, Object>> getMaps(List<Subscription> subs) {
        List<Map<String, Object>> emailList = new ArrayList<>();
        for (Subscription s : subs) {
            // If no unsubscribe link => skip
            if (s.getUnsubscribeLink() == null) continue;

            var mapItem = new HashMap<String, Object>();
            mapItem.put("brandName",       s.getBrandName());
            mapItem.put("fromAddress",     s.getFromAddress());
            mapItem.put("unsubscribeLink", s.getUnsubscribeLink());
            mapItem.put("subject",         s.getLastSubject());
            emailList.add(mapItem);
        }
        return emailList;
    }
}
