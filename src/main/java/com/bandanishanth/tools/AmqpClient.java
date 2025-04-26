package com.bandanishanth.tools;

import java.util.Properties;

import org.apache.camel.CamelContext;
import org.apache.camel.component.amqp.AMQPComponent;
import org.apache.camel.impl.DefaultCamelContext;

public class AmqpClient {
    public static void main(String[] args) throws Exception {

        if (args.length > 0 && !args[0].isEmpty()) {

            Properties amqpProperties = PropertiesUtil.loadProperties(args[0]);

            System.out.println("===============================CAMEL AMQP CLIENT===============================");

            if (PropertiesUtil.validateProperties()) {

                String operation = amqpProperties.getProperty("tool.operation");

                CamelContext camelContext = new DefaultCamelContext();

                AMQPComponent amqp = CreateAMQPComponent.createComponentFromProperties(amqpProperties);

                camelContext.addComponent("amqp", amqp);

                String queueName = amqpProperties.getProperty("queue");

                String queueParameters = amqpProperties.getProperty("queueParameters");

                int timeout = Integer.parseInt(amqpProperties.getProperty("timeout"));

                if (operation.equalsIgnoreCase("read")) {

                    System.out.println("This utility will only connect and listen for messages for a maximum of " + timeout
                            + " seconds.\n");

                    camelContext.addRoutes(new AMQPReadRoute(queueName, queueParameters));

                } else if (operation.equalsIgnoreCase("write")) {
                    String message = amqpProperties.getProperty("write.message");
                    camelContext.addRoutes(new AMQPWriteRoute(queueName, queueParameters, message));
                }

                //Starting the Camel Context
                camelContext.start();

                Thread.sleep(timeout * 1000L);

                System.out.println("\n==============================================================\n");
                System.out.println("Closing connection and stopping the utility as timeout was reached.");
                System.out.println("\n==============================================================\n");

                //Stop and close the Camel Context
                camelContext.stop();
                camelContext.close();

            } else {
                System.out.println("The properties file provided seems to have some missing/invalid values.");
                System.out.println("Please rerun the utility after providing a valid properties file.");
            }

            System.out.println("==================CAMEL AMQP CLIENT==================");
        } else {
            System.out.println("Usage is: java -jar <JAR_NAME> <PATH>/amqp.properties");
        }
    }
}