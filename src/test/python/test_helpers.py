
import re
import sys
import json
import argparse
import requests

# helper function for parsing args
def global_args():
    global BACKEND_ENDPOINT

    parser = argparse.ArgumentParser(description=__doc__)

    parser.add_argument('-b','--backend', 
        dest='backend_endpoint',
        default='http://localhost:8080/games',
        help="endpoint for backend server")
    
    args = parser.parse_args()
    
    # set global constants
    BACKEND_ENDPOINT = args.backend_endpoint

# helper for controlling test game demo logic
def trigger_http(context = '', p = {}, type = 'get'):

    # grab gid from context
    gid = str(list(map(int, re.findall(r'\d+', context)))[0])

    # TODO: wait for user input
    input("\nClick ENTER to make HTTP request: ")

    # make http call
    http_request(context, p, type)

    # get game data
    game_data = http_request('/' + gid)

    # print game data
    print(json.dumps(game_data.json(), indent=4))

    # return game data
    return game_data

# helper function for sending http requests to backend
def http_request(context = '', p = {}, type = 'get'):
    if (type == 'get'):
        return requests.get(BACKEND_ENDPOINT + context, params=p)
    elif (type == 'post'):
        return requests.post(BACKEND_ENDPOINT + context, params=p)