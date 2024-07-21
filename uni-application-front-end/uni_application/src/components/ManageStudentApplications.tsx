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
    fetchFilteredStudentApplications,
    fetchStudentApplications,
    generateApplicationReceipt
} from "../axios/requests";
import Filter from "./Filter";
import {useNavigate} from "react-router-dom";

const ManageStudentApplications: React.FC = () => {
    const [studentApplications, setStudentApplications] = useState<any[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const {keycloak} = useKeycloak();
    const theme = useTheme();
    const navigate = useNavigate();

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
                <Alert severity="error">{error}</Alert>
            </Container>
        );
    }

    return (
        <Container>
            <Filter
                // @ts-ignore
                filters={filterConfig}
                onSearch={handleSearch}
                filterName={'Filter Applications'}
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
                                    Faculty: {application.facultyName} ({application.facultyId})
                                </Typography>
                                <Typography variant="body2">
                                    Specialty: {application.specialtyName} ({application.specialtyId})
                                </Typography>
                                <Typography variant="body2">
                                    Sent Date: {new Date(application.applicationSentDate).toLocaleDateString()}
                                </Typography>
                                <Box mt={2}>
                                    <Chip
                                        label={application.applicationStatus.applicationStatus}
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
                                    Status Description: {application.applicationStatus.applicationDescription}
                                </Typography>
                                <Typography style={{
                                    wordBreak: 'break-all',
                                    wordWrap: 'break-word',
                                    whiteSpace: 'normal',
                                    maxWidth: '400px'
                                }} variant="body2" mt={2}>
                                    Application Description: {application.applicationDescription}
                                </Typography>
                                <Typography variant="body2" mt={2}>
                                    Average Grade: {application.avgGrade}
                                </Typography>
                                {application.languageProficiencyTestResult && (
                                    <Typography variant="body2" mt={2}>
                                        Language Proficiency Test Result: {application.languageProficiencyTestResult}
                                    </Typography>
                                )}
                                <Typography variant="body2" mt={2}>
                                    Standardized Test Result: {application.standardizedTestResult}
                                </Typography>
                                {application.letterOfRecommendation && (
                                    <Typography variant="body2" mt={2}>
                                        Letter of Recommendation: {application.letterOfRecommendation}
                                    </Typography>
                                )}
                                {application.personalStatement && (
                                    <Typography style={{
                                        wordBreak: 'break-all',
                                        wordWrap: 'break-word',
                                        whiteSpace: 'normal',
                                        maxWidth: '400px'
                                    }} variant="body2" mt={2}>
                                        Personal Statement: {application.personalStatement}
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
                                    Delete
                                </Button>
                                {application.applicationStatus.applicationStatus === "PENDING" && (
                                    <>
                                        <Button
                                            disabled={!keycloak.hasRealmRole("FULL_ACCESS")}
                                            size="small"
                                            color="primary"
                                            onClick={() => handleAccept(index)}
                                        >
                                            Accept
                                        </Button>
                                        <Button
                                            disabled={!keycloak.hasRealmRole("FULL_ACCESS")}
                                            size="small"
                                            color="error"
                                            onClick={() => handleDecline(index)}
                                        >
                                            Decline
                                        </Button>
                                    </>
                                )}
                                <Button size="small" color="primary" onClick={() => handleGenerateReceipt(application)}>
                                    Generate Receipt
                                </Button>
                                <Button size="small" color="primary"
                                        onClick={() => handleEvaluateApplication(application)}>
                                    Evaluate Application
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
