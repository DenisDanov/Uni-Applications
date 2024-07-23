import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { RootState } from '../store';
import { fetchFaculties } from '../slices/facultySlice';
import { Card, CardContent, Typography, Grid, ListItemText, ListItem, List, Box } from '@mui/material';
import { useTranslation } from 'react-i18next';

const FacultyComponent: React.FC = () => {
    const dispatch = useDispatch();
    const { faculties, status, error } = useSelector((state: RootState) => state.faculties);
    const { t, i18n } = useTranslation();

    useEffect(() => {
        // @ts-ignore
        dispatch(fetchFaculties());
    }, [dispatch]);

    const isBgLanguage = i18n.language === 'bg'; // Check if the current language is Bulgarian

    // Helper function to get translated or raw data
    const getTranslatedText = (key: string, fallback: string) => {
        return isBgLanguage ? t(key) : fallback;
    };

    if (status === 'loading') {
        return <div>{getTranslatedText('loading', 'Loading...')}</div>;
    }

    if (status === 'failed') {
        return <div>{getTranslatedText('error', 'Error')}: {error}</div>;
    }

    return (
        <Grid container spacing={2} justifyContent={"center"}>
            {faculties.map((faculty, index) => (
                <Grid item xs={12} sm={6} md={4} lg={3} key={index}>
                    <Card>
                        <CardContent>
                            <Typography variant="h5" component="div">
                                {getTranslatedText(`faculty.${faculty.facultyName}.title`, faculty.facultyName)}
                            </Typography>
                            <Typography variant="body2" color="text.secondary">
                                {getTranslatedText(`faculty.${faculty.facultyName}.description`, faculty.description)}
                            </Typography>
                            <Box mt={2}>
                                <Typography variant="body1">
                                    {getTranslatedText(`faculty.${faculty.facultyName}.establishedOn`, `Established On: ${faculty.establishedOn}`)}
                                </Typography>
                                <Typography variant="body1">
                                    {getTranslatedText(`faculty.${faculty.facultyName}.totalNumberStudents`, `Total Number of Students: ${faculty.totalNumberStudents}`)}
                                </Typography>
                                <Typography variant="h6" component="div" mt={2}>
                                    {getTranslatedText('faculty.teachersHeader', 'Teachers')}:
                                </Typography>
                                {faculty.teachers.map((teacher, idx) => (
                                    <Box key={idx} mt={1}>
                                        <Typography variant="body1">
                                            {getTranslatedText(`faculty.teachers.${teacher.teacherName}`, teacher.teacherName)}
                                        </Typography>
                                        <List>
                                            {teacher.subjects.map((subject, subIdx) => (
                                                <ListItem key={subIdx}>
                                                    <ListItemText
                                                        primary={getTranslatedText(`faculty.${faculty.facultyName}.subjects.${subject.subjectName}`, subject.subjectName)}
                                                        secondary={getTranslatedText(`faculty.${faculty.facultyName}.subjects.${subject.subjectDescription}`, subject.subjectDescription)}
                                                    />
                                                </ListItem>
                                            ))}
                                        </List>
                                    </Box>
                                ))}
                                <Typography variant="h6" component="div" mt={2}>
                                    {getTranslatedText('faculty.specialties', 'Specialties')}:
                                </Typography>
                                {faculty.specialties.map((specialty, idx) => (
                                    <Box key={idx} mt={2}>
                                        <Typography variant="h6" component="div">
                                            {getTranslatedText(`faculty.${faculty.facultyName}.specialties.${specialty.specialtyName}`, specialty.specialtyName)}
                                        </Typography>
                                        <Typography variant="body2" color="text.secondary">
                                            {getTranslatedText(`faculty.${faculty.facultyName}.specialties.${specialty.specialtyProgram.description}`, specialty.specialtyProgram.description)}
                                        </Typography>
                                        <Typography variant="body1">
                                            {getTranslatedText('faculty.employmentRate', 'Employment Rate')}: {specialty.employmentRate}%
                                        </Typography>
                                        <Typography variant="body1">
                                            {getTranslatedText(`faculty.${faculty.facultyName}.degreeType`, `Degree type: ${specialty.specialtyProgram.degreeType.degreeType} (${specialty.specialtyProgram.degreeType.degreeDescription})`)}
                                        </Typography>
                                        <Typography variant="body1">
                                            {getTranslatedText(`faculty.${faculty.facultyName}.accreditation`, `Accreditation: ${specialty.specialtyProgram.accreditationStatus.accreditationType} (${specialty.specialtyProgram.accreditationStatus.accreditationDescription})`)}
                                        </Typography>
                                        <Typography variant="body1">
                                            {getTranslatedText(`faculty.${faculty.facultyName}.programDuration`, `Program Duration: ${specialty.specialtyProgram.startsOn} to ${specialty.specialtyProgram.endsOn}`)}
                                        </Typography>
                                        <Typography variant="body1">
                                            {getTranslatedText('faculty.totalCreditsRequired', 'Total Credits Required')}: {specialty.totalCreditsRequired}
                                        </Typography>
                                        <Typography variant="body1">
                                            {getTranslatedText(`faculty.subjectsHeader`, 'Subjects')}:
                                        </Typography>
                                        <List>
                                            {specialty.subjects.map((subject, subIdx) => (
                                                <ListItem key={subIdx}>
                                                    <ListItemText
                                                        primary={getTranslatedText(`faculty.${faculty.facultyName}.subjects.${subject.subjectName}`, subject.subjectName)}
                                                        secondary={getTranslatedText(`faculty.${faculty.facultyName}.subjects.${subject.subjectDescription}`, subject.subjectDescription)}
                                                    />
                                                </ListItem>
                                            ))}
                                        </List>
                                        <Typography variant="body1">
                                            {getTranslatedText('faculty.standardizedTestMinResult', 'Standardized Test Minimum Result')}: {specialty.specialtyRequirement.standardizedTestMinResult}
                                        </Typography>
                                        <Typography variant="body1">
                                            {getTranslatedText('faculty.languageProficiencyTestMinResult', 'Language Proficiency Test Minimum Result')}: {specialty.specialtyRequirement.languageProficiencyTestMinResult || 'N/A'}
                                        </Typography>
                                        <Typography variant="body1">
                                            {getTranslatedText('faculty.minimumGrade', 'Minimum Grade')}: {specialty.specialtyRequirement.minGrade}
                                        </Typography>
                                        <Typography variant="body1">
                                            {getTranslatedText(`faculty.${faculty.facultyName}.requirementDetails`, `Requirement Details: ${specialty.specialtyRequirement.requirementDetails}`)}
                                        </Typography>
                                        <Typography variant="body1">
                                            {getTranslatedText(`faculty.${faculty.facultyName}.personalStatementRequired`, `Personal Statement Required: ${specialty.specialtyRequirement.personalStatementRequired ? 'Yes' : 'No'}`)}
                                        </Typography>
                                        <Typography variant="body1">
                                            {getTranslatedText(`faculty.${faculty.facultyName}.letterOfRecommendationRequired`, `Letter of Recommendation Required: ${specialty.specialtyRequirement.letterOfRecommendationRequired ? 'Yes' : 'No'}`)}
                                        </Typography>
                                    </Box>
                                ))}
                            </Box>
                        </CardContent>
                    </Card>
                </Grid>
            ))}
        </Grid>
    );
};

export default FacultyComponent;
