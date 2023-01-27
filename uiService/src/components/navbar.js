import * as React from 'react';
import {useEffect, useState} from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import IconButton from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';
import Menu from '@mui/material/Menu';
import MenuIcon from '@mui/icons-material/Menu';
import Container from '@mui/material/Container';
import Button from '@mui/material/Button';
import Tooltip from '@mui/material/Tooltip';
import MenuItem from '@mui/material/MenuItem';
import {Link, useNavigate} from "react-router-dom"
import LocalShippingIcon from '@mui/icons-material/LocalShipping';
import PersonIcon from "@mui/icons-material/Person";
import {resetStateCurrUser, selectLoginState, selectUserMail, selectUserRole} from "../app/currUserSlice";
import {useDispatch, useSelector} from "react-redux";
import {getUsersAsync, resetStateUsers} from "../app/userSlice";
import {getBoxesAsync, resetStateBoxes} from "../app/boxSlice";
import {
    getDeliveriesDelivererCustomerAsync,
    getDeliveriesDispatcherAsync,
    resetStateDeliveries
} from "../app/deliverySlice";

const pagesDispatcher = ['userManagement', 'boxManagement', 'deliveryManagement'];
const pagesUserDeliverer = ['deliveryManagement']

function ResponsiveAppBar() {

    const [anchorElNav, setAnchorElNav] = useState(null);
    const [anchorElUser, setAnchorElUser] = useState(null);
    const selectorUser = useSelector(selectUserRole);
    const selectorMail = useSelector(selectUserMail)
    const pages = selectorUser === "DISPATCHER" ? pagesDispatcher : pagesUserDeliverer
    const dispatch = useDispatch();
    const navigate = useNavigate()
    const selectorLoginStatus = useSelector(selectLoginState)

    useEffect(() => {
        if (selectorLoginStatus === "logout") {
            navigate("/")
        }
    }, [selectorLoginStatus])

    const handleOpenNavMenu = (event) => {
        setAnchorElNav(event.currentTarget);
    };
    const handleOpenUserMenu = (event) => {
        setAnchorElUser(event.currentTarget);
    };

    const handleCloseNavMenu = () => {
        setAnchorElNav(null);
    };
    // TODO order tracking for customer
    // TODO nginx refresh 404 not found https://stackoverflow.com/questions/56213079/404-error-on-page-refresh-with-angular-7-nginx-and-docker
    const handleLinkClick = (page) => {
        setAnchorElNav(null);
        switch (page) {
            case "boxManagement":
                dispatch(getBoxesAsync())
                break;
            case "userManagement":
                dispatch(getUsersAsync())
                break;
            case "deliveryManagement":
                switch (selectorUser) {
                    case "DISPATCHER":
                        dispatch(getDeliveriesDispatcherAsync())
                        break;
                    case "DELIVERER":
                        dispatch(getDeliveriesDelivererCustomerAsync())
                        break;
                    case "CUSTOMER":
                        dispatch(getDeliveriesDelivererCustomerAsync())
                        break;
                }
                break;
        }
        navigate(`/mainPage/${page}`)
    };

    const handleCloseUserMenu = () => {
        setAnchorElUser(null);
    };

    return (
        <AppBar position="static">
            <Container maxWidth="xl">
                <Toolbar disableGutters>
                    <LocalShippingIcon sx={{display: {xs: 'none', md: 'flex'}, mr: 1}}/>
                    <Typography
                        variant="h6"
                        noWrap
                        component="a"
                        href="/"
                        sx={{
                            mr: 2,
                            display: {xs: 'none', md: 'flex'},
                            fontFamily: 'monospace',
                            fontWeight: 700,
                            letterSpacing: '.3rem',
                            color: 'inherit',
                            textDecoration: 'none',
                        }}
                    >
                        APD
                    </Typography>

                    <Box sx={{flexGrow: 1, display: {xs: 'flex', md: 'none'}}}>
                        <IconButton
                            size="large"
                            aria-label="account of current user"
                            aria-controls="menu-appbar"
                            aria-haspopup="true"
                            onClick={handleOpenNavMenu}
                            color="inherit"
                        >
                            <MenuIcon/>
                        </IconButton>
                        <Menu
                            id="menu-appbar"
                            anchorEl={anchorElNav}
                            anchorOrigin={{
                                vertical: 'bottom',
                                horizontal: 'left',
                            }}
                            keepMounted
                            transformOrigin={{
                                vertical: 'top',
                                horizontal: 'left',
                            }}
                            open={Boolean(anchorElNav)}
                            onClose={handleCloseNavMenu}
                            sx={{
                                display: {xs: 'block', md: 'none'},
                            }}
                        >
                            {pages.map((page) => (
                                <MenuItem key={page} onClick={() => handleLinkClick(page)}>
                                    <Typography textAlign="center">
                                        {page.toUpperCase()}
                                    </Typography>
                                </MenuItem>
                            ))}
                        </Menu>
                    </Box>
                    <Typography
                        variant="h5"
                        noWrap
                        component="a"
                        href=""
                        sx={{
                            mr: 2,
                            display: {xs: 'flex', md: 'none'},
                            flexGrow: 1,
                            fontFamily: 'monospace',
                            fontWeight: 700,
                            letterSpacing: '.3rem',
                            color: 'inherit',
                            textDecoration: 'none',
                        }}
                    >
                        APD
                    </Typography>
                    <Box sx={{flexGrow: 1, display: {xs: 'none', md: 'flex'}}}>
                        {pages.map((page) => (
                            <Button
                                key={page.toUpperCase()}
                                onClick={() => handleLinkClick(page)}
                                sx={{my: 2, color: 'white', display: 'block'}}
                            >
                                {page.toUpperCase()}
                            </Button>
                        ))}
                    </Box>

                    <Box sx={{flexGrow: 0}}>
                        <Tooltip title="Open settings">
                            <IconButton onClick={handleOpenUserMenu} sx={{p: 0}}>
                                <PersonIcon/>
                                <Typography textAlign="center" color={'white'}>
                                    {selectorMail + " "}
                                </Typography>
                            </IconButton>
                        </Tooltip>
                        <Menu
                            sx={{mt: '45px'}}
                            id="menu-appbar"
                            anchorEl={anchorElUser}
                            anchorOrigin={{
                                vertical: 'top',
                                horizontal: 'right',
                            }}
                            keepMounted
                            transformOrigin={{
                                vertical: 'top',
                                horizontal: 'right',
                            }}
                            open={Boolean(anchorElUser)}
                            onClose={handleCloseUserMenu}
                        >

                            <MenuItem key={'Logout'} onClick={() => {
                                console.log("logout")
                                dispatch(resetStateCurrUser());
                                dispatch(resetStateBoxes())
                                dispatch(resetStateDeliveries())
                                dispatch(resetStateUsers())
                                navigate("/")
                            }}>
                                <Typography textAlign="center">
                                    <Link style={{textDecoration: "none", color: "black"}}
                                          to={`/`}>{'Logout'}</Link>
                                </Typography>
                            </MenuItem>

                        </Menu>
                    </Box>
                </Toolbar>
            </Container>
        </AppBar>
    );
}

export default ResponsiveAppBar;