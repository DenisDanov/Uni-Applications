import React, {useState, useEffect, useRef} from 'react';
import {Toolbar, IconButton, Typography, Box} from '@mui/material';
import MenuIcon from '@mui/icons-material/Menu';
import {useKeycloak} from '../keycloak';
import {useTranslation} from 'react-i18next';
import NavBarMenu from "./NavBarMenu";
import {menuItems} from "./menuItems";

const NavBar: React.FC = () => {
    const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
    const [displayInline, setDisplayInline] = useState(true);
    const [isMobileMenu, setIsMobileMenu] = useState(false);
    const navBarRef = useRef<HTMLDivElement>(null);
    const menuItemsRef = useRef<HTMLDivElement>(null);
    const {keycloak} = useKeycloak();
    const {t, i18n} = useTranslation();
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

    // Adjust the menu display based on the width
    const adjustMenuDisplay = () => {
        if (window.innerWidth <= 1050 || (window.innerWidth <= 1150 && isBgLanguage)) {
            setIsMobileMenu(true);
        } else {
            setIsMobileMenu(false);
            if (navBarRef.current && menuItemsRef.current) {
                const navbarWidth = navBarRef.current.offsetWidth;
                const menuItemsWidth = menuItemsRef.current.scrollWidth;

                setDisplayInline(menuItemsWidth <= navbarWidth);
            }
        }
    };

    useEffect(() => {
        adjustMenuDisplay();
        window.addEventListener('resize', adjustMenuDisplay);
        return () => {
            window.removeEventListener('resize', adjustMenuDisplay);
        };
    }, []);

    return (
        <div style={{margin: '5px'}} ref={navBarRef}>
            <Toolbar>
                <Box sx={{display: window.innerWidth <= 1050 || (window.innerWidth <= 1150 && isBgLanguage) ? 'none' : "flex" , flexDirection: 'row'}} ref={menuItemsRef}>
                    {menuItems(handleLogout, keycloak, isBgLanguage, t)}
                </Box>
                <Box
                    sx={{display: window.innerWidth <= 1050 || (window.innerWidth <= 1150 && isBgLanguage) ? 'flex' : "none" , flexDirection: 'column', cursor: 'pointer'}}
                    onClick={handleMenu}
                >
                    <IconButton style={{padding: '0'}} color="inherit" aria-label="menu">
                        <MenuIcon/>
                    </IconButton>
                    <Typography variant="h6">
                        {isBgLanguage ? t('menu.title') : 'Menu'}
                    </Typography>
                </Box>
                <NavBarMenu
                    anchorEl={anchorEl}
                    handleClose={handleClose}
                    handleLogout={handleLogout}
                    isBgLanguage={isBgLanguage}
                    keycloak={keycloak}
                />
            </Toolbar>
        </div>
    );
};

export default NavBar;
