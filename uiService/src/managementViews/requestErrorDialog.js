import * as React from "react";
import {useEffect, useState} from "react";
import Button from "@mui/material/Button";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import DialogContentText from "@mui/material/DialogContentText";
import DialogActions from "@mui/material/DialogActions";
import {useSelector} from "react-redux";
import {selectBoxRequestError} from "../app/boxSlice";
import {selectUserRequestError} from "../app/userSlice";

export default function NewRequestErrorDialog() {
    const [open, setOpen] = useState(false);
    const boxRequestError = useSelector(selectBoxRequestError)
    const userRequestError = useSelector(selectUserRequestError)
    const [errorMsg, setErrorMsg] = useState("")

    useEffect(() => {
        if (boxRequestError !== "") {
            setErrorMsg(boxRequestError)
            setOpen(true)
        }
    }, [boxRequestError])

    useEffect(() => {
        if (userRequestError !== "") {
            setErrorMsg(userRequestError)
            setOpen(true)
        }
    }, [userRequestError])

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