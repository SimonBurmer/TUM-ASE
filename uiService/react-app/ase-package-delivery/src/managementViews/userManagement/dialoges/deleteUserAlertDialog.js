import * as React from 'react';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import DeleteIcon from "@mui/icons-material/Delete";
import IconButton from "@mui/material/IconButton";

export default function DeleteUserAlertDialog() {
    const [open, setOpen] = React.useState(false);

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    //TODO Buttons im diaolg müssen noch mit funktion versehen werden.
    //TODO der User sollte eine Nachricht bekommen ob der User gelöscht wurde
    return (
        <div>
            <IconButton  edge="end" aria-label="delete" onClick={handleClickOpen}>
                <DeleteIcon/>
            </IconButton>
            <Dialog
                open={open}
                onClose={handleClose}
                aria-labelledby="alert-dialog-title"
                aria-describedby="alert-dialog-description"
            >
                <DialogTitle id="alert-dialog-title">
                    {"Do you really want to delete this user?"}
                </DialogTitle>
                <DialogContent>
                    <DialogContentText id="alert-dialog-description">
                        If you delete this user you will not be able to restore it. Note: You can only delete a user if he/she is not assigned to a delivery.
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>Do not delete user</Button>
                    <Button onClick={handleClose} autoFocus>
                        Delete User
                    </Button>
                </DialogActions>
            </Dialog>
        </div>
    );
}