#! /usr/bin/env python
"""
A skeleton python script which reads from an input file,
writes to an output file and parses command line arguments
"""
from __future__ import print_function
import sys
import argparse
import requests

# helper function for sending http requests to backend
def http_request(context = '', p = {}, type = 'get'):
    if (type == 'get'):
        return requests.get(BACKEND_ENDPOINT + context, params=p)
    elif (type == 'post'):
        return requests.post(BACKEND_ENDPOINT + context, params=p)

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

if __name__ == "__main__":

    # parse global args
    global_args()

    # create game
    game_data = http_request('', {'name': "Zach", 'charName': "mr. green"}, 'post').json()
    game_id = game_data['gameId']

    # add players
    game_data = http_request('/' + str(game_id) + '/players', {'name': "Megan", 'charName': "mrs. peacock"}, 'post').json()
    game_data = http_request('/' + str(game_id) + '/players', {'name': "Alex", 'charName': "mr. white"}, 'post').json()

    # start game