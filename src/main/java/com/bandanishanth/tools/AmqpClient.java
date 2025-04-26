package com.bandanishanth.tools;

import org.apache.camel.CamelContext;
import org.apache.camel.component.amqp.AMQPComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class AmqpClient {
    static Logger logger = LoggerFactory.getLogger(AmqpClient.class);
    public static final String AMQP_CLIENT_MESSAGE_BANNER = "===============================CAMEL AMQP CLIENT===============================";

    //public static final String OUTPUT_SECTION_DELIMITER_STRING = "==============================================================";
    public static void main(String[] args) throws Exception {

        if (args.length > 0 && !args[0].isEmpty()) {

            Properties amqpProperties = PropertiesUtil.loadProperties(args[0]);

            logger.info(AMQP_CLIENT_MESSAGE_BANNER);

            if (PropertiesUtil.validateProperties()) {

                String operation = amqpProperties.getProperty("tool.operation");

                CamelContext camelContext = new DefaultCamelContext();

                AMQPComponent amqp = CreateAMQPComponent.createComponentFromProperties(amqpProperties);

                camelContext.addComponent("amqp", amqp);

                String queueName = amqpProperties.getProperty("queue");

                String queueParameters = amqpProperties.getProperty("queueParameters");

                int timeout = Integer.parseInt(amqpProperties.getProperty("timeout"));

                if (operation.equalsIgnoreCase("read")) {

                    logger.info("This utility will only connect and listen for messages for a maximum of {} seconds.", timeout);

                    camelContext.addRoutes(new AMQPReadRoute(queueName, queueParameters));

                } else if (operation.equalsIgnoreCase("write")) {
                    String message = amqpProperties.getProperty("write.message");
                    camelContext.addRoutes(new AMQPWriteRoute(queueName, queueParameters, message));
                }

                //Starting the Camel Context
                camelContext.start();

                Thread.sleep(timeout * 1000L);

                logger.info("Closing connection and stopping the utility as timeout was reached.");

                //Stop and close the Camel Context
                camelContext.stop();
                camelContext.close();

            } else {
                logger.info("The properties file provided seems to have some missing/invalid values.");
                logger.info("Please rerun the utility after providing a valid properties file.");
            }

            logger.info("==================CAMEL AMQP CLIENT==================");
        } else {
            System.out.println("Usage is: java -jar <JAR_NAME> <PATH>/amqp.properties");
        }
    }
}