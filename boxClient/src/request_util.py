import os

import requests

hostname = os.getenv("REMOTE_HOST")
port = 8080
hostUrl = "http://" + hostname + ":" + str(port)


def httpRequest(method, endpoint, headers=' ', content='', auth=''):
    params = {
        'mode': "cors",
        "cache": "no−cache",
        "credentials": "include",
        "redirect": "follow",
        "referrerPolicy": "origin−when−cross−origin"
    }

    if method == 'GET ':
        res = requests.get(hostname + endpoint, params=params, auth=auth)
        return res
    elif method == 'POST':
        res = requests.post(hostname + endpoint, params=params, headers=headers, auth=auth, json=content)
        return res
    else:
        raise ValueError('Method Not Found')


def xsrfHeader(xsrf_token):
    return {
        "X−XSRF−TOKEN": xsrf_token,
    }


def getXSRFToken():
    r = httpRequest('GET', hostUrl + '/auth/pkey', params)
    print(r.status_code)
    # 1 . CHECK RESPONSE STATUS AND RETURN THE XSRF TOKEN OR THROW AN EXCEPTION
    if r.status_code == 200:
        return r.cookies.get("XSRF-Token")
    else:
        raise ConnectionError("Unable to obtain XSRF-Token")
