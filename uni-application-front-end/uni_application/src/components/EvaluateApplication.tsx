import React, { useState, useEffect } from "react";
import { useLocation } from "react-router-dom";
import {
    Container,
    Typography,
    Box,
    Chip,
    CircularProgress,
    Alert,
    Grid,
    Divider,
    Avatar,
    Paper,
} from "@mui/material";
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import CancelIcon from '@mui/icons-material/Cancel';
import { getSpecialtyById } from "../axios/requests";
import { useTranslation } from 'react-i18next';

const EvaluateApplication: React.FC = () => {
    const location = useLocation();
    const { application } = location.state || {};
    const [requirements, setRequirements] = useState<any | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const { t, i18n } = useTranslation();

    const isBgLanguage = i18n.language === 'bg'; // Check if the current language is Bulgarian

    useEffect(() => {
        if (application) {
            getSpecialtyById(application.specialtyId)
                .then((specialty) => {
                    setRequirements(specialty.specialtyRequirement);
                    setLoading(false);
                })
                .catch((err) => {
                    setError(err.message);
                    setLoading(false);
                });
        } else {
            setLoading(false);
        }
    }, [application]);

    const meetsRequirement = (appValue: number, reqValue: number | null) => {
        if (reqValue === null || reqValue === undefined) return true;
        return appValue >= reqValue;
    };

    if (loading) {
        return (
            <Container>
                <Box display="flex" justifyContent="center" alignItems="center" height="100vh">
                    <CircularProgress />
                </Box>
            </Container>
        );
    }

    if (error) {
        return (
            <Container>
                <Alert severity="error">{error}</Alert>
            </Container>
        );
    }

    if (!application) {
        return (
            <Container>
                <Typography variant="h6">{isBgLanguage ? t('noApplicationData') : 'No application data available'}</Typography>
            </Container>
        );
    }

    return (
        <Container maxWidth="md" sx={{ mt: 4 }}>
            <Paper elevation={3} sx={{ p: 3, borderRadius: 2 }}>
                <Box display="flex" alignItems="center" mb={3}>
                    <Avatar sx={{ width: 56, height: 56, mr: 2 }}>{application.username[0]}</Avatar>
                    <Typography variant="h4">{application.username}</Typography>
                </Box>
                <Divider sx={{ mb: 3 }} />
                <Grid container spacing={2}>
                    <Grid item xs={12} sm={6}>
                        <Typography variant="body2">
                            {isBgLanguage ? `${t('card.faculty')}: ${t(`faculties.${application.facultyName}`)}` : `Faculty: ${application.facultyName}`} ({application.facultyId})
                        </Typography>
                    </Grid>
                    <Grid item xs={12} sm={6}>
                        <Typography variant="body2">
                            {isBgLanguage ? `${t('card.specialty')}: ${t(`specialties.${application.specialtyName}.title`)}` : `Specialty: ${application.specialtyName}`} ({application.specialtyId})
                        </Typography>
                    </Grid>
                    <Grid item xs={12} sm={6}>
                        <Typography variant="body2">
                            {isBgLanguage ? `${t('card.sentDate')}: ${new Date(application.applicationSentDate).toLocaleDateString()}` : `Sent Date: ${new Date(application.applicationSentDate).toLocaleDateString()}`}
                        </Typography>
                    </Grid>
                    <Grid item xs={12} sm={6}>
                        <Typography variant="body2">
                            {isBgLanguage ? `${t('status')}:` : `Status:`}
                            <Box mt={2}>
                                <Chip
                                    label={isBgLanguage ? t(`profile.status.${application.applicationStatus.applicationStatus}`) : application.applicationStatus.applicationStatus}
                                    color={
                                        application.applicationStatus.applicationStatus === "PENDING"
                                            ? "warning"
                                            : application.applicationStatus.applicationStatus === "DECLINED"
                                                ? "error"
                                                : "success"
                                    }
                                />
                            </Box>
                        </Typography>

                    </Grid>
                    <Grid item xs={12}>
                        <Typography variant="body2" mt={2}>
                            {isBgLanguage ? t(`applicationStatus.${application.applicationStatus.applicationStatus}`) : application.applicationStatus.applicationDescription}
                        </Typography>
                    </Grid>
                    <Grid item xs={12}>
                        <Typography style={{
                            wordBreak: 'break-all',
                            wordWrap: 'break-word',
                            whiteSpace: 'normal',
                            maxWidth: '400px'
                        }} variant="body2" mt={2}>
                            {isBgLanguage ? `${t('card.applicationDescription')}: ${application.applicationDescription}` : `Application Description: ${application.applicationDescription}`}
                        </Typography>
                    </Grid>
                    <Grid item xs={12}>
                        <Box display="flex" alignItems="center" mt={2}>
                            <Typography variant="subtitle1" color="textSecondary" mr={1}>
                                {isBgLanguage ? t('card.averageGrade') : 'Average Grade'}:
                            </Typography>
                            <Typography variant="body1">{application.avgGrade}</Typography>
                            {meetsRequirement(application.avgGrade, requirements?.minGrade) ? (
                                <CheckCircleIcon color="success" sx={{ ml: 1 }} />
                            ) : (
                                <CancelIcon color="error" sx={{ ml: 1 }} />
                            )}
                        </Box>
                    </Grid>
                    {application.languageProficiencyTestResult && (
                        <Grid item xs={12}>
                            <Box display="flex" alignItems="center" mt={2}>
                                <Typography variant="subtitle1" color="textSecondary" mr={1}>
                                    {isBgLanguage ? t('card.languageProficiencyTestResult') : 'Language Proficiency Test Result'}:
                                </Typography>
                                <Typography variant="body1">{application.languageProficiencyTestResult}</Typography>
                                {meetsRequirement(application.languageProficiencyTestResult, requirements?.languageProficiencyTestMinResult) ? (
                                    <CheckCircleIcon color="success" sx={{ ml: 1 }} />
                                ) : (
                                    <CancelIcon color="error" sx={{ ml: 1 }} />
                                )}
                            </Box>
                        </Grid>
                    )}
                    <Grid item xs={12}>
                        <Box display="flex" alignItems="center" mt={2}>
                            <Typography variant="subtitle1" color="textSecondary" mr={1}>
                                {isBgLanguage ? t('card.standardizedTestResult') : 'Standardized Test Result'}:
                            </Typography>
                            <Typography variant="body1">{application.standardizedTestResult}</Typography>
                            {meetsRequirement(application.standardizedTestResult, requirements?.standardizedTestMinResult) ? (
                                <CheckCircleIcon color="success" sx={{ ml: 1 }} />
                            ) : (
                                <CancelIcon color="error" sx={{ ml: 1 }} />
                            )}
                        </Box>
                    </Grid>
                    {requirements?.letterOfRecommendationRequired && (
                        <Grid item xs={12}>
                            <Box display="flex" alignItems="center" mt={2}>
                                <Typography variant="subtitle1" color="textSecondary" mr={1}>
                                    {isBgLanguage ? t('letterOfRecommendation') : 'Letter of Recommendation'}:
                                </Typography>
                                <Typography variant="body1">{application.letterOfRecommendation ? (isBgLanguage ? t('provided') : "Provided") : (isBgLanguage ? t('notProvided') : "Not Provided")}</Typography>
                                {application.letterOfRecommendation ? (
                                    <CheckCircleIcon color="success" sx={{ ml: 1 }} />
                                ) : (
                                    <CancelIcon color="error" sx={{ ml: 1 }} />
                                )}
                            </Box>
                        </Grid>
                    )}
                    {requirements?.personalStatementRequired && (
                        <Grid item xs={12}>
                            <Box display="flex" alignItems="center" mt={2}>
                                <Typography variant="subtitle1" color="textSecondary" mr={1}>
                                    {isBgLanguage ? t('card.personalStatement') : 'Personal Statement'}:
                                </Typography>
                                <Typography variant="body1">{application.personalStatement ? (isBgLanguage ? t('provided') : "Provided") : (isBgLanguage ? t('notProvided') : "Not Provided")}</Typography>
                                {application.personalStatement ? (
                                    <CheckCircleIcon color="success" sx={{ ml: 1 }} />
                                ) : (
                                    <CancelIcon color="error" sx={{ ml: 1 }} />
                                )}
                            </Box>
                        </Grid>
                    )}
                </Grid>
            </Paper>
        </Container>
    );
};

export default EvaluateApplication
