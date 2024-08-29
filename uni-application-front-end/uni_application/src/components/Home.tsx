import React from 'react';
import {Typography, Container, Box, Button} from '@mui/material';
import {Link as RouterLink} from 'react-router-dom';
import {styled} from '@mui/system';
import {useTranslation} from "react-i18next";

const StyledButton = styled(Button)({
    margin: '10px',
    padding: '10px 20px',
    fontSize: '1.2rem',
    borderRadius: '50px',
    boxShadow: '0px 4px 10px rgba(0, 0, 0, 0.3)',
    '&:hover': {
        transform: 'scale(1.05)',
        transition: 'all 0.3s ease-in-out',
    },
});

const Home: React.FC = () => {
    const { t } = useTranslation();
    const language = localStorage.getItem('language') ? localStorage.getItem('language') : 'en';
    const isBgLanguage = language === 'bg'; // Check if the current language is Bulgarian

    return (
        <Container
            maxWidth="lg"
            sx={{
                zIndex: 2,
                height: '100%',
                display: 'flex',
                flexDirection: 'column',
                justifyContent: 'center',
                alignItems: 'center',
                minHeight: '100vh'
            }}
        >
            <Box textAlign="center">
                <Typography variant="h3" component="h1" gutterBottom>
                    {isBgLanguage ? t('home.welcomeTitle') : 'Welcome to the University Application Portal'}
                </Typography>
                <Typography variant="h5" component="h2" gutterBottom>
                    {isBgLanguage ? t('home.subTitle') : 'Your gateway to a bright future starts here'}
                </Typography>
                <Typography variant="body1" paragraph>
                    {isBgLanguage ? t('home.description') : 'Explore various faculties and specialties that our university has to offer. Create an account to start your application process.'}
                </Typography>
                <Box my={4}>
                    <StyledButton
                        variant="contained"
                        color="primary"
                        // @ts-ignore
                        component={RouterLink} to="/faculties"
                    >
                        {isBgLanguage ? t('home.exploreFaculties') : 'Explore Faculties'}
                    </StyledButton>
                    <StyledButton
                        variant="contained"
                        color="secondary"
                        // @ts-ignore
                        component={RouterLink} to="/specialties"
                    >
                        {isBgLanguage ? t('home.discoverSpecialties') : 'Discover Specialties'}
                    </StyledButton>
                    <StyledButton
                        variant="outlined"
                        color="inherit"
                        // @ts-ignore
                        component={RouterLink} to="/apply"
                    >
                        {isBgLanguage ? t('home.startApplication') : 'Start Your Application'}
                    </StyledButton>
                </Box>
            </Box>
        </Container>
    );
};

export default Home;
