import * as React from 'react';
import {useState} from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import {useDispatch} from "react-redux";
import {createBoxAsync} from "../../../app/boxSlice";
import QrCodeScannerIcon from '@mui/icons-material/QrCodeScanner';

export default function PickupOrderDialog() {
    const [open, setOpen] = React.useState(false);
    const [newName, setNewName] = useState("");
    const [newAddress, setNewAddress] = useState("")
    const [newRasPiId, setNewRasPiId] = useState("")

    const dispatch = useDispatch()

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    const handleAdd = () => {
        if (newName !== "" && newAddress !== "" && newRasPiId !== "") {
            setOpen(false);
            console.log(newName, newRasPiId, newAddress)
            dispatch(createBoxAsync({boxName: newName, boxRasPiId: newRasPiId, boxAddress: newAddress}))
        }
    }

    return (
        <div>
            <Button sx={{ml: 3, mt: 3}} variant="contained" onClick={handleClickOpen} startIcon={<QrCodeScannerIcon/>}>
                Scan QR Code
            </Button>
            <Dialog open={open} onClose={handleClose}>
                <DialogTitle>Add Box</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Please enter the information on the Box you want to add.
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
                    <Button onClick={handleAdd}>Add</Button>
                </DialogActions>
            </Dialog>
        </div>
    );
}