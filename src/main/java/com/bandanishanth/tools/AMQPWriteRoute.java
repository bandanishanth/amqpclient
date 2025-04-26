package com.bandanishanth.tools;

import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AMQPWriteRoute extends RouteBuilder {

    static Logger logger = LoggerFactory.getLogger(AMQPWriteRoute.class);

    String queueName;
    String queueParameters;
    String endpointString;
    String message;

    public AMQPWriteRoute(String queueName, String queueParameters, String message) {
        this.queueName = queueName;
        this.queueParameters = queueParameters;
        this.message = message;

        if (queueParameters.isEmpty()) {
            this.endpointString = "amqp:queue:" + queueName;
        } else {
            this.endpointString = "amqp:queue:" + queueName + "?" + queueParameters;
        }
    }

    public void configure() {
        logger.info("The Camel Endpoint String used is: {}", this.endpointString);

        from("timer:SendStringToQueue?period=2000&repeatCount=1")
                .setBody(simple(message))
                .to(this.endpointString)
                .log("Sent Message to Queue")
                .end();
    }
}
