import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";
import axios from "axios";
import {Jose} from 'jose-jwe-jws';

//TODO den default auf null setzten und bei login auf die rolle des angemeldeten users setzten
const initialState = {
    userRole: "",
    loginState: "logout",
}

const api = axios.create({baseURL: 'http://localhost:10789', withCredentials: true}) //TODO move to specific file for constants


export const userSlice = createSlice({
    name: 'role',
    initialState,

    reducers: {
        logout: (state) => {
            state.loginState = "logout";
            state.userRole = "";
        },

    },
    extraReducers: (builder) => {
        builder
            .addCase(postUserAsync.fulfilled, (state, action) => {
                //hier im store speichern
                console.log(action.payload)
                state.userRole = action.payload
                if (state.loginState !== "loggedIn") {
                    state.loginState = "loggedIn"
                }
            })
            .addCase(postUserAsync.rejected, (state) => {
                console.log("login Failed");
                if (state.loginState !== "failed") {
                    state.loginState = "failed";
                }
            })
            .addCase(postUserAsync.pending, (state) => {
                if (state.loginState !== "pending") {
                    state.loginState = "pending";
                }
            });
    },
})

export const postUserAsync = createAsyncThunk(
    'user/postUser',
    async (payload) => {

        await console.log(`Attempting login with email: ${payload.email} and ${payload.password}`)

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

                    await console.log(`encrypted pw: ${password}`);
                    return password

                });
        const response = await api.post('/auth', {email: payload.email, password_enc: encryptedPassword})
        const userInfo = await api.get('user/current')
        return userInfo.data.role
    }
);

export default userSlice.reducer
export const selectUserRole = (state) => state.role.userRole;
export const selectLoginState = (state) => state.role.loginState;
export const {logout} = userSlice.actions;

