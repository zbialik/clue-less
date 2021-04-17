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

    # CREATE GAME
    game_id = http_request('', {'playerName': "Zach", 'charName': CHARACTER_NAME_COLONEL_MUSTARD}, 'post').json()['gameId']
    context_game = "/{}".format(game_id)

    # ADD PLAYERS
    trigger_http(context_game + '/players', {'playerName': "Megan", 'charName': CHARACTER_NAME_MRS_WHITE}, 'post')
    trigger_http(context_game + '/players', {'playerName': "Alex", 'charName': CHARACTER_NAME_MR_GREEN}, 'post')

    # START GAME
    trigger_http(context_game, {'activate': True, 'charName': CHARACTER_NAME_COLONEL_MUSTARD, 'playerName': "Zach"}, 'post')

    # MAKE FIRST MOVES FOR ALL 3 PLAYERS
    trigger_http(context_game + '/location', {'locName': LOCATION_NAME_HALLWAY_65, 'playerName': "Zach", 'charName': CHARACTER_NAME_COLONEL_MUSTARD}, 'post')
    trigger_http(context_game + '/complete-turn', {'playerName': "Zach", 'charName': CHARACTER_NAME_COLONEL_MUSTARD}, 'post')
    trigger_http(context_game + '/location', {'locName': LOCATION_NAME_HALLWAY_52, 'playerName': "Megan", 'charName': CHARACTER_NAME_MRS_WHITE}, 'post')
    trigger_http(context_game + '/complete-turn', {'playerName': "Megan", 'charName': CHARACTER_NAME_MRS_WHITE}, 'post')
    trigger_http(context_game + '/location', {'locName': LOCATION_NAME_HALLWAY_32, 'playerName': "Alex", 'charName': CHARACTER_NAME_MR_GREEN}, 'post')
    trigger_http(context_game + '/complete-turn', {'playerName': "Alex", 'charName': CHARACTER_NAME_MR_GREEN}, 'post')

    # FIRST PLAYER MAKES A MOVE AND THEN A SUGGESTION -- BALL ROOM, PROFESSOR PLUM, REVOLVER
    trigger_http(context_game + '/location', {'locName': LOCATION_NAME_BALL_ROOM, 'playerName': "Zach", 'charName': CHARACTER_NAME_COLONEL_MUSTARD}, 'post')
    trigger_http(context_game + '/suggestion/suggest', 
        {
            'room': LOCATION_NAME_BALL_ROOM,
            'weapon': WEAPON_NAME_REVOLVER,
            'suspect': CHARACTER_NAME_PROF_PLUM,
            'playerName': "Zach", 
            'charName': CHARACTER_NAME_COLONEL_MUSTARD
        }, 'post'
    )

    # NEXT PLAYER REVEALS CLUE -- PROFESSOR PLUM
    trigger_http(context_game + '/suggestion/reveal', 
        {
            'cardName': CHARACTER_NAME_PROF_PLUM,
            'playerName': "Megan", 
            'charName': CHARACTER_NAME_MRS_WHITE
        }, 'post'
    )

    # FIRST PLAYER ACCEPTS REVEALED CLUE
    # FIRST PLAYER COMPLETES THEIR TURN
    trigger_http(context_game + '/suggestion/accept', {'playerName': "Zach", 'charName': CHARACTER_NAME_COLONEL_MUSTARD}, 'post')
    trigger_http(context_game + '/complete-turn', {'playerName': "Zach", 'charName': CHARACTER_NAME_COLONEL_MUSTARD}, 'post')
    
    # SECOND PLAYER MAKES A MOVE AND THEN A SUGGESTION THAT NO ONE HAS A CLUE FOR
    # SECOND PLAYER COMPLETES TURN
    trigger_http(context_game + '/location', {'locName': LOCATION_NAME_KITCHEN, 'playerName': "Megan", 'charName': CHARACTER_NAME_MRS_WHITE}, 'post')
    trigger_http(context_game + '/suggestion/suggest', 
        {
            'room': LOCATION_NAME_KITCHEN,
            'weapon': WEAPON_NAME_KNIFE,
            'suspect': CHARACTER_NAME_COLONEL_MUSTARD,
            'playerName': "Megan", 
            'charName': CHARACTER_NAME_MRS_WHITE
        }, 'post'
    )
    trigger_http(context_game + '/complete-turn', {'playerName': "Megan", 'charName': CHARACTER_NAME_MRS_WHITE}, 'post')

    # THIRD PLAYER MAKES INCORRECT ACCUSATION -- LIBRARY, COLONEL MUSTARD, KNIFE
    trigger_http(context_game + '/accusation/accuse', 
        {
            'room': LOCATION_NAME_LIBRARY,
            'weapon': WEAPON_NAME_KNIFE,
            'suspect': CHARACTER_NAME_COLONEL_MUSTARD,
            'playerName': "Alex", 
            'charName': CHARACTER_NAME_MR_GREEN
        }, 'post'
    )

    # FIRST PLAYER MAKES MOVE THEN MAKES CORRECT ACCUSATION -- KITCHEN, COLONEL MUSTARD, KNIFE
    trigger_http(context_game + '/location', {'locName': LOCATION_NAME_HALLWAY_43, 'playerName': "Zach", 'charName': CHARACTER_NAME_COLONEL_MUSTARD}, 'post')
    trigger_http(context_game + '/accusation/accuse', 
        {
            'room': LOCATION_NAME_KITCHEN,
            'weapon': WEAPON_NAME_KNIFE,
            'suspect': CHARACTER_NAME_COLONEL_MUSTARD,
            'playerName': "Zach", 
            'charName': CHARACTER_NAME_COLONEL_MUSTARD
        }, 'post'
    )

