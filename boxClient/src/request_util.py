import os

import requests

hostname = os.getenv("REMOTE_HOST")
port = os.getenv("REMOTE_PORT")
hostUrl = "http://" + hostname + ":" + str(port)


def httpRequest(method, endpoint, headers=None, content=None):
    if method == 'GET':
        res = requests.get(hostUrl + endpoint,  headers=headers)
        return res
    elif method == 'PUT':
        res = requests.put(hostUrl + endpoint, headers=headers, json=content)
        return res
    else:
        raise ValueError('Method Not Found')


def bearerHeader(bearer_token):
    return {
        "Authorization": "Bearer " + bearer_token
    }

def xsrfHeader(xsrf_token):
    return {
        "X-XSRF-Token": xsrf_token
    }


def getXSRFToken():
    r = httpRequest('GET', '/auth/pkey')
    if r.status_code == 200:
        return r.cookies.get("XSRF-TOKEN")
    else:
        raise ConnectionError("Unable to obtain XSRF-Token")
