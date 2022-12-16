import * as React from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import EditIcon from "@mui/icons-material/Edit";
import IconButton from "@mui/material/IconButton";

export default function ChangeDeliveryFormDialog() {
    let delivery= ["Delivery A", "DID1234567"]
    const [open, setOpen] = React.useState(false);

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    return (
        <div>
            <IconButton  edge="end" aria-label="delete" onClick={handleClickOpen}>
                <EditIcon/>
            </IconButton>
            <Dialog open={open} onClose={handleClose}>
                <DialogTitle>Editing {delivery[0]}</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Here you can change the properties of your delivery. Only enter the information you would like to change.
                    </DialogContentText>
                    <TextField
                        autoFocus
                        margin="dense"
                        id="name"
                        label="name"
                        type="name"
                        fullWidth
                        variant="standard"
                    />
                    <TextField
                        autoFocus
                        margin="dense"
                        id="DeliveryID"
                        label="DeliveryID"
                        type="DeliveryID"
                        fullWidth
                        variant="standard"
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>Cancel</Button>
                    <Button onClick={handleClose}>Change</Button>
                </DialogActions>
            </Dialog>
        </div>
    );
}