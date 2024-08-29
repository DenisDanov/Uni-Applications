import React, {useEffect} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {RootState} from '../store';
import {fetchFaculties} from '../slices/facultySlice'; // Updated action import
import {Card, CardContent, Typography, Grid, Box, Button, Skeleton, Divider} from '@mui/material';
import {useNavigate} from 'react-router-dom';
import {useTranslation} from "react-i18next";
import {CalendarToday, Group} from "@mui/icons-material"; // Import useNavigate from react-router-dom

const FacultyComponent: React.FC = () => {
    const dispatch = useDispatch();
    const navigate = useNavigate(); // Initialize navigate
    const {faculties, status, error} = useSelector((state: RootState) => state.faculties);
    const {t, i18n} = useTranslation();
    const isBgLanguage = i18n.language === 'bg';

    const getTranslatedText = (key: string, fallback: string) => {
        return isBgLanguage ? t(key) : fallback;
    };

    useEffect(() => {
        // @ts-ignore
        dispatch(fetchFaculties());
    }, [dispatch]);

    if (status === 'loading') {
        return (
            <Grid container spacing={3} justifyContent="center">
                {Array.from(new Array(6)).map((_, index) => (
                    <Grid item xs={12} sm={6} md={4} lg={3} key={index}>
                        <Card sx={{borderRadius: '16px', boxShadow: 3}}>
                            <CardContent>
                                <Skeleton variant="text" width="60%" sx={{fontSize: '1.5rem', mb: 2}}/>
                                <Skeleton variant="text" width="100%" sx={{fontSize: '1rem', mb: 2}}/>
                                <Divider sx={{mb: 2, borderBottomWidth: 2}}>
                                    <Skeleton variant="rectangular" width={140} height={30}/>
                                </Divider>
                                <Skeleton variant="text" width="50%" sx={{mb: 1}}/>
                                <Skeleton variant="text" width="70%" sx={{mb: 2}}/>
                                <Skeleton variant="rectangular" height={36} width="100%"/>
                            </CardContent>
                        </Card>
                    </Grid>
                ))}
            </Grid>
        );
    }

    if (status === 'failed') {
        return <div>Error: {error}</div>;
    }

    return (
        <Grid container spacing={3} justifyContent="center">
            {faculties.map((faculty, index) => (
                <Grid item xs={12} sm={6} md={4} lg={3} key={index}>
                    <Card
                        sx={{
                            boxShadow: 3,
                            borderRadius: '16px',
                            overflow: 'hidden',
                            transition: 'transform 0.3s ease-in-out',
                            '&:hover': {
                                transform: 'scale(1.05)',
                                boxShadow: 5
                            }
                        }}
                    >
                        <CardContent>
                            <Typography variant="h5" component="div" sx={{fontWeight: 'bold', color: 'primary.main'}}>
                                {getTranslatedText(`faculty.${faculty.facultyName}.title`, faculty.facultyName)}
                            </Typography>

                            <Typography variant="body2" color="text.secondary" sx={{marginBottom: '16px'}}>
                                {getTranslatedText(`faculty.${faculty.facultyName}.description`, faculty.description)}
                            </Typography>

                            <Divider sx={{marginBottom: '16px'}}/>

                            <Box sx={{display: 'flex', alignItems: 'center', marginBottom: '8px'}}>
                                <CalendarToday sx={{color: 'primary.main', marginRight: '8px'}}/>
                                <Typography variant="body1">
                                    {getTranslatedText(`faculty.${faculty.facultyName}.establishedOn`, 'Established On:')}
                                    {' ' + faculty.establishedOn}
                                </Typography>
                            </Box>

                            <Box sx={{display: 'flex', alignItems: 'center', marginBottom: '16px'}}>
                                <Group sx={{color: 'primary.main', marginRight: '8px'}}/>
                                <Typography variant="body1">
                                    {getTranslatedText(`faculty.${faculty.facultyName}.totalNumberStudents`, 'Total Number of Students:')}
                                    {' ' + faculty.totalNumberStudents}
                                </Typography>
                            </Box>

                            <Typography variant="h6" component="div" sx={{fontWeight: 'bold', marginBottom: '8px'}}>
                                {getTranslatedText(`faculty.specialties`, 'Specialties:')}
                            </Typography>

                            {faculty.specialties.map((specialty, idx) => (
                                <Box key={idx} mt={1} width={'max-content'}>
                                    <Typography
                                        variant="body1"
                                        className="specialty-link"
                                        sx={{
                                            cursor: 'pointer',
                                            color: 'primary.main',
                                            textDecoration: 'underline',
                                            '&:hover': {
                                                textDecoration: 'none',
                                            }
                                        }}
                                        onClick={() => navigate(`/specialties?specialtyName=${encodeURIComponent(specialty.specialtyName)}`)}
                                    >
                                        {getTranslatedText(`specialties.${specialty.specialtyName}.title`, specialty.specialtyName)}
                                    </Typography>
                                </Box>
                            ))}

                            <Box mt={3} textAlign="center">
                                <Button
                                    variant="contained"
                                    color="primary"
                                    sx={{borderRadius: '999px', padding: '8px 24px', textTransform: 'none'}}
                                    onClick={() => navigate(`/specialties?facultyId=${encodeURIComponent(faculty.id)}`)}
                                >
                                    {getTranslatedText(`seeAllSpecialties`, 'See all Specialties')}
                                </Button>
                            </Box>
                        </CardContent>
                    </Card>
                </Grid>
            ))}
        </Grid>
    );
};

export default FacultyComponent;
