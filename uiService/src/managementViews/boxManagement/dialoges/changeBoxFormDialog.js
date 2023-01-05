import * as React from 'react';
import {useState} from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import EditIcon from "@mui/icons-material/Edit";
import IconButton from "@mui/material/IconButton";
import {useDispatch} from "react-redux";
import {updateBoxAsync} from "../../../app/boxSlice";

export default function ChangeBoxFormDialog({boxId, boxName, boxRaspId, boxAddress}) {
    const [open, setOpen] = React.useState(false);
    const [newName, setNewName] = useState("");
    const [newAddress, setNewAddress] = useState("")
    const [newRasPiId, setNewRasPiId] = useState("")

    const dispatch = useDispatch()

    const handleChange = () => {
        if (newName !== "" && newAddress !== "" && newRasPiId !== "") {
            setOpen(false);
            console.log(newName, newRasPiId, newAddress)
            dispatch(updateBoxAsync({boxId: boxId, boxName: newName, boxRasPiId: newRasPiId, boxAddress: newAddress}))
        }
    }

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    return (
        <div>
            <IconButton edge="end" aria-label="delete" onClick={handleClickOpen}>
                <EditIcon/>
            </IconButton>
            <Dialog open={open} onClose={handleClose}>
                <DialogTitle>Editing the box profile of {boxId}</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Here you can change the properties of your box. Only enter the information you would like
                        to change.
                        BoxID: {boxId}
                        BoxRaspID: {boxRaspId}
                        BoxName: {boxName}
                        BoxAddress: {boxAddress}

                    </DialogContentText>
                    <TextField
                        autoFocus
                        margin="dense"
                        id="Name"
                        label="Name"
                        type="Name"
                        fullWidth
                        variant="standard"
                        onChange={(e) => {
                            setNewName(e.target.value)
                        }}
                    />
                    <TextField
                        autoFocus
                        margin="dense"
                        id="Raspi ID"
                        label="Raspi ID"
                        type="Raspi ID"
                        fullWidth
                        variant="standard"
                        onChange={(e) => {
                            setNewRasPiId(e.target.value)
                        }}
                    />
                    <TextField
                        autoFocus
                        margin="dense"
                        id="Street Address"
                        label="Street Address"
                        type="Street Address"
                        fullWidth
                        variant="standard"
                        onChange={(e) => {
                            setNewAddress(e.target.value)
                        }}
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>Cancel</Button>
                    <Button onClick={handleChange}>Change</Button>
                </DialogActions>
            </Dialog>
        </div>
    );
}