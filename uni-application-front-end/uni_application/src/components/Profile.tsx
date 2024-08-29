import React, {useState, useEffect} from "react";
import {
    Container,
    Box,
    TextField,
    Typography,
    Button,
    CircularProgress,
    Alert,
    Accordion,
    AccordionSummary,
    AccordionDetails,
    Chip,
    Grid,
    Card,
    CardContent,
} from "@mui/material";
import {useFormik} from "formik";
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import PersonIcon from '@mui/icons-material/Person';
import EmailIcon from '@mui/icons-material/Email';
import DateRangeIcon from '@mui/icons-material/DateRange';
import PhoneIcon from '@mui/icons-material/Phone';
import WorkIcon from '@mui/icons-material/Work';
import BadgeIcon from '@mui/icons-material/Badge';
import {useTranslation} from 'react-i18next';
import {validationSchemaProfile} from "../types/validationSchema";
import {downloadFile, generateApplicationProgram, getUserData, handleSubmit} from "../axios/requests";
import AttachedFilesMenu from "./AttachedFilesMenu";

const Profile: React.FC = () => {
    const {t, i18n} = useTranslation();
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);
    const [errorUpdate, setErrorUpdate] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);
    const [studentApplications, setStudentApplications] = useState<any[]>([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                await getUserData(formik, setLoading, setStudentApplications, setError);
            } catch (err: any) {
                setError(err.message);
                setLoading(false);
            }
        };

        fetchData();
    }, []);

    const handleDownloadProgram = async (application: any) => {
        try {
            const blob = await generateApplicationProgram(application);
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.style.display = 'none';
            a.href = url;
            a.download = 'Studying_Program.pdf';
            document.body.appendChild(a);
            a.click();
            window.URL.revokeObjectURL(url);
        } catch (error) {
            console.error("Failed to download program:", error);
        }
    };

    const formik = useFormik({
        initialValues: {
            username: "",
            email: "",
            firstName: "",
            middleName: "",
            lastName: "",
            password: "",
            dateOfBirth: "",
            phoneNumber: "",
            accessLevel: {
                accessType: "",
                accessDescription: "",
            },
            roleDTO: {
                role: "",
                roleDescription: "",
            },
        },
        validationSchema: () => validationSchemaProfile(formik.values, localStorage.getItem('language') || 'en'),
        onSubmit: async (values) => {
            try {
                await handleSubmit(values, formik, setSuccess, setErrorUpdate, t);
            } catch (err: any) {
                setError(err.message);
            }
        },
    });

    const isBgLanguage = i18n.language === 'bg';

    if (loading) {
        return (
            <Container>
                <Box my={4} textAlign="center">
                    <CircularProgress/>
                </Box>
            </Container>
        );
    }

    if (error) {
        return (
            <Container>
                <Box my={4} textAlign="center">
                    <Alert severity="error">{error}</Alert>
                </Box>
            </Container>
        );
    }

    return (
        <Container maxWidth="md">
            <Box my={4} textAlign="center">
                <Typography variant="h4" component="h1" gutterBottom>
                    {isBgLanguage ? t('profile.title') : 'Profile'}
                </Typography>
            </Box>
            <Box my={4}>
                {errorUpdate && (
                    <Box mb={2}>
                        <Alert severity="error">{errorUpdate}</Alert>
                    </Box>
                )}
                {success && (
                    <Box mb={2}>
                        <Alert severity="success">{success}</Alert>
                    </Box>
                )}
                <Card variant="outlined">
                    <CardContent>
                        <Typography variant="h6" gutterBottom>
                            {isBgLanguage ? t('profile.details') : 'Profile Details'}
                        </Typography>
                        <form onSubmit={formik.handleSubmit}>
                            <Grid container spacing={2}>
                                <Grid item xs={12} sm={6}>
                                    <TextField
                                        label={
                                            <Box display="flex" alignItems="center">
                                                <PersonIcon sx={{mr: 1}}/>
                                                {isBgLanguage ? t('profile.username') : 'Username'}
                                            </Box>
                                        }
                                        value={formik.values.username}
                                        margin="normal"
                                        fullWidth
                                        InputLabelProps={{shrink: true}}
                                        InputProps={{readOnly: true}}
                                    />
                                </Grid>
                                <Grid item xs={12} sm={6}>
                                    <TextField
                                        label={
                                            <Box display="flex" alignItems="center">
                                                <EmailIcon sx={{mr: 1}}/>
                                                {isBgLanguage ? t('profile.email') : 'Email'}
                                            </Box>
                                        }
                                        name="email"
                                        value={formik.values.email}
                                        onChange={formik.handleChange}
                                        onBlur={formik.handleBlur}
                                        margin="normal"
                                        fullWidth
                                        InputLabelProps={{shrink: true}}
                                        InputProps={{readOnly: true}}
                                        error={formik.touched.email && Boolean(formik.errors.email)}
                                        helperText={formik.touched.email && formik.errors.email}
                                    />
                                </Grid>
                                {formik.values.roleDTO.role === 'STUDENT' && (
                                    <>
                                        <Grid item xs={12} sm={6}>
                                            <TextField
                                                label={
                                                    <Box display="flex" alignItems="center">
                                                        <DateRangeIcon sx={{mr: 1}}/>
                                                        {isBgLanguage ? t('profile.dateOfBirth') : 'Date of Birth'}
                                                    </Box>
                                                }
                                                name="dateOfBirth"
                                                type="date"
                                                value={formik.values.dateOfBirth}
                                                onChange={formik.handleChange}
                                                onBlur={formik.handleBlur}
                                                margin="normal"
                                                fullWidth
                                                InputLabelProps={{shrink: true}}
                                                error={formik.touched.dateOfBirth && Boolean(formik.errors.dateOfBirth)}
                                                helperText={formik.touched.dateOfBirth && formik.errors.dateOfBirth}
                                            />
                                        </Grid>
                                        <Grid item xs={12} sm={6}>
                                            <TextField
                                                label={
                                                    <Box display="flex" alignItems="center">
                                                        <PhoneIcon sx={{mr: 1}}/>
                                                        {isBgLanguage ? t('profile.phoneNumber') : 'Phone Number'}
                                                    </Box>
                                                }
                                                name="phoneNumber"
                                                value={formik.values.phoneNumber}
                                                onChange={formik.handleChange}
                                                onBlur={formik.handleBlur}
                                                margin="normal"
                                                fullWidth
                                                error={formik.touched.phoneNumber && Boolean(formik.errors.phoneNumber)}
                                                helperText={formik.touched.phoneNumber && formik.errors.phoneNumber}
                                            />
                                        </Grid>
                                    </>
                                )}
                                <Grid item xs={12} sm={6}>
                                    <TextField
                                        label={
                                            <Box display="flex" alignItems="center">
                                                <PersonIcon sx={{mr: 1}}/>
                                                {isBgLanguage ? t('profile.firstName') : 'First Name'}
                                            </Box>
                                        }
                                        name="firstName"
                                        value={formik.values.firstName}
                                        onChange={formik.handleChange}
                                        onBlur={formik.handleBlur}
                                        margin="normal"
                                        fullWidth
                                        error={formik.touched.firstName && Boolean(formik.errors.firstName)}
                                        helperText={formik.touched.firstName && formik.errors.firstName}
                                    />
                                </Grid>
                                <Grid item xs={12} sm={6}>
                                    <TextField
                                        label={
                                            <Box display="flex" alignItems="center">
                                                <PersonIcon sx={{mr: 1}}/>
                                                {isBgLanguage ? t('profile.middleName') : 'Middle Name'}
                                            </Box>
                                        }
                                        name="middleName"
                                        value={formik.values.middleName}
                                        onChange={formik.handleChange}
                                        onBlur={formik.handleBlur}
                                        margin="normal"
                                        fullWidth
                                        error={formik.touched.middleName && Boolean(formik.errors.middleName)}
                                        helperText={formik.touched.middleName && formik.errors.middleName}
                                    />
                                </Grid>
                                <Grid item xs={12} sm={6}>
                                    <TextField
                                        label={
                                            <Box display="flex" alignItems="center">
                                                <PersonIcon sx={{mr: 1}}/>
                                                {isBgLanguage ? t('profile.lastName') : 'Last Name'}
                                            </Box>
                                        }
                                        name="lastName"
                                        value={formik.values.lastName}
                                        onChange={formik.handleChange}
                                        onBlur={formik.handleBlur}
                                        margin="normal"
                                        fullWidth
                                        error={formik.touched.lastName && Boolean(formik.errors.lastName)}
                                        helperText={formik.touched.lastName && formik.errors.lastName}
                                    />
                                </Grid>
                                <Grid item xs={12} sm={6}>
                                    <TextField
                                        label={
                                            <Box display="flex" alignItems="center">
                                                <BadgeIcon sx={{mr: 1}}/>
                                                {isBgLanguage ? t('profile.accountType') : 'Account Type'}
                                            </Box>
                                        }
                                        value={t(`profile.accountTypeValue.${formik.values.roleDTO.role}`, {defaultValue: formik.values.roleDTO.role})}
                                        margin="normal"
                                        fullWidth
                                        InputProps={{readOnly: true}}
                                    />
                                </Grid>
                                {formik.values.roleDTO.role === 'ADMIN' && (
                                    <Grid item xs={12} sm={6}>
                                        <TextField
                                            label={
                                                <Box display="flex" alignItems="center">
                                                    <WorkIcon sx={{mr: 1}}/>
                                                    {isBgLanguage ? t('profile.accessLevel') : 'Access Level'}
                                                </Box>
                                            }
                                            value={t(`profile.accessLevelType.${formik.values.accessLevel.accessType}`, {defaultValue: formik.values.accessLevel.accessType})}
                                            margin="normal"
                                            fullWidth
                                            InputProps={{readOnly: true}}
                                        />
                                    </Grid>
                                )}
                            </Grid>
                            <Box mt={2} textAlign="center">
                                <Button variant="contained" color="primary" type="submit">
                                    {isBgLanguage ? t('profile.saveChanges') : 'Save Changes'}
                                </Button>
                            </Box>
                        </form>
                    </CardContent>
                </Card>
            </Box>
            <Box my={4}>
                {formik.values.roleDTO.role === 'STUDENT' && (
                    <Typography variant="h4" gutterBottom>
                        {isBgLanguage ? t('profile.studentApplications') : 'Student Applications'}
                    </Typography>
                )}
                {studentApplications.length > 0 ? (
                    studentApplications.map((application, index) => (
                        <Card key={index} variant="outlined" sx={{mb: 2}}>
                            <Accordion>
                                <AccordionSummary
                                    expandIcon={<ExpandMoreIcon/>}
                                    aria-controls={`panel${index}a-content`}
                                    id={`panel${index}a-header`}
                                >
                                    <Typography variant="h6">
                                        {isBgLanguage ? t('profile.application', {
                                            index: index + 1,
                                            specialtyName: t(`profile.specialties.${application.specialtyName}`, {defaultValue: application.specialtyName})
                                        }) : `Application #${index + 1} - ${application.specialtyName}`}
                                    </Typography>
                                </AccordionSummary>
                                <AccordionDetails>
                                    <Box mb={2}>
                                        <Typography color="textSecondary" gutterBottom>
                                            {isBgLanguage ? t('profile.faculty', {facultyName: t(`profile.faculties.${application.facultyName}`, {defaultValue: application.facultyName})}) : `Faculty: ${application.facultyName}`}
                                        </Typography>
                                        <Typography color="textSecondary">
                                            {isBgLanguage ? t('profile.sent', {date: new Date(application.applicationSentDate).toLocaleDateString()}) : `Sent: ${new Date(application.applicationSentDate).toLocaleDateString()}`}
                                        </Typography>
                                    </Box>
                                    <Box mb={2}>
                                        <Typography component={'span'} color="textSecondary">
                                            {isBgLanguage
                                                ? t('status')
                                                : `Status`}
                                            {": "}
                                        </Typography>
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
                                    <Typography style={{
                                        wordBreak: 'break-all',
                                        wordWrap: 'break-word',
                                        whiteSpace: 'normal',
                                        maxWidth: '400px'
                                    }}>
                                        {isBgLanguage ? t('profile.description', {description: application.applicationDescription}) : `Description: ${application.applicationDescription}`}
                                    </Typography>
                                    <Typography>
                                        {isBgLanguage ? t('profile.averageGrade', {avgGrade: application.avgGrade}) : `Average Grade: ${application.avgGrade}`}
                                    </Typography>
                                    {application.languageProficiencyTestResult &&
                                        <Typography>
                                            {isBgLanguage ? t('profile.languageTest', {languageTest: application.languageProficiencyTestResult}) : `Language Test: ${application.languageProficiencyTestResult}`}
                                        </Typography>}
                                    <Typography>
                                        {isBgLanguage ? t('profile.standardizedTest', {standardizedTest: application.standardizedTestResult}) : `Standardized Test: ${application.standardizedTestResult}`}
                                    </Typography>
                                    <Typography style={{
                                        marginBottom: "0",
                                        wordBreak: 'break-all',
                                        wordWrap: 'break-word',
                                        whiteSpace: 'normal',
                                        maxWidth: '400px'
                                    }}>
                                        {isBgLanguage ? t('profile.personalStatement', {personalStatement: application.personalStatement}) : `Personal Statement: ${application.personalStatement}`}
                                    </Typography>
                                    {application.letterOfRecommendation && (
                                        <Typography>
                                            {isBgLanguage ? t('profile.recommendationLetter') : 'Recommendation Letter:'}
                                            <Button
                                                color="primary"
                                                onClick={() => downloadFile(application)}
                                                sx={{ml: 1, padding: "0 8px"}}
                                            >
                                                {isBgLanguage ? t('profile.download') : 'Download'}
                                            </Button>
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
                                    {application.applicationStatus.applicationStatus === "ACCEPTED" && (
                                        <Box mt={2} textAlign="center">
                                            <Button variant="contained" color="primary"
                                                    onClick={() => handleDownloadProgram(application)}>
                                                {isBgLanguage ? t('profile.downloadProgram') : 'Download Studying Program'}
                                            </Button>
                                        </Box>
                                    )}
                                </AccordionDetails>
                            </Accordion>
                        </Card>
                    ))
                ) : formik.values.roleDTO.role === 'STUDENT' && (
                    <Typography variant="body1" align="center">
                        {isBgLanguage ? t('profile.noApplications') : 'No applications to display.'}
                    </Typography>
                )}
            </Box>
        </Container>
    );
};

export default Profile;

