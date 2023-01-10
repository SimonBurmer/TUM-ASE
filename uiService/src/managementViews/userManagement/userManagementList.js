import * as React from 'react';
import {useState} from 'react';
import {styled} from '@mui/material/styles';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemAvatar from '@mui/material/ListItemAvatar';
import ListItemText from '@mui/material/ListItemText';
import Avatar from '@mui/material/Avatar';
import Grid from '@mui/material/Grid';
import Typography from '@mui/material/Typography';
import PersonIcon from '@mui/icons-material/Person';
import DeleteUserAlertDialog from "./dialoges/deleteUserAlertDialog";
import {Container} from "@mui/material";
import ChangeUserFormDialog from "./dialoges/changeUserFormDialog";
import {useSelector} from "react-redux";
import NewRequestErrorDialog from "../requestErrorDialog";
import {selectUsers} from "../../app/userSlice";
import NewUserFormDialog from "./dialoges/newUserFormDialog";

//TODO statt dem Icon Button den Bearbeiten Dialog hinterlegen
function generate(users) {
    return users.map((user) => (
            <ListItem
                key={user.id}
                divider
                secondaryAction={
                    <Grid container spacing={0}>
                        <Grid item xs={8}>
                            <ChangeUserFormDialog userId={user.id}
                                                  userMail={user.email}
                                                  userRole={user.role}
                                                  userRfid={user.rfid}/>
                        </Grid>
                        <Grid item xs={4}>
                            <DeleteUserAlertDialog userId={user.id}/>
                        </Grid>
                    </Grid>
                }
            >
                <ListItemAvatar>
                    <Avatar>
                        <PersonIcon/>
                    </Avatar>
                </ListItemAvatar>
                <Typography component={'div'}>
                    <ListItemText
                        primary={user.email + " | Role: " + user.role}
                        secondary={"Rfid: " + user.rfid + " | userID: " + user.id}
                        // secondary={<List>
                        //     <ListItemText>
                        //         {user.address}
                        //     </ListItemText>
                        //     <ListItemText>
                        //         {user.rasPiId} || {user.id}
                        //     </ListItemText>
                        // </List>}
                    />
                </Typography>
            </ListItem>
        )
    );
}

//TODO Liste and Objekten an users übergeben und generate und IntercativeList entsprechend anpassen

const Demo = styled('div')(({theme}) => ({
    backgroundColor: theme.palette.background.paper,
}));

export default function UserManagementList() {
    const [dense, setDense] = useState(false);
    const users = useSelector(selectUsers)

    return (
        <Container>
            <NewRequestErrorDialog/>
            <Grid container spacing={2}>
                <Grid item xs={10}>
                    <Typography sx={{mt: 3, mb: 2, ml: 3}} variant="h6" component="div">
                        User
                    </Typography>
                </Grid>
                <Grid item xs={2}>
                    <NewUserFormDialog/>
                </Grid>
            </Grid>
            <Demo>
                <List dense={dense}>
                    {generate(users)}
                </List>
            </Demo>
        </Container>
    );
}