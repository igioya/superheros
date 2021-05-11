package com.superheroes.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {

    private static Logger logger;

    static {
        try {
            logger = LogManager.getLogger(Class.forName(getClassName()).getClass());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void info(String msgToLog) {
        logger.info(msgToLog);
    }

    public static void debug(String msgToLog) {
        logger.debug(msgToLog);
    }

    public static void exception(Exception exception) {
        logger.error(exception.getMessage(), exception);
    }

    public static void exception(Throwable throwable) {
        logger.error(throwable.getMessage(), throwable);
    }

    public static void warning(String msgToLog) {
        logger.warn(msgToLog);
    }

    private static String getClassName() {
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        for (int i=1; i<stElements.length; i++) {
            StackTraceElement ste = stElements[i];
            if (!Log.class.getName().equals(ste.getClassName()) && ste.getClassName().indexOf("java.lang.Thread") != 0) {
                return ste.getClassName();
            }
        }
        return Log.class.getName();
    }

}
