import json
from abc import abstractmethod
from time import sleep
from mfrc522 import SimpleMFRC522

from src.authorize import authenticate
from src.led import red, green
from src.sensor import is_closed

reader = SimpleMFRC522()


class State:
    @abstractmethod
    def handle(self):
        pass


class Closed(State):
    def handle(self):
        id, text = reader.read()
        print("id", id)
        print("text", text)
        user_id_json = json.loads(text)
        authenticated = authenticate(user_id_json)
        print(authenticated)

        if authenticated:
            return Green()
        else:
            return Red()


class Open(State):
    def handle(self):
        sleep(10)
        if is_closed():  # is_open()
            return Closed()
        else:
            return Blinking()


class Red(State):
    def handle(self):
        red()
        return Closed()


class Green(State):
    def handle(self):
        green()
        return Open()


class Blinking(State):
    def handle(self):
        while not is_closed():
            red()
        return Closed()
