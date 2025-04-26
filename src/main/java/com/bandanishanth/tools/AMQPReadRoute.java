package com.bandanishanth.tools;

import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AMQPReadRoute extends RouteBuilder {
    static Logger logger = LoggerFactory.getLogger(AMQPReadRoute.class);

    String queueName;
    String queueParameters;
    String endpointString;

    public AMQPReadRoute(String queueName, String queueParameters) {
        this.queueName = queueName;
        this.queueParameters = queueParameters;

        if (queueParameters.isEmpty()) {
            this.endpointString = "amqp:queue:" + queueName;
        } else {
            this.endpointString = "amqp:queue:" + queueName + "?" + queueParameters;
        }
    }

    public void configure() {
        logger.info("The Camel Endpoint String used is: {}", this.endpointString);
        from(this.endpointString)
                .log("Message Received: ${body}")
                .end();
    }
}