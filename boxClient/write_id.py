from mfrc522 import SimpleMFRC522

reader = SimpleMFRC522()

new_id = input("Enter new ID:")
reader.write(new_id)
print("Successfully written \"" + new_id+ "\" to rfid chip")