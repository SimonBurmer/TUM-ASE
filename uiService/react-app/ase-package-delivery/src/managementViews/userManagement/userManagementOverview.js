import React from "react";
import InteractiveList from "./userManagementList";
import Box from "@mui/material/Box";
import NewUserFormDialog from "./dialoges/newUserFormDialog";
import {Container} from "@mui/material";





export default function UserManagementOverview () {

    return (
        <Container>
            <InteractiveList></InteractiveList>
        </Container>
    )
}