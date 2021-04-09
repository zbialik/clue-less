#! /usr/bin/env python
"""
A skeleton python script which reads from an input file,
writes to an output file and parses command line arguments
"""
from __future__ import print_function
import sys
import argparse
import requests
import json

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
    game_id = http_request('', {'name': "Zach", 'charName': "mr. green"}, 'post').json()['gameId']

    # add players
    http_request('/' + str(game_id) + '/players', {'name': "Megan", 'charName': "mrs. peacock"}, 'post').json()
    http_request('/' + str(game_id) + '/players', {'name': "Alex", 'charName': "professor plum"}, 'post').json()

    # start game
    game_data = http_request('/' + str(game_id), {'startGame': True, 'charName': "mr. green"}, 'post').json()
    print(json.dumps(game_data, indent=4))

    # make first moves for all players

    # make move for first player
    # make suggestion for first player
    # have another player reveal clue
    # complete turn for first player

    # make move for second player
    # make suggestion for second player (suggest cards that match mysterCards to test use-case)
    # complete_turn for second player

    # make wrong accusation for third player

    # make move for first player
    # make correct accusation for first player

    

