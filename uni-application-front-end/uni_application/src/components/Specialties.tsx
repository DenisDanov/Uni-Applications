import React, {useState, useEffect} from 'react';
import {Card, CardContent, Typography, Grid, Box} from '@mui/material';
import {Specialty} from "../types/Specialty";
import {fetchSpecialties} from "../axios/requests";

const Specialties = () => {
    const [specialties, setSpecialties] = useState<Specialty[]>([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                await fetchSpecialties(setSpecialties);
            } catch (err) {
                console.error(err);
            }
        };

        fetchData();
    }, []);

    if (specialties.length === 0) {
        return <div>Loading...</div>;
    }

    return (
        <Grid container spacing={2}>
            {specialties.map((specialty, index) => (
                <Grid item xs={12} sm={6} md={4} key={index}>
                    <Card>
                        <CardContent>
                            <Typography variant="h5" component="div">
                                {specialty.specialtyName}
                            </Typography>
                            <Typography variant="body2" color="text.secondary">
                                {specialty.specialtyProgram.description}
                            </Typography>
                            <Box mt={2}>
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
                                <ul>
                                    {specialty.subjects.map((subject, idx) => (
                                        <li key={idx}>
                                            <Typography variant="body1">
                                                {subject.subjectName}: {subject.subjectDescription}
                                            </Typography>
                                        </li>
                                    ))}
                                </ul>
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
                        </CardContent>
                    </Card>
                </Grid>
            ))}
        </Grid>
    );
};

export default Specialties;
