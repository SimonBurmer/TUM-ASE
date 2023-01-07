import json
import os
from time import sleep

import RPi.GPIO as GPIO
from mfrc522 import SimpleMFRC522

from src.request_util import getXSRFToken, httpRequest, xsrfHeader
from src.led import green, red, blink_red
from src.sensor import is_closed

from dotenv import load_dotenv

load_dotenv()

JWT = os.getenv("JWT")

reader = SimpleMFRC522()

def authenticate(rfid):
    res = httpRequest(method="GET", endpoint="/rfid/" + rfid, auth="Bearer " + JWT)
    if res.status_code == 200:
        return res.content
    else:
        raise ConnectionError("Unable to verify rfid")

def placeDeliveries():
    print("Deliverer placed deliveries")
    xsrf_token = getXSRFToken()
    res = httpRequest(method="POST", endpoint="/delivery/place", headers=xsrfHeader(xsrf_token), auth="Bearer " + JWT)
    if res.status_code != 200:
        raise ConnectionError("Unable to place deliveries")

def retrieveDeliveries():
    print("Customer retrieved deliveries")
    xsrf_token = getXSRFToken()
    res = httpRequest(method="POST", endpoint="/delivery/retrieve", headers=xsrfHeader(xsrf_token), auth="Bearer " + JWT)
    if res.status_code != 200:
        raise ConnectionError("Unable to retrieve deliveries")


try:
    while True:
        _, text = reader.read()
        rfid = json.loads(text)
        authenticated = authenticate(rfid)

        if authenticated is not None:

            # light led green
            print("Successfully authenticated as " + authenticated)
            green()

            # after 10s check if closed
            sleep(10)
            while not is_closed():
                # if not blink red until it is closed
                blink_red()

            if authenticated == "DELIVERER":
                placeDeliveries()

            if authenticated == "CUSTOMER":
                retrieveDeliveries()

        else:
            print("Unsuccessful authentication attempt")
            red()

except KeyboardInterrupt:
    GPIO.cleanup()
    raise
