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

export default function ChangeBoxFormDialog() {
    let box= ["Box A", "RaspiID12345"]
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
                <DialogTitle>Editing the box profile of {box[0]}</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Here you can change the properties of your box. Only enter the information you would like to change.
                    </DialogContentText>
                    <TextField
                        autoFocus
                        margin="dense"
                        id="Raspi ID"
                        label="Raspi ID"
                        type="Raspi ID"
                        fullWidth
                        variant="standard"
                    />
                    <TextField
                        autoFocus
                        margin="dense"
                        id="Street Address"
                        label="Street Address"
                        type="Street Address"
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