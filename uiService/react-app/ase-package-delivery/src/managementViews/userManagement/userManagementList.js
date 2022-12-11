import * as React from 'react';
import { styled } from '@mui/material/styles';
import Box from '@mui/material/Box';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemAvatar from '@mui/material/ListItemAvatar';
import ListItemText from '@mui/material/ListItemText';
import Avatar from '@mui/material/Avatar';
import IconButton from '@mui/material/IconButton';
import Grid from '@mui/material/Grid';
import Typography from '@mui/material/Typography';
import EditIcon from '@mui/icons-material/Edit';
import PersonIcon from '@mui/icons-material/Person';
import DeleteIcon from '@mui/icons-material/Delete';


function generate() {
    return users.map((value) => (
            <ListItem
                secondaryAction={
                <List>
                    <IconButton edge="end" aria-label="edit">
                        <EditIcon/>
                    </IconButton>
                    <IconButton sx={{ml:2}} edge="end" aria-label="delete">
                        <DeleteIcon/>
                    </IconButton>
                </List>
                }
            >
                <ListItemAvatar>
                    <Avatar>
                        <PersonIcon/>
                    </Avatar>
                </ListItemAvatar>
                <ListItemText
                    primary= {value}
                    secondary={'Email als UUID'}
                />
            </ListItem>
        )
    );
}
//TODO Liste and Objekten an users übergeben und generate und IntercativeList entsprechend anpassen
let users= ["Peter", "Simon", "Simon", "Leonard", "Felix"]

const Demo = styled('div')(({ theme }) => ({
    backgroundColor: theme.palette.background.paper,
}));

export default function InteractiveList() {
    const [dense, setDense] = React.useState(false);

    return (
        <Box sx={{ flexGrow: 1, maxWidth: 1500 }}>
            <Grid container spacing={2}>
                <Grid item xs={12} md={6}>
                    <Typography sx={{ mt: 4, mb: 2 , ml:3}} variant="h6" component="div">
                        Users
                    </Typography>
                    <Demo>
                        <List dense={dense}>
                            {generate()}
                        </List>
                    </Demo>
                </Grid>
            </Grid>
        </Box>
    );
}