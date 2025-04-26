package com.bandanishanth.tools;

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
}
