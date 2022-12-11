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
import DeleteUserAlertDialog from "./dialoges/deleteUserAlertDialog";
import {Container} from "@mui/material";
import ChangeUserFormDialog from "./dialoges/changeUserFormDialog";

//TODO statt dem Icon Button den Bearbeiten Dialog hinterlegen
function generate() {
    return users.map((value) => (
            <ListItem
                divider
                secondaryAction={
                    <Grid container spacing={0}>
                        <Grid item xs={8}>
                            <ChangeUserFormDialog />
                        </Grid>
                        <Grid item xs={4}>
                            <DeleteUserAlertDialog/>
                        </Grid>
                    </Grid>
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
        <Container>
            <Typography sx={{ mt: 4, mb: 2 , ml:3}} variant="h6" component="div">
                Users
            </Typography>
            <Demo>
                <List dense={dense}>
                    {generate()}
                </List>
            </Demo>
        </Container>
    );
}