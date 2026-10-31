# Example Python Code to Insert a Document 
from pymongo import MongoClient 
from bson.objectid import ObjectId 

class AnimalShelter(object): 
    """ CRUD operations for Animal collection in MongoDB """ 

    def __init__(self, username, password): 
        # Initializing the MongoClient. This helps to access the MongoDB 
        # databases and collections. This is hard-wired to use the aac 
        # database, the animals collection, and the aac user. 
        # 
        # You must edit the password below for your environment. 
        # 
        # Connection Variables 
        # 
        USER = "aacuser"
        PASS = "SNHU550963"
        HOST = 'localhost' 
        PORT = 27017 
        DB = 'aac' 
        COL = 'animals' 
        # 
        # Initialize Connection 
        # 
        self.client = MongoClient('mongodb://%s:%s@%s:%d' % (USER,PASS,HOST,PORT)) 
        self.database = self.client['%s' % (DB)] 
        self.collection = self.database['%s' % (COL)] 

    # Creates a method to return the next available record number for use in the create method
    def create(self, data):
        if data is not None:
            # Tries to insert data as a dictionary IF insertAnimal is acknowledged by the system
            try:
                insertAnimal = self.database.animals.insert_one(data)  # data should be dictionary
                if insertAnimal.acknowledged:
                    print(f"Animal was added with ID: {insertAnimal.inserted_id}")
                    return True
                else: 
                    print("Insert was not completed")
                    return False
            # standard error message if the Try fails
            except Exception as e:
                print(f"An error occured during insert: {e}")
                return False
        else:
            raise Exception("Nothing to save, because data parameter is empty") 

    # Creates a method to implement the R in CRUD.
    def read(self, data):
        # Tries to create a cursor if data is not empty/none
        try:
            if data is not None:
                cursor = self.database.animals.find(data)
            else:
                cursor = self.database.animals.find({})
            return list(cursor)
        # standard error message if the Try fails
        except Exception as e:
            print(f"Error occured while reading data: {e}")
            # returns empty list
            return []
    # Creates a method to implement the U in CRUD
    def update(self, searchData, updateData):
        # IF searchData is not none/empty AND update_data isnt as well
        if searchData is not None and update_data is not None:
            try:
                result = self.database.animals.update_many(searchData, {"$set": updateData})
                return result.raw_result
            except Exception as e:
                print(f"Database update has failed: {e}")
                return 0
        else:
            raise ValueError("Search data OR update data was wrong or empty")
    # Creates a method to implement the D in CRUD
    def delete(self, deleteData):
        # calls delete_many to delete data specified by data and returns amount of deletions
        if deleteData is not None:
            result = self.database.animals.delete_many(deleteData)
            return result.deleted_count
        else:
            return "{}"