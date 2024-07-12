import React, {useEffect} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {RootState} from '../store';
import {fetchFaculties} from '../slices/facultySlice';
import {Card, CardContent, Typography, Grid, ListItemText, ListItem, List, Box} from '@mui/material';

const FacultyComponent: React.FC = () => {
    const dispatch = useDispatch();
    const {faculties, status, error} = useSelector((state: RootState) => state.faculties);

    useEffect(() => {
        // @ts-ignore
        dispatch(fetchFaculties());
    }, [dispatch]);

    if (status === 'loading') {
        return <div>Loading...</div>;
    }

    if (status === 'failed') {
        return <div>Error: {error}</div>;
    }

    return (
        <Grid container spacing={2} justifyContent={"center"}>
            {faculties.map((faculty, index) => (
                <Grid item xs={12} sm={6} md={4} lg={3} key={index}>
                    <Card>
                        <CardContent>
                            <Typography variant="h5" component="div">
                                {faculty.facultyName}
                            </Typography>
                            <Typography variant="body2" color="text.secondary">
                                {faculty.description}
                            </Typography>
                            <Box mt={2}>
                                <Typography variant="body1">
                                    Established On: {faculty.establishedOn}
                                </Typography>
                                <Typography variant="body1">
                                    Total Number of Students: {faculty.totalNumberStudents}
                                </Typography>
                                <Typography variant="h6" component="div" mt={2}>
                                    Teachers:
                                </Typography>
                                {faculty.teachers.map((teacher, idx) => (
                                    <Box key={idx} mt={1}>
                                        <Typography variant="body1">
                                            {teacher.teacherName}
                                        </Typography>
                                        <List>
                                            {teacher.subjects.map((subject, subIdx) => (
                                                <ListItem key={subIdx}>
                                                    <ListItemText
                                                        primary={subject.subjectName}
                                                        secondary={subject.subjectDescription}
                                                    />
                                                </ListItem>
                                            ))}
                                        </List>
                                    </Box>
                                ))}
                                <Typography variant="h6" component="div" mt={2}>
                                    Specialties:
                                </Typography>
                                {faculty.specialties.map((specialty, idx) => (
                                    <Box key={idx} mt={2}>
                                        <Typography variant="h6" component="div">
                                            {specialty.specialtyName}
                                        </Typography>
                                        <Typography variant="body2" color="text.secondary">
                                            {specialty.specialtyProgram.description}
                                        </Typography>
                                        <Typography variant="body1">
                                            Employment Rate: {specialty.employmentRate}%
                                        </Typography>
                                        <Typography variant="body1">
                                            Degree
                                            Type: {specialty.specialtyProgram.degreeType.degreeDescription} ({specialty.specialtyProgram.degreeType.degreeType})
                                        </Typography>
                                        <Typography variant="body1">
                                            Accreditation: {specialty.specialtyProgram.accreditationStatus.accreditationType} - {specialty.specialtyProgram.accreditationStatus.accreditationDescription}
                                        </Typography>
                                        <Typography variant="body1">
                                            Program
                                            Duration: {specialty.specialtyProgram.startsOn} to {specialty.specialtyProgram.endsOn}
                                        </Typography>
                                        <Typography variant="body1">
                                            Total Credits Required: {specialty.totalCreditsRequired}
                                        </Typography>
                                        <Typography variant="body1">
                                            Subjects:
                                        </Typography>
                                        <List>
                                            {specialty.subjects.map((subject, subIdx) => (
                                                <ListItem key={subIdx}>
                                                    <ListItemText
                                                        primary={subject.subjectName}
                                                        secondary={subject.subjectDescription}
                                                    />
                                                </ListItem>
                                            ))}
                                        </List>
                                        <Typography variant="body1">
                                            Standardized Test Minimum
                                            Result: {specialty.specialtyRequirement.standardizedTestMinResult}
                                        </Typography>
                                        <Typography variant="body1">
                                            Language Proficiency Test Minimum
                                            Result: {specialty.specialtyRequirement.languageProficiencyTestMinResult}
                                        </Typography>
                                        <Typography variant="body1">
                                            Minimum Grade: {specialty.specialtyRequirement.minGrade}
                                        </Typography>
                                        <Typography variant="body1">
                                            Requirement Details: {specialty.specialtyRequirement.requirementDetails}
                                        </Typography>
                                        <Typography variant="body1">
                                            Personal Statement
                                            Required: {specialty.specialtyRequirement.personalStatementRequired ? 'Yes' : 'No'}
                                        </Typography>
                                        <Typography variant="body1">
                                            Letter of Recommendation
                                            Required: {specialty.specialtyRequirement.letterOfRecommendationRequired ? 'Yes' : 'No'}
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
