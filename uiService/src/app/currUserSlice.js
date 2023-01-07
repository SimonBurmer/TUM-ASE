import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";
import axios from "axios";
import {Jose} from 'jose-jwe-jws';

//TODO den default auf null setzten und bei login auf die rolle des angemeldeten users setzten
const initialState = {
    userRole: "",
    loginState: "logout",
    mailAddress: "",
}

const api = axios.create({baseURL: 'http://localhost:10789', withCredentials: true}) //TODO move to specific file for constants


export const currUserSlice = createSlice({
    name: 'role',
    initialState,

    reducers: {
        logout: (state) => {
            state.loginState = "logout";
            state.userRole = "";
            state.mailAddress = "";
        },

    },
    extraReducers: (builder) => {
        builder
            .addCase(authUserAsync.fulfilled, (state, action) => {
                //hier im store speichern
                state.userRole = action.payload.role
                state.mailAddress = action.payload.email

                if (state.loginState !== "loggedIn") {
                    state.loginState = "loggedIn"
                }
            })
            .addCase(authUserAsync.rejected, (state) => {
                console.log("login Failed");
                if (state.loginState !== "failed") {
                    state.loginState = "failed";
                }
            })
            .addCase(authUserAsync.pending, (state) => {
                if (state.loginState !== "pending") {
                    state.loginState = "pending";
                }
            });
    },
})

export const authUserAsync = createAsyncThunk(
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
        await api.post('/auth', {email: payload.email, password_enc: encryptedPassword});
        const userInfo = await api.get('user/current')
        return userInfo.data
    }
);

export default currUserSlice.reducer
export const selectUserRole = (state) => state.role.userRole;
export const selectLoginState = (state) => state.role.loginState;
export const selectUserMail = (state) => state.role.mailAddress;
export const {logout} = currUserSlice.actions;

