
#CMPE 273 Group Project - Fall 2016
__Project Team - CodeMonks__

#Team Members
1. [Nitinkumar Gove](https://github.com/Nitinkumar-Gove)
2. [Sushant Vairagade](https://github.com/sjsu-sushant)
3. [Sohrab Ali](https://github.com/ali-sohrab)
4. [Saurabh Gedam](https://github.com/saurabhgedam)

#Objective
To implement raft distributed consensus algorithm along with graphical visualization of cluster node states and data replication mechanism across all the cluster nodes.

#How to
1. Start the RabbitMQ service
2. Start RedisDB
3. Build server code using below maven command<br />
   mvn clean install
4. Execute the generated jar file using below command:<br />
   java -jar raft-consensus-{version}.jar -ip {host} -p {port} <br />
   eg. java -jar raft-consensus-1.0-SNAPSHOT.jar -ip localhost -p 8080 <br />
   You can execute as many servers as you wish they will add themselves to the cluster.
5. Use Pyhton file "ClientTest.py" to perform operation as a client
6. You can run the "index.htm" to get the live status of the nodes


#Demo
[Watch Demo Here](https://www.youtube.com/watch?v=VgWI_JIyu80)

#Project Requirements
1. [Java](http://www.oracle.com/technetwork/java/javase/downloads/index-jsp-138363.html)
2. [Redis](https://redis.io/)
3. [RabbitMQ](https://www.rabbitmq.com/)
4. [Python](https://www.python.org/downloads/)

#References
1. [Introduction to Raft](https://raft.github.io/)
2. [Raft Visualization](http://thesecretlivesofdata.com/raft/)
3. [In Search of an Understandable Consensus Algorithm](https://raft.github.io/raft.pdf)
