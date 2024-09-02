import { createTheme } from '@mui/material/styles';

const theme = createTheme({
    palette: {
        primary: {
            main: '#1976d2', // Blue color
        },
        secondary: {
            main: '#FF8E53',
        },
        background: {
            default: '#fff', // White background
        },
        text: {
            primary: '#000', // Black text
        },
    },
    shape: {
        borderRadius: 8,
    },
    typography: {
        h4: {
            fontWeight: 700,
        },
    },
    components: {
        MuiCard: {
            styleOverrides: {
                root: {
                    padding: '16px',
                    borderRadius: '16px',
                    boxShadow: '0 0 20px rgba(0,0,0,0.2)',
                },
            },
        },
        MuiButton: {
            styleOverrides: {
                root: {
                    borderRadius: '8px',
                    padding: '12px 24px',
                },
            },
        },
        MuiTextField: {
            styleOverrides: {
                root: {
                    '& .MuiInputBase-root': {
                        color: '#000', // Black text
                    },
                    '& .MuiInputLabel-root': {
                        color: '#555', // Grey label
                    },
                },
            },
        },
    },
});

export default theme;