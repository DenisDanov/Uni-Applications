import React, { useState } from 'react';
import { Toolbar, IconButton, Typography, Menu, MenuItem, Box } from '@mui/material';
import MenuIcon from '@mui/icons-material/Menu';
import { Link as RouterLink } from 'react-router-dom';
import { useKeycloak } from '../keycloak';
import { useTranslation } from 'react-i18next';

const NavBar: React.FC = () => {
    const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
    const { keycloak } = useKeycloak();
    const { t, i18n } = useTranslation();
    const isBgLanguage = i18n.language === 'bg'; // Check if the current language is Bulgarian

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

    return (
        <div style={{ margin: "8px" }}>
            <Toolbar>
                <Box
                    sx={{ display: 'flex', flexDirection: 'column', cursor: 'pointer' }}
                    onClick={handleMenu}
                >
                    <IconButton style={{ padding: "0" }} color="inherit" aria-label="menu">
                        <MenuIcon />
                    </IconButton>
                    <Typography variant="h6">
                        {isBgLanguage ? t('menu.title') : 'Menu'}
                    </Typography>
                </Box>
                <Menu
                    id="menu-appbar"
                    anchorEl={anchorEl}
                    anchorOrigin={{ vertical: 'top', horizontal: 'left' }}
                    keepMounted
                    transformOrigin={{ vertical: 'top', horizontal: 'left' }}
                    open={Boolean(anchorEl)}
                    onClose={handleClose}
                >
                    <MenuItem onClick={handleClose} component={RouterLink} to="/">
                        {isBgLanguage ? t('menu.home') : 'Home'}
                    </MenuItem>
                    {!keycloak.authenticated && (
                        <MenuItem onClick={handleClose} component={RouterLink} to="/login">
                            {isBgLanguage ? t('menu.login') : 'Login'}
                        </MenuItem>
                    )}
                    {keycloak.authenticated && (
                        <MenuItem onClick={handleLogout}>
                            {isBgLanguage ? t('menu.logout') : 'Logout'}
                        </MenuItem>
                    )}
                    {keycloak.authenticated && (
                        <MenuItem onClick={handleClose} component={RouterLink} to="/profile">
                            {isBgLanguage ? t('menu.profile') : 'Profile'}
                        </MenuItem>
                    )}
                    <MenuItem onClick={handleClose} component={RouterLink} to="/faculties">
                        {isBgLanguage ? t('menu.faculties') : 'Faculties'}
                    </MenuItem>
                    <MenuItem onClick={handleClose} component={RouterLink} to="/specialties">
                        {isBgLanguage ? t('menu.specialties') : 'Specialties'}
                    </MenuItem>
                    {(keycloak.authenticated && keycloak.hasRealmRole("student") &&
                        <MenuItem onClick={handleClose} component={RouterLink} to="/apply">
                            {isBgLanguage ? t('menu.apply') : 'Apply'}
                        </MenuItem>) || (!keycloak.authenticated &&
                        <MenuItem onClick={handleClose} component={RouterLink} to="/apply">
                            {isBgLanguage ? t('menu.apply') : 'Apply'}
                        </MenuItem>)
                    }
                    {keycloak.authenticated && keycloak.hasRealmRole("student") &&
                        <MenuItem onClick={handleClose} component={RouterLink} to="/requirements-tests">
                            {isBgLanguage ? t('menu.requirementsTests') : 'Requirements Tests'}
                        </MenuItem>
                    }
                    {keycloak.authenticated && keycloak.hasRealmRole("admin") &&
                        <MenuItem onClick={handleClose} component={RouterLink} to="/manage-applications">
                            {isBgLanguage ? t('menu.manageApplications') : 'Manage Applications'}
                        </MenuItem>
                    }
                    {keycloak.authenticated && keycloak.hasRealmRole("admin") &&
                        <MenuItem onClick={handleClose} component={RouterLink} to="/manage-users">
                            {isBgLanguage ? t('menu.manageUsers') : 'Manage Users'}
                        </MenuItem>
                    }
                </Menu>
            </Toolbar>
        </div>
    );
};

export default NavBar;
