# This is a simple receiver applicationi which reads messages from the RabbitMQ Queue
import pika
import sys

try:
    # create a connection with server
    connection = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
    channel = connection.channel()
    # create a queue
    channel.queue_declare(queue='raftqueue')

    # defining a callback to be excecuted whenever a msg is receieved
    def callback(ch, method,properties,body):
        print '[x] received %r ' % body 

    # register the callback with queue
    channel.basic_consume(callback,
                      queue='raftqueue',
                      no_ack=True)

    print(' [*] Waiting for messages. To exit press CTRL+C')
    # start consuming the queue
    channel.start_consuming()

except pika.exceptions.ConnectionClosed, msg:
    print msg
    sys.exit(0)

