import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";
import axios from "axios";
import {Jose} from 'jose-jwe-jws';

//TODO den default auf null setzten und bei login auf die rolle des angemeldeten users setzten
const initialState = {
    userRole: "Dispatcher",
    isLoggedIn: false,
    apiToken: "",
}

export const userSlice = createSlice({
    name: 'role',
    initialState,

    reducers: {
        logout: (state) => {
            state.isLoggedIn = false;
            state.apiToken = "";
            state.userRole = "";
        },

    },
    extraReducers: (builder) => {
        builder
            .addCase(postUserAsync.fulfilled, (state, action) => {
                //hier im store speichern
            });
    },
})

export const postUserAsync = createAsyncThunk(
    'user/postUser',
    async (payload) => {

        await console.log(`Attempting login with email: ${payload.email} and ${payload.password}`)

        const api = axios.create({baseURL: 'http://127.0.0.1:10789'}) //TODO move to specific file for constants


        const publicKey = api.get('/auth/pkey')
        let encryptedPassword =
            await publicKey.then((response) => {
                let rsaKey = Jose.Utils.importRsaPublicKey({
                    "e": parseInt(response.data.e),
                    "n": response.data.n
                }, "RSA-OAEP");
                console.log("Key built");
                return rsaKey;
            })
                .then(async (rsaKey) => {

                    let cryptographer = await new Jose.WebCryptographer();
                    let encrypter = await new Jose.JoseJWE.Encrypter(cryptographer, rsaKey);

                    let password = encrypter.encrypt(payload.password)
                    // TODO: Replace encryptAct with a command to encrypt a password written in JSON (i.e., {" password": password}) pw = await encryptAct

                    await console.log(`encrypted pw: ${password}`);
                    return password

                });
        const response = await api.post('/auth', {email: payload.email, password_enc: encryptedPassword}, {headers: {"X-XSRF-Token" : "TODO: Add Token from Cookie here"}})
        console.log("Login successful" + response.data)
        return response.data.content

    }
);

export default userSlice.reducer
export const selectUserRole = (state) => state.role.userRole;
export const selectIsLoggedIn = (state) => state.role.isLoggedIn;
export const {logout} = userSlice.actions;

