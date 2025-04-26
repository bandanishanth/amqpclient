package com.bandanishanth.tools;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.apache.camel.component.amqp.AMQPComponent;

public class CreateAMQPComponent {
    public static AMQPComponent createComponentFromProperties(Properties amqpProperties) {
        AMQPComponent amqpComponent;

        String hostname = amqpProperties.getProperty("hostname");

        String port = amqpProperties.getProperty("port");

        String protocol = amqpProperties.getProperty("sslRequired").equals("true") ? "amqps" : "amqp";

        String connectionParameters = amqpProperties.getProperty("connectionParameters");

        String uri;

        if (connectionParameters.isEmpty()) {
            uri = protocol + "://" + hostname + ":" + port;
        } else {
            uri = protocol + "://" + hostname + ":" + port + "?" + connectionParameters;
        }

        boolean authRequired = amqpProperties.getProperty("authRequired").equals("true");

        System.out.println("The URI used to create the AMQP Camel Component is: " + uri);

        if (authRequired) {
            String username = amqpProperties.getProperty("username");
            String password = amqpProperties.getProperty("password");

            //Component with username and password
            amqpComponent = AMQPComponent.amqpComponent(uri, username, password);
        } else {
            amqpComponent = AMQPComponent.amqpComponent(uri);
        }

        return amqpComponent;
    }

    public static class PropertiesUtil {
        static Properties amqpProperties;

        public static Properties loadProperties(String path) {
            Properties amqpProperties = new Properties();

            try (InputStream fileInputStream = Files.newInputStream(Paths.get(path))) {
                amqpProperties.load(fileInputStream);

                System.out.println("Loaded Properties from the proprties file: " + path);
                System.out.println();
                System.out.println(amqpProperties);

                PropertiesUtil.amqpProperties = amqpProperties;

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            return amqpProperties;
        }

        public static boolean isNullOrEmpty(String key) {
            if (amqpProperties.getProperty(key).isEmpty() || amqpProperties.getProperty(key) == null) {
                return true;
            } else {
                return false;
            }
        }

        public static Boolean validateProperties() {

            //Check if the tool.operation is a valid value i.e Read/Write

            if (isNullOrEmpty("tool.operation")) {
                System.out.println("ERROR: Invalid operation provided, please configure the tool.operation in the properties file as Read (OR) Write.");
                return false;
            }

            // Check for empty hostname and port
            if (isNullOrEmpty("hostname")) {
                System.out.println("ERROR: Hostname not provided in the properties file.");
                return false;
            }

            if (isNullOrEmpty("port")) {
                System.out.println("ERROR: Port not provided in the properties file.");
                return false;
            }

            // SSL and Authentication Flags are compulsory in the properties file.

            if (isNullOrEmpty("authRequired")) {

                System.out.println("ERROR: authRequired property is invalid/incomplete in the properties file.");
                return false;
            }

            if (isNullOrEmpty("queue")) {
                System.out.println(
                        "ERROR: Queue Name provided is either NULL (OR) Empty. Please provide a valid Queue Name.");
                return false;
            }
            return true;
        }
    }
}
