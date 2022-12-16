import * as React from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import ChooseUserRoleConfirmationDialog from "./chooseUserRoleConfirmationDialog";

export default function NewUserFormDialog() {
    const [open, setOpen] = React.useState(false);

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };
    //TODO handleAdd hinzufügen bei der eine Request an den Server geschickt wird (Name und Email)

    return (
        <div>
            <Button sx={{ml: 3, mt: 3}} variant="outlined" onClick={handleClickOpen}>
                Add new User
            </Button>
            <Dialog open={open} onClose={handleClose}>
                <DialogTitle>Add User</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Please enter the information on the user you want to add.
                    </DialogContentText>
                    <TextField
                        autoFocus
                        margin="dense"
                        id="First Name"
                        label="First Name"
                        type="First Name"
                        fullWidth
                        variant="standard"
                    />
                    <TextField
                        autoFocus
                        margin="dense"
                        id="Last Name"
                        label="Last Name"
                        type="Last Name"
                        fullWidth
                        variant="standard"
                    />
                    <TextField
                        autoFocus
                        margin="dense"
                        id="email"
                        label="E-Mail"
                        type="email"
                        fullWidth
                        variant="standard"
                    />
                    <ChooseUserRoleConfirmationDialog />
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>Cancel</Button>
                    <Button onClick={handleClose}>Add</Button>
                </DialogActions>
            </Dialog>
        </div>
    );
}