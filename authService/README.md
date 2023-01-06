# Data Model

## AseUser
```json
{
  "id": "...",
  "email": "...",
  "password" : "...",
  "role": "...",
  "rfid": "..."
}
```
- `password` is not serialized
- `rfid` only required if role is DELIVERER or CUSTOMER

# API Endpoints

| endpoint   | Methods | body                                     | output                              | comment                                             |
|------------|---------|------------------------------------------|-------------------------------------|-----------------------------------------------------|
| /auth/pkey | GET     |                                          | `{ "e": ..., "key": ..., "n": ...}` | Used to fetch the public key for encrypting password |
| /auth      | POST    | email, password_enc, remember (optional) | jwt auth token (as HttpOnly Cookie) | Used to authenticate users                          |


| endpoint       | Methods             | body                                   | output                                     | comment                     |
|----------------|---------------------|----------------------------------------|--------------------------------------------|-----------------------------|
| /user          | GET (DISPATCHER)    |                                        | list of all users                          |                             |
| /user/current  | GET                 |                                        | information about currently logged-in user |                             |
| /user/create   | POST (DISPATCHER)   | email, password, role, rfid (optional) | user (created)                             |                             | 
| /user/{email}  | PUT (DISPATCHER)    | email, password, rfid (optional)       | user (updated)                             | not possible to update role |
| /user/{email}  | DELETE (DISPATCHER) |                                        |                                            |                             | 