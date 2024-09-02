import React from 'react';
import {Box, Container, Typography} from '@mui/material';

const Footer = () => {
    return (
        <Box
            component="footer"
            sx={{
                backgroundColor: '#1976d2',
                textAlign: 'center',
                mt: 'auto',
                py: 2,
                paddingTop: '15px'
            }}
        >
            <Container>
                <Typography variant="body2" color={"white"}>
                    © Uni Applications 2024
                </Typography>
            </Container>
        </Box>
    );
};

export default Footer;
