package com.bandanishanth.tools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class PropertiesUtil {
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
        System.out.println("Performing basic validations on the properties file for mandatory properties.");
        List<String> mandatoryProperties = Arrays.asList("tool.operation","hostname","port","queue","authRequired","sslRequired");
        boolean isValid = true;
        for (String key : mandatoryProperties)
        {
            if(isPropertyNullOrEmpty(key))
            {
                System.out.println("The property " + key +" is either NULL or EMPTY.");
                isValid=false;
            }
        }
        return isValid;
    }
}