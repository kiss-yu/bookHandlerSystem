package freemaker.util.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LogKit {
    private static Logger log =  LoggerFactory.getLogger("nix");
    private final static String getClassName(Class clazz){
        return clazz.getName() + " : ";
    }
    public final static void info(Class clazz,String msg){
        log.info(getClassName(clazz) + msg);
    }
    public final static void info(String msg){
        log.info(msg);
    }
    public final static void debug(Class clazz,String msg){
        log.debug(getClassName(clazz) + msg);
    }
    public final static void debug(String msg){
        log.debug(msg);
    }
    public final static void error(Class clazz,String msg){
        log.error(getClassName(clazz) + msg);
    }
    public final static void error(String msg){
        log.error(msg);
    }
}