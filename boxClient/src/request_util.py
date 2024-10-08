import os

import requests

hostUrl = os.getenv("REMOTE_URL")


def httpRequest(method, endpoint, headers=None, content=None):
    if method == 'GET':
        res = requests.get(hostUrl + endpoint, headers=headers, verify=False)
        return res
    elif method == 'PUT':
        res = requests.put(hostUrl + endpoint, headers=headers, json=content, verify=False)
        return res
    else:
        raise ValueError('Method Not Found')


def bearerHeader(bearer_token):
    return {
        "Authorization": "Bearer " + bearer_token
    }


def xsrfHeader(xsrf_token):
    return {
        # apparently, the CsrfRequestFilter in Spring compares the token in the header to the one in the cookie
        # which is why we 'simulate' the cookie here
        "Cookie": "XSRF-TOKEN=" + xsrf_token,
        "X-XSRF-TOKEN": xsrf_token
    }


def getXSRFToken():
    r = httpRequest('GET', '/auth/pkey')
    if r.status_code == 200:
        return r.cookies.get("XSRF-TOKEN")
    else:
        raise ConnectionError("Unable to obtain XSRF-Token")
