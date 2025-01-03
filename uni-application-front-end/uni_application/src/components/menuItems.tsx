import { Navigate, Link as RouterLink } from 'react-router-dom';
import { List, ListItem, ListItemIcon, ListItemText, Box } from '@mui/material';
import {
    Home,
    Login,
    Logout,
    AccountCircle,
    School,
    Dashboard,
    Assignment,
    People,
    FileCopy,
    LibraryBooks,
    EventNote,
    Newspaper
} from '@mui/icons-material';
import { getAuthUrl, redirectToLogin } from '../util/RedirectToLogin';

export const menuItems = (
    handleLogout: any,
    keycloak: any,
    isBgLanguage: any,
    t: any
) => (
    <Box sx={{ display: 'flex', flexDirection: 'row', backgroundColor: '#1976d2', padding: '3px', borderRadius: '8px' }}>
        <List
            sx={{
                display: 'flex',
                flexDirection: 'row',
                alignItems: 'flex-start', // Align items independently
                padding: 0,
            }}
        >
            {[
                { to: "/", icon: <Home />, text: isBgLanguage ? t('menu.home') : 'Home' },
                {
                    to: getAuthUrl(),
                    icon: <Login />,
                    text: isBgLanguage ? t('menu.login') : 'Login',
                    condition: !keycloak.authenticated
                },
                {
                    to: "#",
                    icon: <Logout />,
                    text: isBgLanguage ? t('menu.logout') : 'Logout',
                    condition: keycloak.authenticated,
                    onClick: handleLogout
                },
                {
                    to: "/profile",
                    icon: <AccountCircle />,
                    text: isBgLanguage ? t('menu.profile') : 'Profile',
                    condition: keycloak.authenticated
                },
                { to: "/faculties", icon: <School />, text: isBgLanguage ? t('menu.faculties') : 'Faculties' },
                { to: "/specialties", icon: <LibraryBooks />, text: isBgLanguage ? t('menu.specialties') : 'Specialties' },
                {
                    to: keycloak.authenticated ? "/apply" : getAuthUrl(),
                    icon: <Assignment />,
                    text: isBgLanguage ? t('menu.apply') : 'Apply',
                    condition: (keycloak.authenticated && keycloak.hasRealmRole("student")) || !keycloak.authenticated
                },
                {
                    to: "/requirements-tests",
                    icon: <EventNote />,
                    text: isBgLanguage ? t('menu.requirementsTests') : 'Requirements Tests',
                    condition: keycloak.authenticated && keycloak.hasRealmRole("student")
                },
                {
                    to: "/manage-applications",
                    icon: <Dashboard />,
                    text: isBgLanguage ? t('menu.manageApplications') : 'Manage Applications',
                    condition: keycloak.authenticated && keycloak.hasRealmRole("admin")
                },
                {
                    to: "/manage-users",
                    icon: <People />,
                    text: isBgLanguage ? t('menu.manageUsers') : 'Manage Users',
                    condition: keycloak.authenticated && keycloak.hasRealmRole("admin")
                },
                {
                    to: "/admin-dashboard",
                    icon: <FileCopy />,
                    text: isBgLanguage ? t('menu.adminDashboard') : 'Admin Dashboard',
                    condition: keycloak.authenticated && keycloak.hasRealmRole("admin")
                },
                {
                    to: '/news',
                    text: isBgLanguage ? t('menu.news') : 'News',
                    icon: <Newspaper />
                }
            ].map((item, index) => (
                item.condition !== false && (
                    <ListItem
                        key={index}
                        // @ts-ignore
                        button
                        component={RouterLink}
                        to={item.text === 'Login' || item.text === 'Вход' || (!keycloak.authenticated && (item.text === 'Apply' || item.text === 'Кандидатствай')) ? '#' : item.to}
                        onClick={item.text === 'Login' || item.text === 'Вход' || (!keycloak.authenticated && (item.text === 'Apply' || item.text === 'Кандидатствай')) ? () => redirectToLogin(item.to) : item.onClick}
                        sx={{
                            flexDirection: 'column',
                            alignItems: 'center',
                            color: '#fff',
                            margin: '0 5px',
                            padding: '10px',
                            '&:hover': { backgroundColor: '#1565c0', borderRadius: '8px' },
                            textAlign: 'center',
                            height: 'auto',  // Allow each item to take only the space it needs
                            width: 'min-content',
                            display: 'table-caption',
                        }}
                    >
                        <ListItemIcon sx={{ color: '#fff', minWidth: 'auto', marginBottom: "4px" }}>
                            {item.icon}
                        </ListItemIcon>
                        <ListItemText
                            primary={item.text}
                            sx={{
                                width: 'auto',
                                marginTop: '4px',
                                marginBottom: 0,
                                textAlign: 'center',
                                whiteSpace: 'normal',  // Allow text to wrap
                                wordWrap: 'break-word', // Break long words if necessary
                                '& span': { // Target the span inside the ListItemText
                                    lineHeight: '1 !important',  // Override the line-height
                                },
                            }}
                        />
                    </ListItem>
                )
            ))}
        </List>
    </Box>
);
