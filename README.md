# Clue-less

This repository contains all of the source code required for delivering the Clue-less Game as part of JHU's Foundation of Software Engineering Team Project.

## Project Description

This game is a simplified version of the popular board game, Clue®. The main simplification is in the navigation of the game board. In Clue-Less there are the same nine rooms, six weapons, and six people as in the board game. The rules are pretty much the same except for moving from room to room.

## Game Rules ##
* The rooms are laid out in a 3x3 grid with a hallway separating each pair of adjacent rooms. (See fig. 1.)
* Each hallway only holds one person. If someone is currently in a hallway, you may not move there.
* When it is your turn, you don’t need to roll a die.
* Your options of moving are limited to the following:
  * If you are in a room, you may do one of the following:
    * Move through one of the doors to the hallway (if it is not blocked).
    * Take a secret passage to a diagonally opposite room (if there is one) and make a suggestion.
    * If you were moved to the room by another player making a suggestion, you may, if you wish, stay in that room and make a suggestion. Otherwise you may move through a doorway or take a secret passage as described above.
  * If you are in a hallway, you must do the following:
    * Move to one of the two rooms accessible from that hallway and make a suggestion.
* If all of the exits are blocked (i.e., there are people in all of the hallways) and you are not in one of the corner rooms (with a secret passage), and you weren’t moved to the room by another player making a suggestion, you lose your turn (except for maybe making an accusation).
* Your first move must be to the hallway that is adjacent to your home square. The inactive characters stay in their home squares until they are moved to a room by someone making a suggestion.
* Whenever a suggestion is made, the room must be the room the one making the suggestion is currently in. The suspect in the suggestion is moved to the room in the suggestion.
* You may make an accusation at any time during your turn.
* You don’t need to show weapons on the board, since they really don’t do anything (but this would be a nice feature). But you should show where each of the characters is.

## Requirements ## 
* Each player should access the game from a separate computer, with a graphical user interface.
* The game rules are the same as in regular Clue except for the navigation (which is described above).
* Each time the game state changes (a person is moved, a suggestion is made, a player disproves a suggestion, or a player is unable to disprove a suggestion) all players should be notified.
* You should document the message interfaces between the clients and the Clue-Less server in your software requirements specification document. There will be messages from a client to
the server, messages from the server to a specific client, and broadcast messages to all clients from the server.
* These messages should be considered as triggers for use cases at the subsystem (client or server) level.
* Consider using a text based client for the minimal system, and a GUI version in the target.
* Feel free to change any of the names of people, rooms or weapons, as well as graphics.
* In fact, it would be a nice feature for a player to be able to load this set of names and graphics at the beginning of a game. This set of names and graphics should be stored in the client. Messages to and from the server should use standard identifiers of people, weapons and rooms, and the mapping from the standard names to the user selected names should be done in the client.

