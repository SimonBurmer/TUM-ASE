import * as React from 'react';
import { styled } from '@mui/material/styles';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemAvatar from '@mui/material/ListItemAvatar';
import ListItemText from '@mui/material/ListItemText';
import Avatar from '@mui/material/Avatar';
import Grid from '@mui/material/Grid';
import Typography from '@mui/material/Typography';
import PersonIcon from '@mui/icons-material/Person';
import DeleteBoxAlertDialog from "./dialoges/deleteBoxAlertDialog";
import {Container} from "@mui/material";
import ChangeBoxFormDialog from "./dialoges/changeBoxFormDialog";
import NewBoxFormDialog from "./dialoges/newBoxFormDialog";

//TODO statt dem Icon Button den Bearbeiten Dialog hinterlegen
function generate() {
    return boxes.map((value) => (
            <ListItem
                divider
                secondaryAction={
                    <Grid container spacing={0}>
                        <Grid item xs={8}>
                            <ChangeBoxFormDialog />
                        </Grid>
                        <Grid item xs={4}>
                            <DeleteBoxAlertDialog/>
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
                    secondary={'RaspiID als UUID'}
                />
            </ListItem>
        )
    );
}
//TODO Liste and Objekten an boxes übergeben und generate und IntercativeList entsprechend anpassen
let boxes= ["Box 1", "Box 2", "Box 3", "Box 4", "Box 5"]

const Demo = styled('div')(({ theme }) => ({
    backgroundColor: theme.palette.background.paper,
}));

export default function BoxManagementList() {
    const [dense, setDense] = React.useState(false);

    return (
        <Container>
            <Grid container spacing={2}>
                <Grid item xs={10}>
                    <Typography sx={{ mt: 3, mb: 2 , ml:3}} variant="h6" component="div">
                        Boxes
                    </Typography>
                </Grid>
                <Grid item xs={2}>
                    <NewBoxFormDialog />
                </Grid>
            </Grid>
            <Demo>
                <List dense={dense}>
                    {generate()}
                </List>
            </Demo>
        </Container>
    );
}