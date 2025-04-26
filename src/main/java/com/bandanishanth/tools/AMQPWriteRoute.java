package com.bandanishanth.tools;

import org.apache.camel.builder.RouteBuilder;

public class AMQPWriteRoute extends RouteBuilder {

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
        System.out.println("=======================================================================================");
        System.out.println("The Camel Endpoint String used is: " + this.endpointString);
        System.out.println("=======================================================================================");

        from("timer:SendStringToQueue?period=2000&repeatCount=1")
                .setBody(simple(message))
                .to(this.endpointString)
                .log("Sent Message to Queue")
                .end();
    }
}
