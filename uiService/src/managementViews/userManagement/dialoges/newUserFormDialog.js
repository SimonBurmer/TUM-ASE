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
import {createUserAsync} from "../../../app/userSlice";
import {FormControl, InputLabel, Select} from "@mui/material";
import MenuItem from "@mui/material/MenuItem";

export default function NewUserFormDialog() {
    const [open, setOpen] = React.useState(false);
    const [newEmail, setNewEmail] = useState("");
    const [newRole, setNewRole] = useState("")
    const [newRfid, setNewRfid] = useState("")
    const [newPassword, setNewPassword] = useState("")

    const dispatch = useDispatch()

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    const handleAdd = () => {
        if (newEmail !== "" && newRole !== "" && newRfid !== "") {
            setOpen(false);
            console.log(newEmail, newRfid, newRole, newPassword)
            dispatch(createUserAsync({
                email: newEmail,
                password: newPassword,
                role: newRole,
                rfid: newRfid,
            }))
        }
    }
    //TODO handleAdd hinzufügen bei der eine Request an den Server geschickt wird

    return (
        <div>
            <Button sx={{ml: 3, mt: 3}} variant="outlined" onClick={handleClickOpen}>
                Add new User
            </Button>
            <Dialog open={open} onClose={handleClose}>
                <DialogTitle>Add User</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Please enter the information on the User you want to add.
                    </DialogContentText>
                    <TextField
                        autoFocus
                        margin="dense"
                        id="E-Mail"
                        label="E-Mail"
                        type="E-Mail"
                        fullWidth
                        variant="standard"
                        onChange={(e) => {
                            setNewEmail(e.target.value)
                        }}
                    />
                    <TextField
                        autoFocus
                        margin="dense"
                        id="Rfid"
                        label="Rfid"
                        type="Rfid"
                        fullWidth
                        variant="standard"
                        onChange={(e) => {
                            setNewRfid(e.target.value)
                        }}
                    />
                    <TextField
                        autoFocus
                        margin="dense"
                        id="Password"
                        label="Password"
                        type="Password"
                        fullWidth
                        variant="standard"
                        onChange={(e) => {
                            setNewPassword(e.target.value)
                        }}
                    />

                    <FormControl fullWidth>
                        <InputLabel id="Role Selection">Role</InputLabel>
                        <Select
                            labelId="Role Selection"
                            id="role"
                            value={newRole}
                            label="Role"
                            onChange={(e) => {
                                setNewRole(e.target.value)
                            }}
                        >
                            <MenuItem value={"CUSTOMER"}>Customer</MenuItem>
                            <MenuItem value={"DELIVERER"}>Deliverer</MenuItem>
                            <MenuItem value={"DISPATCHER"}>Dispatcher</MenuItem>
                        </Select>
                    </FormControl>

                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>Cancel</Button>
                    <Button onClick={handleAdd}>Add</Button>
                </DialogActions>
            </Dialog>
        </div>
    );
}