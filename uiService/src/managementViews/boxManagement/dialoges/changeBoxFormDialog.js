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
    const [open, setOpen] = useState(false);
    const [newName, setNewName] = useState(boxName);
    const [newAddress, setNewAddress] = useState(boxAddress)
    const [newRasPiId, setNewRasPiId] = useState(boxRaspId)

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
                        Here you can edit the properties of your box you want to change.

                    </DialogContentText>
                    <TextField
                        autoFocus
                        margin="dense"
                        id="Name"
                        label="Name"
                        type="Name"
                        defaultValue={boxName}
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
                        defaultValue={boxRaspId}
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
                        defaultValue={boxAddress}
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