import RPi.GPIO as GPIO
from time import sleep

GREEN_GPIO = 18
RED_GPIO = 17

GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
GPIO.setup(RED_GPIO, GPIO.OUT, initial=GPIO.LOW)
GPIO.setup(GREEN_GPIO, GPIO.OUT, initial=GPIO.LOW)


def red():
    GPIO.output(RED_GPIO, GPIO.HIGH)
    sleep(1)
    GPIO.output(RED_GPIO, GPIO.LOW)


def green():
    GPIO.output(GREEN_GPIO, GPIO.HIGH)
    sleep(1)
    GPIO.output(GREEN_GPIO, GPIO.LOW)