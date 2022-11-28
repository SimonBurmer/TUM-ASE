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
        print("id", id)
        print("text", text)
        user_id_json = json.loads(text)
        authenticated = authenticate(user_id_json)
        print(authenticated)

        if authenticated:
            green()
            sleep(10)
            while not is_closed():
                blink_red()
        else:
            red()

except KeyboardInterrupt:
    GPIO.cleanup()
    raise