import * as React from "react";
import {useEffect, useState} from "react";
import Button from "@mui/material/Button";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import DialogContentText from "@mui/material/DialogContentText";
import DialogActions from "@mui/material/DialogActions";
import {useDispatch, useSelector} from "react-redux";
import {resetErrorBoxes, selectBoxRequestError} from "../app/boxSlice";
import {resetErrorUser, selectUserRequestError} from "../app/userSlice";
import {resetErrorDeliveries, selectDeliveryRequestError} from "../app/deliverySlice";

export default function NewRequestErrorDialog() {
    const [open, setOpen] = useState(false);
    const boxRequestError = useSelector(selectBoxRequestError)
    const userRequestError = useSelector(selectUserRequestError)
    const deliveryRequestError = useSelector(selectDeliveryRequestError)
    const [errorMsg, setErrorMsg] = useState("")
    const dispatch = useDispatch();

    useEffect(() => {
        if (boxRequestError !== "") {
            setErrorMsg(boxRequestError)
            setOpen(true)
            dispatch(resetErrorBoxes)

        }
    }, [boxRequestError])

    useEffect(() => {
        if (userRequestError !== "") {
            setErrorMsg(userRequestError)
            setOpen(true)
            dispatch(resetErrorUser)
        }
    }, [userRequestError])

    useEffect(() => {
        if (deliveryRequestError !== "") {
            setErrorMsg(deliveryRequestError)
            setOpen(true)
            dispatch(resetErrorDeliveries)
        }
    }, [deliveryRequestError])

    const handleClose = () => {
        setOpen(false);
    };


    return (
        <div>
            <Dialog open={open} onClose={handleClose}>
                <DialogTitle>Request Error</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        {errorMsg}
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>OK</Button>
                </DialogActions>
            </Dialog>
        </div>
    );
}