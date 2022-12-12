import * as React from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';

export default function NewBoxFormDialog() {
    const [open, setOpen] = React.useState(false);

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };
    //TODO handleAdd hinzufügen bei der eine Request an den Server geschickt wird

    return (
        <div>
            <Button sx={{ml: 3, mt: 3}} variant="outlined" onClick={handleClickOpen}>
                Add new Box
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
                    <Button onClick={handleClose}>Add</Button>
                </DialogActions>
            </Dialog>
        </div>
    );
}