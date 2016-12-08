# This class models the test client in raft implementation
# @author Nitinkumar Gove
# @version 3.0

from rpcclient import RaftRpcClient
from Person import Person
from Request import Request
import json
import pika
import sys

def make_person(dct):
    return Person(dct['id'],dct['name'],dct['age'])

#TEST_POST
def getPOSTRequest(id, name, age):
    p = Person(id,name,age)
    request = Request("POST",p)
    req = request.toJSON()
    return req

#TEST_UPDATE
def getPUTRequest(id,name,age):
    p = Person(id,name,age)
    request = Request("PUT",p)
    req = request.toJSON()
    return req

#TEST_DELETE
def getDELETERequest(id,name,age):
    p = Person(id,name,age)
    request = Request("\DELETE",p)
    req = request.toJSON()
    return req

try:
    raft_rpc = RaftRpcClient()
    print("[x] Sending Request\n")
    # response = raft_rpc.call(getPOSTRequest(-1,"Amit",21))       # TEST 01
    # response = raft_rpc.call(getPOSTRequest(-1,"Bob",23))      # TEST 02
    response = raft_rpc.call(getPOSTRequest(-1,"Cyril",22))    # TEST 03
    # response = raft_rpc.call(getPOSTRequest(-1,"Brian",24))    # TEST 04
    # response = raft_rpc.call(getPOSTRequest(-1,"Charlie",27))  # TEST 05
    # response = raft_rpc.call(getPOSTRequest(-1,"Brad",26))     # TEST 06
    # response = raft_rpc.call(getPOSTRequest(-1,"Chiboo",32))   # TEST 07    
    # response = raft_rpc.call(getPOSTRequest(-1,"Binee",28))    # TEST 08
    # response = raft_rpc.call(getPOSTRequest(-1,"Charles",22))  # TEST 09
    print "[.] Received Response >> \n" ,response
    person = json.loads(response, object_hook=make_person)
    person.showPersonRecord();
   
except pika.exceptions.ConnectionClosed,msg:
    print msg
    sys.exit();