import React, {useState} from 'react';
import {AppBar, Toolbar, IconButton, Typography, Menu, MenuItem, Breadcrumbs, Link, Box} from '@mui/material';
import MenuIcon from '@mui/icons-material/Menu';
import {Link as RouterLink, useLocation} from 'react-router-dom';
import {useKeycloak} from '../keycloak';

const NavBar: React.FC = () => {
    const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
    const {initialized, keycloak} = useKeycloak();

    const handleMenu = (event: React.MouseEvent<HTMLElement>) => {
        setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
        setAnchorEl(null);
    };

    const handleLogout = () => {
        keycloak.logout();
        handleClose();
    };

    const location = useLocation();
    const pathNames = location.pathname.split('/').filter((x) => x);

    return (
        <div style={{margin: "8px"}}>
            <AppBar position="static">
                <Toolbar>
                    <IconButton edge="start" color="inherit" aria-label="menu" onClick={handleMenu}>
                        <MenuIcon/>
                    </IconButton>
                    <Menu
                        id="menu-appbar"
                        anchorEl={anchorEl}
                        anchorOrigin={{vertical: 'top', horizontal: 'left'}}
                        keepMounted
                        transformOrigin={{vertical: 'top', horizontal: 'left'}}
                        open={Boolean(anchorEl)}
                        onClose={handleClose}
                    >
                        <MenuItem onClick={handleClose} component={RouterLink} to="/">
                            Home
                        </MenuItem>
                        {!keycloak.authenticated && (
                            <MenuItem onClick={handleClose} component={RouterLink} to="/login">
                                Log in
                            </MenuItem>
                        )}
                        {keycloak.authenticated && (
                            <MenuItem onClick={handleLogout}>
                                Log out
                            </MenuItem>
                        )}
                        {keycloak.authenticated && (
                            <MenuItem onClick={handleClose} component={RouterLink} to="/profile">
                                Profile
                            </MenuItem>
                        )}
                        <MenuItem onClick={handleClose} component={RouterLink} to="/faculties">
                            Faculties
                        </MenuItem>
                        <MenuItem onClick={handleClose} component={RouterLink} to="/specialties">
                            Specialties
                        </MenuItem>
                        {(keycloak.authenticated && keycloak.hasRealmRole("student") &&
                            <MenuItem onClick={handleClose} component={RouterLink} to="/apply">
                                Apply
                            </MenuItem>) || (!keycloak.authenticated &&
                            <MenuItem onClick={handleClose} component={RouterLink} to="/apply">
                                Apply
                            </MenuItem>)
                        }
                        {keycloak.authenticated && keycloak.hasRealmRole("admin") &&
                            <MenuItem onClick={handleClose} component={RouterLink} to="/manage-applications">
                                Manage Applications
                            </MenuItem>
                        }
                        {keycloak.authenticated && keycloak.hasRealmRole("admin") &&
                            <MenuItem onClick={handleClose} component={RouterLink} to="/manage-users">
                                Manage Users
                            </MenuItem>
                        }
                    </Menu>
                    <Typography variant="h6" sx={{flexGrow: 1}}>
                        Menu
                    </Typography>
                </Toolbar>
            </AppBar>
            <Box m={2}>
                <Breadcrumbs aria-label="breadcrumb">
                    <Link color="inherit" component={RouterLink} to="/">
                        Home
                    </Link>
                    {pathNames.map((value, index) => {
                        const to = `/${pathNames.slice(0, index + 1).join('/')}`;
                        return (
                            <Link key={to} color="inherit" component={RouterLink} to={to}>
                                {value.charAt(0).toUpperCase() + value.slice(1)}
                            </Link>
                        );
                    })}
                </Breadcrumbs>
            </Box>
        </div>
    );
};

export default NavBar;
