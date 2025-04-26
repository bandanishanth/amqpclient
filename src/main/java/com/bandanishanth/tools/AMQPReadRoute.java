package com.bandanishanth.tools;

import org.apache.camel.builder.RouteBuilder;

public class AMQPReadRoute extends RouteBuilder {

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
        System.out.println("=======================================================================================");
        System.out.println("The Camel Endpoint String used is: " + this.endpointString);
        System.out.println("=======================================================================================");

        from(this.endpointString)
                .log("Message Received: ${body}")
                .end();
    }
}
