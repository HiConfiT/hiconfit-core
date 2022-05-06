/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class LoggerUtils {
    @Getter
    public String tab = "";

    public void indent() {
        tab += "   ";
    }

    public void outdent() {
        if (tab.length() > 0) {
            tab = tab.substring(0, tab.length() - 3);
        }
    }

    public void logMethodInfoWithSession(@NonNull String nameMethod, @NonNull String sessionId, int timeout, @NonNull String requestUri, @NonNull String level) {
        String logMessage = "{}[method={}, sessionId={}, timeout={}, request={}]";
        switch (level) {
            case "DEBUG" -> log.debug(logMessage, tab, nameMethod, sessionId, timeout, requestUri);
            case "INFO" -> log.info(logMessage, tab, nameMethod, sessionId, timeout, requestUri);
            case "WARN" -> log.warn(logMessage, tab, nameMethod, sessionId, timeout, requestUri);
            case "ERROR" -> log.error(logMessage, tab, nameMethod, sessionId, timeout, requestUri);
            default -> log.trace(logMessage, tab, nameMethod, sessionId, timeout, requestUri);
        }

    }
}
