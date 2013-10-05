package ch.ethz.asl.message.log;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * A better log.
 */
public class Log {

    private Logger logger;

    Log(Logger logger) {
        this.logger = logger;
    }

    public void error(String msg, Throwable throwable) {
        this.logger.log(Level.SEVERE , msg, throwable);
    }

    public void error(String msg) {
        this.logger.log(Level.SEVERE, msg);
    }

    public void warn(String msg, Throwable throwable) {
        this.logger.log(Level.WARNING, msg, throwable);
    }

    public void warn(String msg) {
        this.logger.log(Level.WARNING, msg);
    }

    public void info(String msg, Throwable throwable) {
        this.logger.log(Level.INFO, msg, throwable);
    }

    public void info(String msg) {
        this.logger.log(Level.INFO, msg);
    }


}

