package ch.ethz.asl.message.shared.log;

import java.util.logging.Logger;


public class LogFactory {

    public static Log getLog(String name) {
        final Logger logger = Logger.getLogger(name);
        return new Log(logger);
    }

    public static Log getLog(Class clazz) {
        return getLog(clazz.getName());
    }

}
