import json

import requests.exceptions
from dotenv import load_dotenv

load_dotenv()

import os
from time import sleep

import RPi.GPIO as GPIO
from mfrc522 import SimpleMFRC522

from src.request_util import getXSRFToken, httpRequest, xsrfHeader, bearerHeader
from src.led import green, red, blink_red
from src.sensor import is_closed

JWT = os.getenv("JWT")

reader = SimpleMFRC522()


def authenticate(rfid):
    print("Verifying rfid token: \"" + rfid + "\"")
    res = httpRequest(method="GET", endpoint="/rfid/" + rfid, headers=bearerHeader(JWT))
    if res.status_code == 200:
        return json.loads(str(res.content, "UTF-8"))
    if res.status_code == 401:
        return None
    else:
        raise ConnectionError("Unable to verify rfid: " + str(res.text))


def placeDeliveries(delivererId):
    print("Deliverer placed deliveries")
    xsrf_token = getXSRFToken()
    res = httpRequest(method="PUT", endpoint="/delivery/place",
                      headers=dict(xsrfHeader(xsrf_token), **bearerHeader(JWT)), content=delivererId)
    if res.status_code != 200:
        raise ConnectionError("Unable to place deliveries: " + str(res.text))


def retrieveDeliveries():
    print("Customer retrieved deliveries")
    xsrf_token = getXSRFToken()
    res = httpRequest(method="PUT", endpoint="/delivery/retrieve",
                      headers=dict(xsrfHeader(xsrf_token), **bearerHeader(JWT)))
    if res.status_code != 200:
        raise ConnectionError("Unable to retrieve deliveries: " + str(res.text))


try:
    while True:
        try:
            _, rfid = reader.read()
            if rfid == "":
                continue
            user = authenticate(rfid.strip())

            if user is not None:

                # light led green
                print("Successfully authenticated as " + str(user))
                green()

                # after 10s check if closed
                sleep(10)
                while not is_closed():
                    # if not blink red until it is closed
                    blink_red()
                print("Box has been closed")

                if user["role"] == "DELIVERER":
                    placeDeliveries(user["id"])

                if user["role"] == "CUSTOMER":
                    retrieveDeliveries()

            else:
                print("Unsuccessful authentication attempt")
                red()

        except requests.exceptions.ConnectionError:
            print("Remote host is not reachable")
        except Exception as e:
            print(e)

except KeyboardInterrupt:
    GPIO.cleanup()
    raise
