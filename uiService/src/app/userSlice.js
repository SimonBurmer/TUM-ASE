import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";
import axios from "axios";
import {Jose} from "jose-jwe-jws";
import {apiUrl} from "../constants";
import {FormControl, InputLabel, Select} from "@mui/material";
import MenuItem from "@mui/material/MenuItem";
import * as React from "react";
import {useSelector} from "react-redux";

const initialState = {
    users: [],
    requestError: ""
}

const api = axios.create({baseURL: apiUrl, withCredentials: true})


export const userSlice = createSlice({
    name: 'user',
    initialState,
    reducers: {
        resetStateUsers: (state) => {
            state.users = []
            state.requestError = ""
        },
        resetErrorUser: (state) => {
            state.requestError = ""
        }
    },
    extraReducers: (builder) => {
        builder
            .addCase(getUsersAsync.fulfilled, (state, action) => {
                //hier im store speichern
                state.users = action.payload
            })
            .addCase(deleteUserAsync.fulfilled, (state, action) => {
                state.users = state.users.filter(object => {
                    return object.id !== action.payload
                })
            })
            .addCase(deleteUserAsync.rejected, (state, action) => {
                state.requestError = "Error while deleting user with Id: " +
                    action.payload.delUserId + ": " + action.payload.errMsg
            })
            .addCase(createUserAsync.fulfilled, (state, action) => {
                //hier im store speichern
                state.users.push(action.payload)
            })
            .addCase(createUserAsync.rejected, (state, action) => {
                state.requestError = "Error while creating user with E-Mail: " +
                    action.payload.createUserId + ": " + action.payload.errMsg

            })
            .addCase(updateUserAsync.fulfilled, (state, action) => {
                //hier im store speichern
                state.users = state.users.map((user) => {
                    if (user.id === action.payload.id) {
                        return action.payload;
                    }
                    return user
                })
            })
            .addCase(updateUserAsync.rejected, (state, action) => {
                state.requestError = "Error while updating user with E-Mail: " +
                    action.payload.updateUserId + ": " + action.payload.errMsg
            })
    }
})

export const getUsersAsync = createAsyncThunk(
    'user/getUsers',
    async () => {
        const users = await api.get('/user')
        return users.data
    }
);

export const deleteUserAsync = createAsyncThunk(
    'user/deleteUser',
    async (userId, {rejectWithValue}) => {
        try {
            await api.delete('/user/' + userId)
            return userId
        } catch (err) {
            return rejectWithValue({delUserId: userId, errMsg: err.response.data.message})
        }
    }
);

export const createUserAsync = createAsyncThunk(
    'user/createUser',
    async (newUserArg, {rejectWithValue}) => {
        const {email, password, role, rfid} = newUserArg
        console.log(email, password, role, rfid)

        const publicKey = api.get('/auth/pkey')
        let encryptedPassword =
            await publicKey.then((response) => {
                let rsaKey = Jose.Utils.importRsaPublicKey({
                    "e": parseInt(response.data.e),
                    "n": response.data.n
                }, "RSA-OAEP");
                return rsaKey;
            })
                .then(async (rsaKey) => {

                    let cryptographer = await new Jose.WebCryptographer();
                    let encrypter = await new Jose.JoseJWE.Encrypter(cryptographer, rsaKey);

                    let password_enc = encrypter.encrypt(password)

                    await console.log(`encrypted pw: ${password_enc}`);
                    return password_enc

                });

        try {
            const newUser = await api.post('/user/create', {
                email: email,
                password_enc: encryptedPassword,
                role: role,
                rfid: rfid
            })
            return newUser.data
        } catch (err) {
            return rejectWithValue({createUserId: email, errMsg: err.response.data.message})
        }
    }
);

export const updateUserAsync = createAsyncThunk(
    'user/updateUser',
    async (updateUser, {rejectWithValue}) => {
        const {id, email, password, rfid, role} = updateUser
        let encryptedPassword = ""
        if (password !== "") {
            const publicKey = api.get('/auth/pkey')
            encryptedPassword =
                await publicKey.then((response) => {
                    return Jose.Utils.importRsaPublicKey({
                        "e": parseInt(response.data.e),
                        "n": response.data.n
                    }, "RSA-OAEP");
                })
                    .then(async (rsaKey) => {

                        let cryptographer = await new Jose.WebCryptographer();
                        let encrypter = await new Jose.JoseJWE.Encrypter(cryptographer, rsaKey);

                        return encrypter.encrypt(password)

                    });

        }

        try {
            const updatedUser = await api.put('/user/' + id, {
                email: email,
                password_enc: encryptedPassword,
                role: role,
                rfid: rfid
            })
            return updatedUser.data
        } catch (err) {
            return rejectWithValue({updateUserId: email, errMsg: err.response.data.message})
        }
    }
);

export function UserDropDown({defaultUser, role, callbackChange}) {
    const selectorUsers = useSelector(selectUsers)
    return (<FormControl fullWidth>
        <InputLabel id="SelectUser">{role}</InputLabel>
        <Select
            labelId="User Selection"
            id="user"
            value={JSON.stringify(defaultUser) ?? null}
            label="Role"
            onChange={(e) => {
                callbackChange(JSON.parse(e.target.value))
            }}
        >
            {selectorUsers.map(user => {
                if (user.role === role) {
                    return <MenuItem key={user.id} value={JSON.stringify(user)}>{user.email}</MenuItem>
                }
            })
            }
        </Select>
    </FormControl>)
}


export default userSlice.reducer
export const selectUsers = (state) => state.user.users;
export const selectUserRequestError = (state) => state.user.requestError;
export const {resetStateUsers, resetErrorUser} = userSlice.actions;

