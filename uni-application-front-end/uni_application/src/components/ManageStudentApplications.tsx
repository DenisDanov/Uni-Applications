import React, {useState, useEffect} from "react";
import {
    Container,
    Grid,
    Card,
    CardContent,
    Typography,
    Button,
    CardActions,
    CircularProgress,
    Alert,
    Box,
    useTheme,
    Chip
} from "@mui/material";
import {useKeycloak} from "../keycloak";
import AttachedFilesMenu from "./AttachedFilesMenu";
import {
    acceptApplication,
    declineApplication,
    deleteApplication,
    downloadFile,
    fetchFilteredStudentApplications,
    fetchStudentApplications,
    generateApplicationReceipt
} from "../axios/requests";
import Filter from "./Filter";
import {useNavigate} from "react-router-dom";
import {useTranslation} from "react-i18next";

const ManageStudentApplications: React.FC = () => {
    const [studentApplications, setStudentApplications] = useState<any[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const {keycloak} = useKeycloak();
    const theme = useTheme();
    const navigate = useNavigate();
    const {t, i18n} = useTranslation();

    const isBgLanguage = i18n.language === 'bg';

    const filterConfig = [
        {name: 'username', type: 'text'},
        {name: 'facultyName', type: 'text'},
        {name: 'specialtyName', type: 'text'},
        {name: 'applicationStatus', type: 'text'},
        {name: 'avgGrade', type: 'number'},
        {name: 'maxResults', type: 'number'},
    ];

    useEffect(() => {
        fetchStudentApplications()
            .then((applications) => {
                setStudentApplications(applications);
                setLoading(false);
            })
            .catch((err) => {
                setError(err.message);
                setLoading(false);
            });
    }, []);

    const handleAccept = (index: number) => {
        const application = studentApplications[index];
        acceptApplication(application)
            .then((response) => {
                const updatedApplications = [...studentApplications];
                updatedApplications[index].applicationStatus = response.applicationStatus;
                setStudentApplications(updatedApplications);
            })
            .catch((err) => {
                setError(err.message);
            });
    };

    const handleDecline = (index: number) => {
        const application = studentApplications[index];
        declineApplication(application)
            .then((response) => {
                const updatedApplications = [...studentApplications];
                updatedApplications[index].applicationStatus = response.applicationStatus;
                setStudentApplications(updatedApplications);
            })
            .catch((err) => {
                setError(err.message);
            });
    };

    const handleDelete = (index: number) => {
        const application = studentApplications[index];
        deleteApplication(application)
            .then(() => {
                const updatedApplications = studentApplications.filter((_, i) => i !== index);
                setStudentApplications(updatedApplications);
            })
            .catch((err) => {
                setError(err.message);
            });
    };

    const handleGenerateReceipt = (selectedApplication: any) => {
        generateApplicationReceipt(selectedApplication)
            .then((blob) => {
                const url = window.URL.createObjectURL(new Blob([blob], {type: 'application/pdf'}));
                const link = document.createElement('a');
                link.href = url;
                link.setAttribute('download', 'Application.pdf');
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);
            })
            .catch((err) => {
                setError(err.message);
                setTimeout(() => {
                    setError(null);
                }, 5000);
            });
    };

    const handleSearch = (activeFilters: any) => {
        fetchFilteredStudentApplications(activeFilters)
            .then((applications) => {
                setStudentApplications(applications);
            })
            .catch((err) => {
                setError(err.message);
            });
    };

    const handleEvaluateApplication = (application: any) => {
        navigate("/evaluate-application", {state: {application}});
    };

    if (loading) {
        return (
            <Container>
                <Box display="flex" justifyContent="center" alignItems="center" height="100vh">
                    <CircularProgress/>
                </Box>
            </Container>
        );
    }

    if (error) {
        return (
            <Container>
                <Alert severity="error">{t('error.loading')}</Alert>
            </Container>
        );
    }

    return (
        <Container>
            <Filter
                // @ts-ignore
                filters={filterConfig}
                onSearch={handleSearch}
                filterName={isBgLanguage ? t('filter.name') : 'Filter Applications'}
            />
            <Grid container spacing={3}>
                {studentApplications.map((application, index) => (
                    <Grid item xs={12} md={6} lg={4} key={index}>
                        <Card
                            sx={{
                                transition: "transform 0.3s",
                                "&:hover": {transform: "scale(1.02)"},
                                backgroundColor: theme.palette.background.paper,
                                boxShadow: theme.shadows[3],
                            }}
                        >
                            <CardContent>
                                <Typography variant="h6">{application.username}</Typography>
                                <Typography variant="body2">
                                    {isBgLanguage ? `${t('card.faculty')}: ${t(`faculties.${application.facultyName}`)}` : `Faculty: ${application.facultyName}`} ({application.facultyId})
                                </Typography>
                                <Typography variant="body2">
                                    {isBgLanguage ? `${t('card.specialty')}: ${t(`specialties.${application.specialtyName}.title`)}` : `Specialty: ${application.specialtyName}`} ({application.specialtyId})
                                </Typography>
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
                                <Typography variant="body2" mt={2}>
                                    {isBgLanguage ? t(`applicationStatus.${application.applicationStatus.applicationStatus}`) : application.applicationStatus.applicationDescription}
                                </Typography>
                                <Typography style={{
                                    wordBreak: 'break-all',
                                    wordWrap: 'break-word',
                                    whiteSpace: 'normal',
                                    maxWidth: '400px'
                                }} variant="body2" mt={2}>
                                    {isBgLanguage ? t('card.applicationDescription') : 'Application Description'}: {application.applicationDescription}
                                </Typography>
                                <Typography variant="body2" mt={2}>
                                    {isBgLanguage ? t('card.averageGrade') : 'Average Grade'}: {application.avgGrade}
                                </Typography>
                                {application.languageProficiencyTestResult && (
                                    <Typography variant="body2" mt={2}>
                                        {isBgLanguage ? t('card.languageProficiencyTestResult') : 'Language Proficiency Test Result'}: {application.languageProficiencyTestResult}
                                    </Typography>
                                )}
                                <Typography variant="body2" mt={2}>
                                    {isBgLanguage ? t('card.standardizedTestResult') : 'Standardized Test Result'}: {application.standardizedTestResult}
                                </Typography>
                                {application.letterOfRecommendation && (
                                    <Typography variant="body2" mt={2}>
                                        {isBgLanguage ? t('card.recommendationLetter') : 'Recommendation Letter'}:
                                        <Button
                                            color="primary"
                                            onClick={() => downloadFile(application)}
                                        >
                                            {isBgLanguage ? t('card.download') : 'Download'}
                                        </Button>
                                    </Typography>
                                )}
                                {application.personalStatement && (
                                    <Typography style={{
                                        wordBreak: 'break-all',
                                        wordWrap: 'break-word',
                                        whiteSpace: 'normal',
                                        maxWidth: '400px'
                                    }} variant="body2" mt={2}>
                                        {isBgLanguage ? t('card.personalStatement') : 'Personal Statement'}: {application.personalStatement}
                                    </Typography>
                                )}
                                <AttachedFilesMenu
                                    application={application}
                                    setExpanded={(expanded) => {
                                        const updatedApplications = [...studentApplications];
                                        updatedApplications[index].expanded = expanded;
                                        setStudentApplications(updatedApplications);
                                    }}
                                />
                            </CardContent>
                            <CardActions sx={{display: 'flex', justifyContent: 'center', flexWrap: 'wrap', gap: 1}}>
                                <Button
                                    disabled={!keycloak.hasRealmRole("FULL_ACCESS")}
                                    size="small"
                                    color="secondary"
                                    onClick={() => handleDelete(index)}
                                >
                                    {isBgLanguage ? t('actions.delete') : 'Delete'}
                                </Button>
                                {application.applicationStatus.applicationStatus === "PENDING" && (
                                    <>
                                        <Button
                                            disabled={!keycloak.hasRealmRole("FULL_ACCESS")}
                                            size="small"
                                            color="primary"
                                            onClick={() => handleAccept(index)}
                                        >
                                            {isBgLanguage ? t('actions.accept') : 'Accept'}
                                        </Button>
                                        <Button
                                            disabled={!keycloak.hasRealmRole("FULL_ACCESS")}
                                            size="small"
                                            color="error"
                                            onClick={() => handleDecline(index)}
                                        >
                                            {isBgLanguage ? t('actions.decline') : 'Decline'}
                                        </Button>
                                    </>
                                )}
                                <Button size="small" color="primary" onClick={() => handleGenerateReceipt(application)}>
                                    {isBgLanguage ? t('actions.generateReceipt') : 'Generate Receipt'}
                                </Button>
                                <Button size="small" color="primary"
                                        onClick={() => handleEvaluateApplication(application)}>
                                    {isBgLanguage ? t('actions.evaluateApplication') : 'Evaluate Application'}
                                </Button>
                            </CardActions>
                        </Card>
                    </Grid>
                ))}
            </Grid>
        </Container>
    );
};

export default ManageStudentApplications;
