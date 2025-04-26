# AMQP CLIENT

This java based utility is based on Apache Camel to connect to an AMQP Messaging Queue.

The tool take in a properties file as input (Sample properties file provided below).

The Syntax to invoe the utility is:

`java -jar amqpclient.jar /path/to/amqp.properties`

## Sample Properties File

```
#Properties File for the tool to connect to the specific Queue.
hostname=hostname
port=5671

#Name of Queue we want to connect to.
queue=queuename

#Is there a requirement for Username and Password to Authenticate (true/false).
authRequired=true

#Only applicable if Auth Required.
username=Username
password=Password

#SSL Required or not (true/false).
sslRequired=true

#Query Parameters at the component/connection level separated by ampersand.
connectionParameters=

#Query parameters at the endpoint/queue level separated by ampersand
queueParameters=testConnectionOnStartup=true

#Time to Wait for Messages
timeout=60
```