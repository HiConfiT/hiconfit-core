/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2023
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.common;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
@Slf4j
public class LoggerUtils {
    @Setter
    public boolean useThreadInfo = true;

    @Getter
    public ConcurrentHashMap<Long, String> tabs = new ConcurrentHashMap<>();

    public String tab() {
        long threadId = Thread.currentThread().threadId();
        String threadInfo = useThreadInfo ? "|thread=" + threadId + "|" : "";
        return threadInfo + tabs.computeIfAbsent(threadId, k -> "");
    }

    public void indent() {
        long threadId = Thread.currentThread().threadId();
        String tab = tabs.computeIfAbsent(threadId, k -> "");
        tab += "   ";
        tabs.replace(threadId, tab);
    }

    public void outdent() {
        long threadId = Thread.currentThread().threadId();
        String tab = tabs.computeIfAbsent(threadId, k -> "");
        if (tab.length() > 0) {
            tab = tab.substring(0, tab.length() - 3);
            tabs.replace(threadId, tab);
        }
    }

    public void reset() {
        tabs.clear();
    }

    public synchronized void logMethodInfoWithSession(@NonNull String nameMethod, @NonNull String sessionId, int timeout,
                                                      @NonNull String requestUri, @NonNull String level) {
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
