# This class models person record
# @author Nitinkumar Gove
# @version 3.0

import json
class Person:
    def __init__(self, id, name, age):
        self.id = id
        self.name = name
        self.age = age
    
    def showPersonRecord(self):
        print "\nid:",self.id, ",\nname:",self.name,",\nage:",self.age
    
    def toJSON(self):
        return json.dumps(self, default=lambda o: o.__dict__, 
            sort_keys=True, indent=4)
    
    def setName(self,name):
        self.name = name
    
    def setAge(self,age):
        self.age = age
