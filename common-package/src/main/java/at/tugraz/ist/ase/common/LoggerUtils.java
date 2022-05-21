/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.common;import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
@Slf4j
public class LoggerUtils {
    @Getter
    public ConcurrentHashMap<Long, String> tabs = new ConcurrentHashMap<>();

    public String tab() {
        long threadId = Thread.currentThread().getId();
        return "|thread=" + threadId + "|" + tabs.computeIfAbsent(threadId, k -> "");
    }

    public void indent() {
        long threadId = Thread.currentThread().getId();
        String tab = tabs.computeIfAbsent(threadId, k -> "");
        tab += "   ";
        tabs.replace(threadId, tab);
    }

    public void outdent() {
        long threadId = Thread.currentThread().getId();
        String tab = tabs.computeIfAbsent(threadId, k -> "");
        if (tab.length() > 0) {
            tab = tab.substring(0, tab.length() - 3);
            tabs.replace(threadId, tab);
        }
    }

    public synchronized void logMethodInfoWithSession(@NonNull String nameMethod, @NonNull String sessionId, int timeout, @NonNull String requestUri, @NonNull String level) {
        String logMessage = "{}[method={}, sessionId={}, timeout={}, request={}]";
        switch (level) {
            case "DEBUG" -> log.debug(logMessage, tab(), nameMethod, sessionId, timeout, requestUri);
            case "INFO" -> log.info(logMessage, tab(), nameMethod, sessionId, timeout, requestUri);
            case "WARN" -> log.warn(logMessage, tab(), nameMethod, sessionId, timeout, requestUri);
            case "ERROR" -> log.error(logMessage, tab(), nameMethod, sessionId, timeout, requestUri);
            default -> log.trace(logMessage, tab(), nameMethod, sessionId, timeout, requestUri);
        }
    }
}
