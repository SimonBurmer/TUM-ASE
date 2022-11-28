import json
from time import sleep

import RPi.GPIO as GPIO
from mfrc522 import SimpleMFRC522

from src.authorize import authenticate
from src.led import green, red, blink_red
from src.sensor import is_closed

reader = SimpleMFRC522()

try:
    while True:
        id, text = reader.read()
        user_id_json = json.loads(text)
        authenticated = authenticate(user_id_json)

        if authenticated:

            # light led green
            print("Successfully authenticated")
            green()

            # after 10s check if closed
            sleep(10)
            while not is_closed():
                # if not blink red until it is closed
                blink_red()
        else:
            print("Unsuccessful authentication attempt")
            red()

except KeyboardInterrupt:
    GPIO.cleanup()
    raise