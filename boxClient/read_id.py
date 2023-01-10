import RPi.GPIO as GPIO
from mfrc522 import SimpleMFRC522

reader = SimpleMFRC522()

try:
    _, text = reader.read()
    print("Current value of this rfid chip: \"" + text + "\"")
finally:
    GPIO.cleanup()