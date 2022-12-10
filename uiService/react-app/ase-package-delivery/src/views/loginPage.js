import {ListItemButton} from "@mui/material";

function LoginPage() {
    return (
        <div>
            <h1>Login Page</h1>
            <ListItemButton href={"/mainPage"}>Login</ListItemButton>
        </div>
    )
}

export default LoginPage