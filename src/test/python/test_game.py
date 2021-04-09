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

    # initialize all contexts
    context_game = "/{}".format(game_id)
    context_players = context_game + "/players"
    context_update_location = context_game + "/players/location"
    context_make_accusation = context_game + "/players/accusation/accuse"
    context_make_suggestion = context_game + "/players/suggestion/suggest"
    context_cancel_accusation = context_game + "/players/accusation/cancel"
    context_cancel_suggestion = context_game + "/players/suggestion/cancel"
    context_reveal_suggestion = context_game + "/players/suggestion/reveal"
    context_reveal_suggestion = context_game + "/players/suggestion/accept"
    context_reveal_suggestion = context_game + "/players/complete-turn"

    # add players
    http_request(context_players, {'name': "Megan", 'charName': "mrs. peacock"}, 'post').json()
    http_request(context_players, {'name': "Alex", 'charName': "professor plum"}, 'post').json()

    # start game
    game_data = http_request(context_game, {'startGame': True, 'charName': "mr. green"}, 'post').json()

    # make first moves for all players
    # game_data = http_request(context_update_location, {'locName': , 'charName': "mr. green"}, 'post').json()

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



