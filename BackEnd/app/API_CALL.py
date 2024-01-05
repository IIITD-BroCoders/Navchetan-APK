#code for calling api
import requests
import json
import os
import sys

#function to call api POST data to it
def call_api():
    for i in range(10,20):
        response = {
            "emailid_T1": "U@gmail.com",
            "emailid_T2": "X@gmail.com",
            "state": "Delhi",
            "district": "Shahdara",
            "address": "TrilokPuri",
            "datetime": "2020-12-{}T10:00:00Z".format(i),
            "capacity": 10,
            "status": "0"
        }
        response = json.dumps(response)
        response = json.loads(response)
        response = requests.post('http://165.22.212.47/api/addUser/',data=response)
        print(response.json())
call_api()