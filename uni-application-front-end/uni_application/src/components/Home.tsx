import React from 'react';
import {Typography, Container, Box, Button} from '@mui/material';
import {Link as RouterLink} from 'react-router-dom';

const Home: React.FC = () => {
    return (
        <Container>
            <Box my={4} textAlign="center">
                <Typography variant="h3" component="h1" gutterBottom>
                    Welcome to the University Application Portal
                </Typography>
                <Typography variant="h5" component="h2" gutterBottom>
                    Your gateway to a bright future starts here
                </Typography>
                <Typography variant="body1" paragraph>
                    Explore various faculties and specialties that our university has to offer.
                    Create an account to start your application process.
                </Typography>
                <Box my={2}>
                    <Button variant="contained" color="primary" component={RouterLink} to="/faculties">
                        Explore Faculties
                    </Button>
                </Box>
                <Box my={2}>
                    <Button variant="contained" color="secondary" component={RouterLink} to="/specialties">
                        Discover Specialties
                    </Button>
                </Box>
                <Box my={2}>
                    <Button variant="outlined" component={RouterLink} to="/apply">
                        Start Your Application
                    </Button>
                </Box>
            </Box>
        </Container>
    );
};

export default Home;
