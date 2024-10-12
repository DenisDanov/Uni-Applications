import React from 'react';
import { Menu, List, ListItem, ListItemIcon, ListItemText, Box } from '@mui/material';
import {
    Home,
    Login,
    Logout,
    AccountCircle,
    LibraryBooks,
    EventNote,
    Assignment,
    Dashboard,
    People,
    FileCopy,
    School, Newspaper
} from '@mui/icons-material';
import { Link as RouterLink } from 'react-router-dom';
import { t } from "i18next";
import { getAuthUrl, redirectToLogin } from '../util/RedirectToLogin';

// @ts-ignore
const NavBarMenu = ({ anchorEl, handleClose, handleLogout, isBgLanguage, keycloak }) => {
    const menuLinks = [
        { to: '/', text: isBgLanguage ? t('menu.home') : 'Home', icon: <Home /> },
        !keycloak.authenticated && {
            to: getAuthUrl(),
            icon: <Login />,
            text: isBgLanguage ? t('menu.login') : 'Login',
            condition: !keycloak.authenticated
        }, ,
        keycloak.authenticated && {
            to: '#',
            text: isBgLanguage ? t('menu.logout') : 'Logout',
            icon: <Logout />,
            action: handleLogout,
        },
        keycloak.authenticated && {
            to: '/profile',
            text: isBgLanguage ? t('menu.profile') : 'Profile',
            icon: <AccountCircle />,
        },
        { to: '/faculties', text: isBgLanguage ? t('menu.faculties') : 'Faculties', icon: <School /> },
        { to: '/specialties', text: isBgLanguage ? t('menu.specialties') : 'Specialties', icon: <LibraryBooks /> },
        (keycloak.authenticated && keycloak.hasRealmRole("student") && {
            to: "/apply",
            text: isBgLanguage ? t('menu.apply') : 'Apply',
            icon: <Assignment />,
        }) ||
        (!keycloak.authenticated && {
            to: getAuthUrl(),
            text: isBgLanguage ? t('menu.apply') : 'Apply',
            icon: <Assignment />,
        }),
        keycloak.authenticated && keycloak.hasRealmRole("student") && {
            to: '/requirements-tests',
            text: isBgLanguage ? t('menu.requirementsTests') : 'Requirements Tests',
            icon: <EventNote />,
        },
        keycloak.authenticated && keycloak.hasRealmRole("admin") && {
            to: '/manage-applications',
            text: isBgLanguage ? t('menu.manageApplications') : 'Manage Applications',
            icon: <Dashboard />,
        },
        keycloak.authenticated && keycloak.hasRealmRole("admin") && {
            to: '/manage-users',
            text: isBgLanguage ? t('menu.manageUsers') : 'Manage Users',
            icon: <People />,
        },
        keycloak.authenticated && keycloak.hasRealmRole("admin") && {
            to: '/admin-dashboard',
            text: isBgLanguage ? t('menu.adminDashboard') : 'Admin Dashboard',
            icon: <FileCopy />,
        },
        {
            to: '/news',
            text: isBgLanguage ? t('menu.news') : 'News',
            icon: <Newspaper />
        }
    ].filter(Boolean);

    return (
        <Menu
            id="menu-appbar"
            anchorEl={anchorEl}
            anchorOrigin={{ vertical: 'top', horizontal: 'left' }}
            keepMounted
            transformOrigin={{ vertical: 'top', horizontal: 'left' }}
            open={Boolean(anchorEl)}
            onClose={handleClose}
        >
            <Box sx={{ width: 250, backgroundColor: '#1976d2' }}>
                <List>
                    {menuLinks.map(({ to, text, icon, action }, index) => (
                        <ListItem
                            button
                            key={index}
                            component={RouterLink}
                            to={text === 'Login' || text === 'Вход' || (!keycloak.authenticated && (text === 'Apply' || text === 'Кандидатствай')) ? '#' : to}
                            onClick={text === 'Login' || text === 'Вход' || (!keycloak.authenticated && (text === 'Apply' || text === 'Кандидатствай')) ? () => redirectToLogin(to) : action ? action : handleClose}
                            sx={{
                                color: '#fff',
                                '&:hover': {
                                    backgroundColor: '#1565c0',
                                },
                                '& .MuiListItemIcon-root': {
                                    marginRight: '8px',
                                    minWidth: 'auto',
                                },
                                '& .MuiListItemText-primary': {
                                    textAlign: 'center',
                                }
                            }}
                        >
                            <ListItemIcon sx={{ color: '#fff', minWidth: 'auto' }}>
                                {icon}
                            </ListItemIcon>
                            <ListItemText
                                primary={text}
                                sx={{
                                    margin: "0",
                                    textAlign: 'center',
                                }}
                            />
                        </ListItem>
                    ))}
                </List>
            </Box>
        </Menu>
    );
};

export default NavBarMenu;
