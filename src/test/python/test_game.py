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
    http_request(context_game + '/location', {'locName': LOCATION_NAME_HALLWAY_32, 'playerName': "Alex", 'charName': CHARACTER_NAME_MR_GREEN}, 'post')
    http_request(context_game + '/complete-turn', {'playerName': "Alex", 'charName': CHARACTER_NAME_MR_GREEN}, 'post')

    # make move, then suggestion for first player -- BALL ROOM, PROFESSOR PLUM, REVOLVER
    http_request(context_game + '/location', {'locName': LOCATION_NAME_BALL_ROOM, 'playerName': "Zach", 'charName': CHARACTER_NAME_COLONEL_MUSTARD}, 'post')
    http_request(context_game + '/suggestion/suggest', 
        {
            'room': LOCATION_NAME_BALL_ROOM,
            'weapon': WEAPON_NAME_REVOLVER,
            'suspect': CHARACTER_NAME_PROF_PLUM,
            'playerName': "Zach", 
            'charName': CHARACTER_NAME_COLONEL_MUSTARD
        }, 'post'
    )

    # have next player reveal clue  -- PROFESSOR PLUM
    http_request(context_game + '/suggestion/reveal', 
        {
            'cardName': CHARACTER_NAME_PROF_PLUM,
            'playerName': "Megan", 
            'charName': CHARACTER_NAME_MRS_WHITE
        }, 'post'
    )

    # accept reveal for first player
    # complete turn for first player
    http_request(context_game + '/suggestion/accept', {'playerName': "Zach", 'charName': CHARACTER_NAME_COLONEL_MUSTARD}, 'post')
    http_request(context_game + '/complete-turn', {'playerName': "Zach", 'charName': CHARACTER_NAME_COLONEL_MUSTARD}, 'post')
    
    # make move for second player (to KITCHEN)
    # make suggestion for second player -- KITCHEN, COLONEL MUSTARD, KNIFE
    # complete_turn for second player
    http_request(context_game + '/location', {'locName': LOCATION_NAME_KITCHEN, 'playerName': "Megan", 'charName': CHARACTER_NAME_MRS_WHITE}, 'post')
    http_request(context_game + '/suggestion/suggest', 
        {
            'room': LOCATION_NAME_KITCHEN,
            'weapon': WEAPON_NAME_KNIFE,
            'suspect': CHARACTER_NAME_COLONEL_MUSTARD,
            'playerName': "Megan", 
            'charName': CHARACTER_NAME_MRS_WHITE
        }, 'post'
    )
    http_request(context_game + '/complete-turn', {'playerName': "Megan", 'charName': CHARACTER_NAME_MRS_WHITE}, 'post')

    # make wrong accusation for third player -- LIBRARY, COLONEL MUSTARD, KNIFE
    http_request(context_game + '/accusation/accuse', 
        {
            'room': LOCATION_NAME_LIBRARY,
            'weapon': WEAPON_NAME_KNIFE,
            'suspect': CHARACTER_NAME_COLONEL_MUSTARD,
            'playerName': "Alex", 
            'charName': CHARACTER_NAME_MR_GREEN
        }, 'post'
    )

    # make move for first player
    # make correct accusation for first player -- KITCHEN, COLONEL MUSTARD, KNIFE
    http_request(context_game + '/location', {'locName': LOCATION_NAME_HALLWAY_43, 'playerName': "Zach", 'charName': CHARACTER_NAME_COLONEL_MUSTARD}, 'post')
    http_request(context_game + '/accusation/accuse', 
        {
            'room': LOCATION_NAME_KITCHEN,
            'weapon': WEAPON_NAME_KNIFE,
            'suspect': CHARACTER_NAME_COLONEL_MUSTARD,
            'playerName': "Zach", 
            'charName': CHARACTER_NAME_COLONEL_MUSTARD
        }, 'post'
    )

