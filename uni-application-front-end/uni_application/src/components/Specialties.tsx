import React, { useState, useEffect } from 'react';
import { Card, CardContent, Typography, Grid, Box } from '@mui/material';
import { Specialty } from "../types/Specialty";
import { fetchSpecialties } from "../axios/requests";
import { useTranslation } from 'react-i18next';

const Specialties = () => {
    const [specialties, setSpecialties] = useState<Specialty[]>([]);
    const { t, i18n } = useTranslation();

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

    const isBgLanguage = i18n.language === 'bg'; // Check if the current language is Bulgarian

    // Helper function to get translated or raw data
    const getTranslatedText = (key: string, fallback: string) => {
        return isBgLanguage ? t(key) : fallback;
    };

    if (specialties.length === 0) {
        return <div>{getTranslatedText('loading', 'Loading...')}</div>;
    }

    return (
        <Grid container spacing={2}>
            {specialties.map((specialty, index) => (
                <Grid item xs={12} sm={6} md={4} key={index}>
                    <Card>
                        <CardContent>
                            <Typography variant="h5" component="div">
                                {getTranslatedText(`specialties.${specialty.specialtyName}.title`, specialty.specialtyName)}
                            </Typography>
                            <Typography variant="body2" color="text.secondary">
                                {getTranslatedText(`specialties.${specialty.specialtyName}.description`, specialty.specialtyProgram.description)}
                            </Typography>
                            <Box mt={2}>
                                <Typography variant="body1">
                                    {getTranslatedText('specialties.employmentRate', 'Employment Rate')}: {specialty.employmentRate}%
                                </Typography>
                                <Typography variant="body1">
                                    {getTranslatedText('specialties.degreeType', 'Degree Type')}: {getTranslatedText(`specialties.${specialty.specialtyName}.degreeType`, specialty.specialtyProgram.degreeType.degreeType)}
                                </Typography>
                                <Typography variant="body1">
                                    {getTranslatedText('specialties.accreditation', 'Accreditation')}: {getTranslatedText(`specialties.${specialty.specialtyName}.accreditation`, `${specialty.specialtyProgram.accreditationStatus.accreditationType} - ${specialty.specialtyProgram.accreditationStatus.accreditationDescription}`)}
                                </Typography>
                                <Typography variant="body1">
                                    {getTranslatedText('specialties.programDuration', 'Program Duration')}: {specialty.specialtyProgram.startsOn} to {specialty.specialtyProgram.endsOn}
                                </Typography>
                                <Typography variant="body1">
                                    {getTranslatedText('specialties.totalCreditsRequired', 'Total Credits Required')}: {specialty.totalCreditsRequired}
                                </Typography>
                                <Typography variant="body1">
                                    {getTranslatedText('specialties.subjectsHeader', 'Subjects')}:
                                </Typography>
                                <ul>
                                    {specialty.subjects.map((subject, idx) => (
                                        <li key={idx}>
                                            <Typography variant="body1">
                                                {getTranslatedText(`specialties.${specialty.specialtyName}.subjects.${subject.subjectName}`, subject.subjectName)}: {getTranslatedText(`specialties.${specialty.specialtyName}.subjects.${subject.subjectDescription}`, subject.subjectDescription)}
                                            </Typography>
                                        </li>
                                    ))}
                                </ul>
                                <Typography variant="body1">
                                    {getTranslatedText('specialties.standardizedTestMinResult', 'Standardized Test Minimum Result')}: {specialty.specialtyRequirement.standardizedTestMinResult}
                                </Typography>
                                <Typography variant="body1">
                                    {getTranslatedText('specialties.languageProficiencyTestMinResult', 'Language Proficiency Test Minimum Result')}: {specialty.specialtyRequirement.languageProficiencyTestMinResult || 'N/A'}
                                </Typography>
                                <Typography variant="body1">
                                    {getTranslatedText('specialties.minimumGrade', 'Minimum Grade')}: {specialty.specialtyRequirement.minGrade}
                                </Typography>
                                <Typography variant="body1">
                                    {getTranslatedText('specialties.requirementDetails', 'Requirement Details')}: {getTranslatedText(`specialties.${specialty.specialtyName}.requirementDetails`, specialty.specialtyRequirement.requirementDetails)}
                                </Typography>
                                <Typography variant="body1">
                                    {getTranslatedText('specialties.personalStatementRequired', 'Personal Statement Required')}: {specialty.specialtyRequirement.personalStatementRequired ? getTranslatedText('yes', 'Yes') : getTranslatedText('no', 'No')}
                                </Typography>
                                <Typography variant="body1">
                                    {getTranslatedText('specialties.letterOfRecommendationRequired', 'Letter of Recommendation Required')}: {specialty.specialtyRequirement.letterOfRecommendationRequired ? getTranslatedText('yes', 'Yes') : getTranslatedText('no', 'No')}
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
