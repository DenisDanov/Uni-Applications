import React from 'react';
import { Typography, Container, Box, Button } from '@mui/material';
import { Link as RouterLink } from 'react-router-dom';
import { useTranslation } from 'react-i18next';

const Home: React.FC = () => {
    const { t, i18n } = useTranslation();
    const isBgLanguage = i18n.language === 'bg'; // Check if the current language is Bulgarian

    return (
        <Container>
            <Box my={4} textAlign="center">
                <Typography variant="h3" component="h1" gutterBottom>
                    {isBgLanguage ? t('home.welcomeTitle') : 'Welcome to the University Application Portal'}
                </Typography>
                <Typography variant="h5" component="h2" gutterBottom>
                    {isBgLanguage ? t('home.subTitle') : 'Your gateway to a bright future starts here'}
                </Typography>
                <Typography variant="body1" paragraph>
                    {isBgLanguage ? t('home.description') : 'Explore various faculties and specialties that our university has to offer. Create an account to start your application process.'}
                </Typography>
                <Box my={2}>
                    <Button variant="contained" color="primary" component={RouterLink} to="/faculties">
                        {isBgLanguage ? t('home.exploreFaculties') : 'Explore Faculties'}
                    </Button>
                </Box>
                <Box my={2}>
                    <Button variant="contained" color="secondary" component={RouterLink} to="/specialties">
                        {isBgLanguage ? t('home.discoverSpecialties') : 'Discover Specialties'}
                    </Button>
                </Box>
                <Box my={2}>
                    <Button variant="outlined" component={RouterLink} to="/apply">
                        {isBgLanguage ? t('home.startApplication') : 'Start Your Application'}
                    </Button>
                </Box>
            </Box>
        </Container>
    );
};

export default Home;
