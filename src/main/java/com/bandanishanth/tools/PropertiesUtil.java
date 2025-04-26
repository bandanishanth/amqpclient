package com.bandanishanth.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class PropertiesUtil {
    static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
    static Properties properties = new Properties();

    static boolean isPropertyNullOrEmpty(String key)
    {
        //If property is present/valid we should return false so we negate the expression result.
        return !(properties != null && properties.getProperty(key) != null && !properties.getProperty(key).isEmpty());
    }

    static Properties loadProperties(String path) throws IOException {
        properties.load(Files.newInputStream(Paths.get(path)));
        return properties;
    }
    static boolean validateProperties()
    {
        logger.info("Performing basic validations on the properties file for mandatory properties.");
        List<String> mandatoryProperties = Arrays.asList("tool.operation","hostname","port","queue","authRequired","sslRequired");
        boolean isValid = true;
        for (String key : mandatoryProperties)
        {
            if(isPropertyNullOrEmpty(key))
            {
                logger.error("The property {} is either NULL or EMPTY in the properties file.", key);
                isValid=false;
            }
        }
        //Check the Message in case the operation is a WRITE.
        if (!isPropertyNullOrEmpty("tool.operation") && properties.getProperty("tool.operation").equals("write") && isPropertyNullOrEmpty("write.message")) {
            logger.error("The Message to be written to the Queue is Empty, please provide a message to write in the 'write.message' property within the properties file.");
            isValid = false;
        }
        return isValid;
    }
}