import React from 'react';
import { AppBar, Toolbar, Box, Breadcrumbs, Link } from '@mui/material';
import NavBar from './NavBar';
import LanguageSwitcher from './LanguageSwitcher';
import { Link as RouterLink, useLocation } from 'react-router-dom';
import { useTranslation } from 'react-i18next';

const Header: React.FC = () => {
    const location = useLocation();
    const pathNames = location.pathname.split('/').filter((x) => x);
    const { t, i18n } = useTranslation();
    const isBgLanguage = i18n.language === 'bg'; // Check if the current language is Bulgarian

    const breadcrumbNameMap: { [key: string]: string } = {
        '': isBgLanguage ? t('breadcrumbs.home') : 'Home',
        'faculties': isBgLanguage ? t('breadcrumbs.faculties') : 'Faculties',
        'specialties': isBgLanguage ? t('breadcrumbs.specialties') : 'Specialties',
        'apply': isBgLanguage ? t('breadcrumbs.apply') : 'Apply',
        'requirements-tests': isBgLanguage ? t('breadcrumbs.requirementsTests') : 'Requirements Tests',
        'manage-applications': isBgLanguage ? t('breadcrumbs.manageApplications') : 'Manage Applications',
        'manage-users': isBgLanguage ? t('breadcrumbs.manageUsers') : 'Manage Users',
        'profile': isBgLanguage ? t('breadcrumbs.profile') : 'Profile',
        'login': isBgLanguage ? t('breadcrumbs.login') : 'Login',
        'evaluate-application': isBgLanguage ? t('breadcrumbs.evaluateApplication') : 'Evaluate Application',
        'admin-dashboard': isBgLanguage ? t('breadcrumbs.adminDashboard') : 'Admin Dashboard',
    };

    return (
        <>
            <AppBar position="static" sx={{ boxShadow: 'none' }}>
                <Toolbar>
                    <Box sx={{ flexGrow: 1, display: 'flex', flexDirection: 'column' }}>
                        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                            <NavBar />
                            <LanguageSwitcher />
                        </Box>
                    </Box>
                </Toolbar>
            </AppBar>
            <Box style={{ marginTop: '10px', marginLeft: '1.1%' }}>
                <Breadcrumbs aria-label="breadcrumb">
                    <Link color="inherit" component={RouterLink} to="/">
                        {breadcrumbNameMap['']}
                    </Link>
                    {pathNames.map((value, index) => {
                        const to = `/${pathNames.slice(0, index + 1).join('/')}`;
                        return (
                            <Link key={to} color="inherit" component={RouterLink} to={to}>
                                {breadcrumbNameMap[value] || value.charAt(0).toUpperCase() + value.slice(1)}
                            </Link>
                        );
                    })}
                </Breadcrumbs>
            </Box>
        </>
    );
};

export default Header;
