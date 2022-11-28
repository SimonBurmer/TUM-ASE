import json


def authenticate(user_id):
    with open("user_ids.json", "r") as file:
        valid_user_ids = json.load(file)
        return user_id in valid_user_ids