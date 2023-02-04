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
import {updateUserAsync} from "../../../app/userSlice";
import EditIcon from "@mui/icons-material/Edit";
import IconButton from "@mui/material/IconButton";

export default function ChangeUserFormDialog({
                                                 userId,
                                                 userMail,
                                                 userRole,
                                                 userRfid
                                             }) {
    const [open, setOpen] = React.useState(false);
    const [newRole, setNewRole] = useState(userRole)
    const [newRfid, setNewRfid] = useState(userRfid)
    const [newPassword, setNewPassword] = useState("")
    const [newEmail, setNewEmail] = useState(userMail);


    const dispatch = useDispatch()

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    const handleChange = () => {
        if (newEmail !== "" && newRole !== "" && newRfid !== "") {
            setOpen(false);
            dispatch(updateUserAsync({
                id: userId,
                email: newEmail,
                password: newPassword,
                role: newRole,
                rfid: newRfid,
            }))
        }
    }

    return (
        <div>
            <IconButton edge="end" aria-label="edit" onClick={handleClickOpen}>
                <EditIcon/>
            </IconButton>
            <Dialog open={open} onClose={handleClose}>
                <DialogTitle>Edit User</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Please edit the information you want to change. If you enter no new password it will stay the
                        same.
                    </DialogContentText>
                    <TextField
                        autoFocus
                        margin="dense"
                        id="E-Mail"
                        label="E-Mail"
                        type="E-Mail"
                        fullWidth
                        defaultValue={userMail}
                        variant="standard"
                        onChange={(e) => {
                            setNewEmail(e.target.value)
                        }}
                    />
                    <TextField
                        disabled={newRole === "DISPATCHER"}
                        autoFocus
                        margin="dense"
                        id="Rfid"
                        label="Rfid"
                        type="Rfid"
                        fullWidth
                        defaultValue={userRfid}
                        variant="standard"
                        onChange={(e) => {
                            setNewRfid(e.target.value)
                        }}
                    />
                    <TextField
                        autoFocus
                        margin="dense"
                        id="Password"
                        label="New Password"
                        type="Password"
                        fullWidth
                        variant="standard"
                        onChange={(e) => {
                            setNewPassword(e.target.value)
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