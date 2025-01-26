package com.subscription_manager.utils;

import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartHeader;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class EmailParser {

    @Getter
    public static class ParsedFrom {
        private final String brandName;
        private final String fromAddress;

        public ParsedFrom(String brandName, String fromAddress) {
            this.brandName = brandName;
            this.fromAddress = fromAddress;
        }

    }

    public ParsedFrom parseFrom(String fromRaw) {
        Matcher m = Pattern.compile("^(.*?)<(.*?)>$").matcher(fromRaw);
        String displayName;
        String address;
        if (m.find()) {
            displayName = m.group(1).replaceAll("\"", "").trim();
            address = m.group(2).trim();
        } else {
            displayName = fromRaw;
            address = "";
        }

        // Cut after " - " in the brandName
        int dashPos = displayName.indexOf(" - ");
        if (dashPos != -1) {
            displayName = displayName.substring(0, dashPos).trim();
        }
        return new ParsedFrom(displayName, address);
    }

    public String extractHeader(Message msg, String headerName) {
        if (msg.getPayload() != null && msg.getPayload().getHeaders() != null) {
            for (MessagePartHeader h : msg.getPayload().getHeaders()) {
                if (headerName.equalsIgnoreCase(h.getName())) {
                    return h.getValue();
                }
            }
        }
        return null;
    }

    public boolean isLikelySubscription(Message msg) {
        // a) Header List-Unsubscribe ?
        if (extractHeader(msg, "List-Unsubscribe") != null) return true;

        // b) from
        String from = extractHeader(msg, "From");
        if (from != null) {
            String lower = from.toLowerCase();
            if (lower.contains("newsletter") || lower.contains("no-reply") || lower.contains("subscription")) {
                return true;
            }
        }

        // c) subject
        String subject = extractHeader(msg, "Subject");
        if (subject != null) {
            String ls = subject.toLowerCase();
            if (ls.contains("unsubscribe") || ls.contains("désabonn") || ls.contains("désinscri")) {
                return true;
            }
        }

        // d) fallback: parse body (search for unsubscribe in the body)
        return parseBodyForUnsubscribe(msg);
    }

    private boolean parseBodyForUnsubscribe(Message msg) {
        if (msg.getPayload() == null) return false;
        return searchPartsForUnsubKeyword(msg.getPayload());
    }

    private boolean searchPartsForUnsubKeyword(MessagePart part) {
        if (part.getParts() != null) {
            for (MessagePart sub : part.getParts()) {
                if (searchPartsForUnsubKeyword(sub)) return true;
            }
        } else {
            if (HtmlExtractor.isTextOrHtml(part.getMimeType())) {
                String txt = HtmlExtractor.decodeBody(part.getBody()).toLowerCase();
                if (txt.contains("unsubscribe") || txt.contains("désabo") || txt.contains("désinscri")) {
                    return true;
                }
            }
        }
        return false;
    }
}
