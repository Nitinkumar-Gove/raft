# Raft-Implementation
This repository stores the implementation of Raft algorithm using python, java, rabbitMQ and redis. 

###RabbitMQ 
RabbitMQ is a message broker. The principal idea is pretty simple: it accepts and forwards messages.

_**How about a metaphor?**_

You can think about RabbitMQ as a post office: when you send mail to the post box you're pretty sure that Mr. Postman will eventually deliver the mail to your recipient. Using this metaphor RabbitMQ is a post box, a post office and a postman.

_**Why use it in this project?**_

To store the request messages from different clients. And to store all the reply messages from leader. 
