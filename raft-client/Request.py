# This class models the request to be sent to the raft broker
# @author Nitinkumar Gove
# @version 2.0

from Person import Person
import json

class Request:
    action = "GET"
    person = Person(2,"Nitin",26)

    def __init__(self, action, person):
        self.action = action 
        self.person = person

    def printRequest(self):
        print "action:", self.action
        self.person.showPersonRecord()
    
    def toJSON(self):
        return json.dumps(self, default=lambda o: o.__dict__, 
            sort_keys=True, indent=4)





    
    