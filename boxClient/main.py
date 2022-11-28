import RPi.GPIO as GPIO

from src.state import Closed


state = Closed()

try:
    while True:
        new_state = state.handle()
        print("New state is: " + new_state.__class__.__name__)
        state = new_state

except KeyboardInterrupt:
    GPIO.cleanup()
    raise