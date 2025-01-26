package com.subscription_manager.utils;

import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartBody;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class HtmlExtractor {

    public static boolean isTextOrHtml(String mime) {
        if (mime == null) return false;
        mime = mime.toLowerCase();
        return mime.startsWith("text/plain") || mime.startsWith("text/html");
    }

    public static String decodeBody(MessagePartBody body) {
        if (body == null || body.getData() == null) return "";
        byte[] decoded = Base64.getUrlDecoder().decode(body.getData());
        return new String(decoded, StandardCharsets.UTF_8);
    }

    public String findUnsubscribeLinkInWholeHtml(String html) {
        // Pattern <a ...>some text</a>
        Pattern pLink = Pattern.compile("<a\\s+([^>]+)>(.*?)</a>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher mLink = pLink.matcher(html);

        while (mLink.find()) {
            String aAttrs = mLink.group(1);
            String innerText = mLink.group(2);

            String textLower = innerText.replace("&nbsp;", " ").toLowerCase();
            String attrsLower = aAttrs.toLowerCase();

            if (textLower.contains("désabo") || textLower.contains("unsub")
                    || attrsLower.contains("désabo") || attrsLower.contains("unsub")) {

                String dsUrl = findDataSafeUrl(aAttrs);
                if (dsUrl != null) {
                    String real = UrlUtils.extractQParam(dsUrl);
                    if (real != null) {
                        return UrlUtils.urlDecode(real);
                    }
                }
                String fallback = findHrefHttp(aAttrs);
                if (fallback != null) {
                    return fallback;
                }
            }
        }
        return null;
    }

    private String findDataSafeUrl(String aAttrs) {
        Pattern p = Pattern.compile("data-safedirecturl\\s*=\\s*\"([^\"]+)\"", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(aAttrs);
        return m.find() ? m.group(1) : null;
    }

    private String findHrefHttp(String aAttrs) {
        Pattern p = Pattern.compile("href\\s*=\\s*\"(https?://[^\"]+)\"", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(aAttrs);
        return m.find() ? m.group(1) : null;
    }

    public String parsePartsForLinks(MessagePart part) {
        if (part.getParts() != null) {
            for (MessagePart sub : part.getParts()) {
                String found = parsePartsForLinks(sub);
                if (found != null) return found;
            }
        } else {
            if (isTextOrHtml(part.getMimeType())) {
                String html = decodeBody(part.getBody());
                String link = findUnsubscribeLinkInWholeHtml(html);
                if (link != null) return link;
            }
        }
        return null;
    }
}

