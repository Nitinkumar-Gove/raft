# This is a simple hello world application using RabbitMQ 
# This is a sender.

import pika
import sys

try:
    # create a connection with server
    connection = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
    channel = connection.channel()
    
    # create a queue
    channel.queue_declare(queue='raftqueue')
    
    # write a message to the queue
    channel.basic_publish(exchange='',routing_key='raftqueue',body='Just another Hello ! :)')
    print "[x] sent a message on queue"
    
    # close the connection
    connection.close()

except pika.exceptions.ConnectionClosed,msg:
    print msg
    sys.exit(0)
