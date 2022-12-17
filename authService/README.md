# API Endpoints

| endpoint | Methods | input                                                              | output                              | description                                          |
|----------|---------|--------------------------------------------------------------------|-------------------------------------|------------------------------------------------------|
| /auth/pkey | GET   |                                                                    | `{ "e": ..., "key": ..., "n": ...}` | Used to fetch the public key for encrypting password |
| /auth    | POST | `{ "email": "...", "password_enc": "...", "remember":true/false }` | jwt auth token (as HttpOnly Cookie) | Used to authenticate users, "remember" is optional   |

