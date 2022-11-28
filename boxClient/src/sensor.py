from time import sleep

import RPi.GPIO as GPIO


SENSOR_GPIO = 19

GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
GPIO.setup(SENSOR_GPIO, GPIO.IN)


def is_closed():
    value = GPIO.input(SENSOR_GPIO)
    return value == 0