import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";
import axios from "axios";
import {Jose} from 'jose-jwe-jws';
import {apiUrl} from "../constants";

export const initialStateCurrUser = {
    userRole: "",
    loginState: "logout",
    mailAddress: "",
}

const api = axios.create({baseURL: apiUrl, withCredentials: true})


export const currUserSlice = createSlice({
    name: 'role',
    initialState: initialStateCurrUser,

    reducers: {
        resetStateCurrUser: (state) => {

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
            .addCase(authUserAsync.rejected, (state, action) => {
                console.log("login Failed: " + action.payload);
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
    async (payload, {rejectWithValue}) => {

        await console.log(`Attempting login with email: ${payload.email} and ${payload.password}`)
        try {
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

                        let password = await encrypter.encrypt(payload.password)

                        await console.log(`encrypted pw: ${password}`);
                        return password

                    });
            await api.post('/auth', {email: payload.email, password_enc: encryptedPassword});
            const userInfo = await api.get('user/current')
            return userInfo.data
        } catch (err) {
            return rejectWithValue(err)
        }
    }
);

export default currUserSlice.reducer
export const selectUserRole = (state) => state.role.userRole;
export const selectLoginState = (state) => state.role.loginState;
export const selectUserMail = (state) => state.role.mailAddress;
export const {resetStateCurrUser} = currUserSlice.actions;

