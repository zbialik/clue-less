
import sys
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

# helper function for sending http requests to backend
def http_request(context = '', p = {}, type = 'get'):
    if (type == 'get'):
        return requests.get(BACKEND_ENDPOINT + context, params=p)
    elif (type == 'post'):
        return requests.post(BACKEND_ENDPOINT + context, params=p)