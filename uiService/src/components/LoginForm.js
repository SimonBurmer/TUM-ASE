import React, {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {Form, FormikProvider, useFormik} from "formik";
import * as Yup from "yup";

import {
    Box,
    Checkbox,
    FormControlLabel,
    IconButton,
    InputAdornment,
    Stack,
    TextField,
    Typography,
} from "@mui/material";
import {LoadingButton} from "@mui/lab";
import {Icon} from "@iconify/react";
import {motion} from "framer-motion";
import {postUserAsync, selectLoginState, selectUserRole} from "../app/currUserSlice";
import {useDispatch, useSelector} from "react-redux";
import {getBoxesAsync} from "../app/boxSlice";

let easing = [0.6, -0.05, 0.01, 0.99];
const animate = {
    opacity: 1,
    y: 0,
    transition: {
        duration: 0.6,
        ease: easing,
        delay: 0.16,
    },
};

const LoginForm = () => {
    const navigate = useNavigate();
    const dispatch = useDispatch();

    const [showPassword, setShowPassword] = useState(false);
    const [loginError, setLoginError] = useState("")
    const [isSubmittingState, setIsSubmitting] = useState(false)

    const LoginSchema = Yup.object().shape({
        email: Yup.string()
            .email("Provide a valid email address")
            .required("Email is required"),
        password: Yup.string().required("Password is required"),
    });

    const formik = useFormik({
        initialValues: {
            email: "",
            password: "",
            remember: true,
        },
        validationSchema: LoginSchema,
        onSubmit: () => {
            setIsSubmitting(true)
            console.log("submitting...");
            dispatch(postUserAsync({email: formik.values.email, password: formik.values.password}))
        },
    });

    const {errors, touched, values, isSubmitting, handleSubmit, getFieldProps} =
        formik;
    const selectorLoginState = useSelector(selectLoginState)
    const selectorUserRole = useSelector(selectUserRole)

    useEffect(() => {
        switch (selectorLoginState) {
            case "loggedIn": {
                switch (selectorUserRole) {
                    case "DISPATCHER": {
                        dispatch(getBoxesAsync())
                        navigate("/mainPage/boxManagement");
                        break;
                    }
                    default:
                        navigate("/mainPage/deliveryManagement")
                }
                break;
            }
            case "failed": {
                if (loginError !== "Wrong password or email!") {
                    setLoginError("Wrong password or email!");
                    setIsSubmitting(false);
                }
                break;
            }
        }
    }, [selectorLoginState])
    return (
        <FormikProvider value={formik}>
            <Form autoComplete="off" noValidate onSubmit={handleSubmit}>
                <Box
                    component={motion.div}
                    animate={{
                        transition: {
                            staggerChildren: 0.55,
                        },
                    }}
                >
                    <Box
                        sx={{
                            display: "flex",
                            flexDirection: "column",
                            gap: 3,
                        }}
                        component={motion.div}
                        initial={{opacity: 0, y: 40}}
                        animate={animate}
                    >
                        <TextField
                            fullWidth
                            autoComplete="username"
                            type="email"
                            label="Email Address"
                            {...getFieldProps("email")}
                            error={Boolean(touched.email && errors.email)}
                            helperText={touched.email && errors.email}
                        />

                        <TextField
                            fullWidth
                            autoComplete="current-password"
                            type={showPassword ? "text" : "password"}
                            label="Password"
                            {...getFieldProps("password")}
                            error={Boolean(touched.password && errors.password)}
                            helperText={touched.password && errors.password}
                            InputProps={{
                                endAdornment: (
                                    <InputAdornment position="end">
                                        <IconButton
                                            onClick={() => setShowPassword((prev) => !prev)}
                                        >
                                            {showPassword ? (
                                                <Icon icon="eva:eye-fill"/>
                                            ) : (
                                                <Icon icon="eva:eye-off-fill"/>
                                            )}
                                        </IconButton>
                                    </InputAdornment>
                                ),
                            }}
                        />
                    </Box>

                    <Box
                        component={motion.div}
                        initial={{opacity: 0, y: 20}}
                        animate={animate}
                    >
                        <Stack
                            direction="row"
                            alignItems="center"
                            justifyContent="space-between"
                            sx={{my: 2}}
                        >
                            <FormControlLabel
                                control={
                                    <Checkbox
                                        {...getFieldProps("remember")}
                                        checked={values.remember}
                                    />
                                }
                                label="Remember me"
                            />
                        </Stack>

                        <LoadingButton
                            fullWidth
                            size="large"
                            type="submit"
                            variant="contained"
                            loading={isSubmittingState}
                        >
                            {isSubmittingState ? "loading..." : "Login"}
                        </LoadingButton>
                    </Box>
                    <Typography sx={{color: "red", mb: 5}}>
                        {loginError}
                    </Typography>
                </Box>
            </Form>
        </FormikProvider>
    );
};

export default LoginForm;
