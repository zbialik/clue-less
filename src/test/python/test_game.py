#! /usr/bin/env python
"""
A skeleton python script which reads from an input file,
writes to an output file and parses command line arguments
"""
from __future__ import print_function
from clue_constants import *
from test_helpers import *
import json

if __name__ == "__main__":

    # parse global args
    global_args()

    # create game
    game_id = http_request('', {'playerName': "Zach", 'charName': CHARACTER_NAME_COLONEL_MUSTARD}, 'post').json()['gameId']

    # initialize game context
    context_game = "/{}".format(game_id)

    # add players
    http_request(context_game + '/players', {'playerName': "Megan", 'charName': CHARACTER_NAME_MRS_WHITE}, 'post')
    http_request(context_game + '/players', {'playerName': "Alex", 'charName': CHARACTER_NAME_MR_GREEN}, 'post')

    # start game
    http_request(context_game, {'activate': True, 'charName': CHARACTER_NAME_COLONEL_MUSTARD, 'playerName': "Zach"}, 'post')

    # make first moves for all players
    http_request(context_game + '/location', {'locName': LOCATION_NAME_HALLWAY_65, 'playerName': "Zach", 'charName': CHARACTER_NAME_COLONEL_MUSTARD}, 'post')
    http_request(context_game + '/complete-turn', {'playerName': "Zach", 'charName': CHARACTER_NAME_COLONEL_MUSTARD}, 'post')
    http_request(context_game + '/location', {'locName': LOCATION_NAME_HALLWAY_52, 'playerName': "Megan", 'charName': CHARACTER_NAME_MRS_WHITE}, 'post')
    http_request(context_game + '/complete-turn', {'playerName': "Megan", 'charName': CHARACTER_NAME_MRS_WHITE}, 'post')
    http_request(context_game + '/location', {'locName': LOCATION_NAME_HALLWAY_65, 'playerName': "Alex", 'charName': CHARACTER_NAME_MR_GREEN}, 'post')
    http_request(context_game + '/complete-turn', {'playerName': "Alex", 'charName': CHARACTER_NAME_MR_GREEN}, 'post')

    # make move, then suggestion for first player
    game_data = http_request(context_game + '/location', {'locName': LOCATION_NAME_BALL_ROOM, 'playerName': "Zach", 'charName': CHARACTER_NAME_COLONEL_MUSTARD}, 'post').json()
    http_request(
        context_game + '/suggestion', 
        {
            'room': LOCATION_NAME_BALL_ROOM,
            'weapon': 'dummy',
            'suspect': 'dummy',
            'playerName': "Zach", 
            'charName': CHARACTER_NAME_COLONEL_MUSTARD
        }, 
        'post'
    )

    # have another player reveal clue
    # complete turn for first player

    # make move for second player
    # make suggestion for second player (suggest cards that match mysterCards to test use-case)
    # complete_turn for second player

    # make wrong accusation for third player

    # make move for first player
    # make correct accusation for first player



