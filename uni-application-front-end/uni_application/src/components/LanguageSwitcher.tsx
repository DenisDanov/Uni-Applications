// src/components/LanguageSwitcher.tsx
import React from 'react';
import { useTranslation } from 'react-i18next';
import { Menu, MenuItem, IconButton, Tooltip } from '@mui/material';
import LanguageIcon from '@mui/icons-material/Language';

const LanguageSwitcher: React.FC = () => {
    const { i18n } = useTranslation();
    const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);

    const handleClick = (event: React.MouseEvent<HTMLElement>) => {
        setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
        setAnchorEl(null);
    };

    const handleLanguageChange = (lang: string) => {
        localStorage.setItem('language', lang); // Save language to localStorage
        i18n.changeLanguage(lang);
        handleClose();
    };

    return (
        <div>
            <Tooltip title="Change Language">
                <IconButton onClick={handleClick} color="inherit">
                    <LanguageIcon />
                </IconButton>
            </Tooltip>
            <Menu
                anchorEl={anchorEl}
                open={Boolean(anchorEl)}
                onClose={handleClose}
                PaperProps={{
                    style: {
                        maxHeight: 48 * 4.5,
                        width: '10ch',
                    },
                }}
            >
                <MenuItem onClick={() => handleLanguageChange('bg')}>BG</MenuItem>
                <MenuItem onClick={() => handleLanguageChange('en')}>EN</MenuItem>
            </Menu>
        </div>
    );
};

export default LanguageSwitcher;
