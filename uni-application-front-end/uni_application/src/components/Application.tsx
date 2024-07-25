import React, { useState, useEffect, ChangeEvent } from 'react';
import {
    MenuItem,
    Select,
    TextField,
    Grid,
    Box,
    Typography,
    Alert,
    FormControl,
    FormHelperText,
    Button,
    IconButton
} from '@mui/material';
import { useFormik } from 'formik';
import { Specialty } from "../types/Specialty";
import { useDispatch, useSelector } from "react-redux";
import { addFiles, clearFiles, selectSelectedFileNames, selectSelectedFiles } from "../slices/fileSlice";
import { AppDispatch, RootState } from "../store";
import FileUpload from "./FileUpload";
import { validationSchemaApplication } from "../types/validationSchema";
import { fetchFaculties } from '../slices/facultySlice';
import { getSpecialtiesByFaculty, submitApplication } from "../axios/requests";
import { StudentsRequirementsResultsDTO } from "../types/StudentRequirementsResultsDTO";
import { axiosClientDefault } from "../axios/axiosClient";
import { useKeycloak } from "../keycloak";
import { LicenseInfo } from "@mui/x-license-pro";
import { LoadingButton } from "@mui/lab";
import UploadFileIcon from '@mui/icons-material/UploadFile';
import { getFileIcon } from "../types/fileIcons";
import DeleteIcon from "@mui/icons-material/Delete";
import { useTranslation } from 'react-i18next';

LicenseInfo.setLicenseKey('e0d9bb8070ce0054c9d9ecb6e82cb58fTz0wLEU9MzI0NzIxNDQwMDAwMDAsUz1wcmVtaXVtLExNPXBlcnBldHVhbCxLVj0y');

const useFetchSpecialties = (facultyId: number | null): Specialty[] => {
    const [specialties, setSpecialties] = useState<Specialty[]>([]);

    useEffect(() => {
        if (facultyId !== null) {
            getSpecialtiesByFaculty(facultyId).then(data => {
                setSpecialties(data);
            }).catch(err => {
                console.log(err);
            });
        } else {
            setSpecialties([]);
        }
    }, [facultyId]);

    return specialties;
};

const ApplicationForm: React.FC = () => {
    const { t, i18n } = useTranslation();
    const dispatch = useDispatch<AppDispatch>();
    const { faculties } = useSelector((state: RootState) => state.faculties);
    const [selectedFacultyId, setSelectedFacultyId] = useState<number | null>(null);
    const specialties = useFetchSpecialties(selectedFacultyId);
    const selectedFiles = useSelector(selectSelectedFiles);
    const selectedFileNames = useSelector(selectSelectedFileNames);
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);
    const { keycloak } = useKeycloak();
    const allowedExtensions = ['pdf', 'doc', 'docx', 'txt'];
    const [loading, setLoading] = useState<boolean>(false);

    const [results, setResults] = useState({
        languageProficiencyTestResult: null,
        standardizedTestResult: null
    });

    const [formRequirements, setFormRequirements] = useState({
        isLanguageProficiencyRequired: false,
        isLetterOfRecommendationRequired: false,
        isPersonalStatementRequired: false,
    });

    useEffect(() => {
        dispatch(fetchFaculties());
    }, [dispatch]);

    useEffect(() => {
        axiosClientDefault
            .get<StudentsRequirementsResultsDTO>(`/students-requirements-results/${keycloak.tokenParsed?.preferred_username}`)
            .then((response: any) => {
                formik.setFieldValue("standardizedTestResult", response.data.standardizedTestResult === null ? -1 : response.data.standardizedTestResult);
                formik.setFieldValue("languageProficiencyTestResult", response.data.languageProficiencyTestResult);
                formik.touched.standardizedTestResult = true;
                formik.touched.languageProficiencyTestResult = true;

                setResults(response.data);

                if (formRequirements.isLanguageProficiencyRequired && response.data.languageProficiencyTestResult === null) {
                    formik.validateField('languageProficiencyTestResult');
                }
            })
            .catch((error) => {
                console.error('Error fetching results:', error);
            });
    }, [keycloak.tokenParsed?.preferred_username, formRequirements.isLanguageProficiencyRequired]);

    const formik = useFormik({
        initialValues: {
            specialtyId: 0,
            specialty: {} as Specialty,
            facultyName: '',
            facultyId: 0,
            applicationDescription: '',
            avgGrade: 0,
            languageProficiencyTestResult: '',
            standardizedTestResult: 0,
            letterOfRecommendation: null as File | null,
            personalStatement: ''
        },
        validationSchema: validationSchemaApplication(formRequirements, i18n.language || 'en'),
        onSubmit: (values) => {
            if (formRequirements.isLetterOfRecommendationRequired && !values.letterOfRecommendation) {
                setError(t('uploadLetterOfRecommendationError', { defaultValue: 'Please upload a letter of recommendation.' }));
                return;
            }

            setError(null);
            setLoading(true);
            const { letterOfRecommendation, specialty, facultyName, ...dataValues } = values;

            const formDataValues = {
                ...dataValues,
                applicationSentDate: new Date(),
                applicationStatus: {
                    applicationStatus: 'PENDING',
                    applicationDescription: t("applicationUnderReview", { defaultValue: 'Your application is under review.' })
                }
            };

            const formData = new FormData();
            selectedFiles.forEach((file, index) => formData.append(`file${index}`, file));
            if (formRequirements.isLetterOfRecommendationRequired && values.letterOfRecommendation) {
                formData.append('letterOfRecommendation', values.letterOfRecommendation);
            }
            formData.append('studentApplicationCreateDTO', JSON.stringify(formDataValues));
            formData.append('specialtyName', values.specialty.specialtyName);
            formData.append('facultyName', values.facultyName);

            submitApplication(formData)
                .then(() => {
                    setSuccess(t("applicationSubmittedSuccessfully", { defaultValue: 'Your application has been submitted successfully.' }));
                    setTimeout(() => setSuccess(null), 5000);
                    const languageProficiencyResult = formik.values.languageProficiencyTestResult;
                    formik.resetForm();
                    formik.setFieldValue("languageProficiencyTestResult", languageProficiencyResult);
                    const selectedFaculty = faculties.find(faculty => faculty.id === values.facultyId);
                    const facultyName = selectedFaculty ? selectedFaculty.facultyName : '';
                    formik.setFieldValue("facultyId", values.facultyId);
                    formik.setFieldValue("facultyName", facultyName);
                    setSelectedFacultyId(values.facultyId);
                    dispatch(clearFiles());
                })
                .catch((err: any) => {
                    const serverErrors = err.errors?.reduce((acc: any, curr: any) => {
                        const fieldName = curr.pointer.replace('/', '');
                        acc[fieldName] = curr.message;
                        return acc;
                    }, {}) || {};
                    formik.setErrors(serverErrors);
                    setError(err.errors?.[0]?.message || t("errorOccurred", { defaultValue: 'An error occurred. Please try again later.' }));
                    setTimeout(() => setError(null), 5000);
                }).finally(() => {
                setLoading(false);
            });
        }
    });

    const handleFileChange = (event: ChangeEvent<HTMLInputElement>) => {
        if (event.target.files) {
            const newFiles = Array.from(event.target.files);
            const allowedExtensions = ['txt', 'pdf', 'doc', 'docx'];
            const existingFileNamesSet = new Set(selectedFileNames);

            const filteredFiles = newFiles.filter(file => {
                const fileExtension = file.name.split('.').pop()?.toLowerCase();
                return fileExtension && allowedExtensions.includes(fileExtension) && !existingFileNamesSet.has(file.name);
            });

            if (filteredFiles.length > 0) {
                dispatch(addFiles({ files: filteredFiles, fileNames: filteredFiles.map(file => file.name) }));
            }
        }
    };

    const handleFacultyChange = (event: ChangeEvent<{ value: unknown }>) => {
        const facultyId = event.target.value as number;
        setSelectedFacultyId(facultyId);

        const selectedFaculty = faculties.find(faculty => faculty.id === facultyId);
        const facultyName = selectedFaculty ? selectedFaculty.facultyName : '';

        formik.setFieldValue('facultyId', facultyId);
        formik.setFieldValue("facultyName", facultyName);
        formik.setFieldValue('specialtyId', 0);
        formik.setFieldValue('specialty', {});
    };

    const handleSpecialtyChange = (event: ChangeEvent<{ value: unknown }>) => {
        const specialtyId = event.target.value as number;
        const specialty = specialties.find(s => s.id === specialtyId) || {} as Specialty;

        setFormRequirements({
            isLanguageProficiencyRequired: !!specialty.specialtyRequirement?.languageProficiencyTestMinResult,
            isLetterOfRecommendationRequired: !!specialty.specialtyRequirement?.letterOfRecommendationRequired,
            isPersonalStatementRequired: !!specialty.specialtyRequirement?.personalStatementRequired,
        });

        if (!specialty.specialtyRequirement.letterOfRecommendationRequired) {
            formik.values.letterOfRecommendation = null;
        }
        if (!specialty.specialtyRequirement.languageProficiencyTestMinResult) {
            // @ts-ignore
            formik.values.languageProficiencyTestResult = null;
        }

        formik.setFieldValue('specialtyId', specialtyId);
        formik.setFieldValue('specialty', specialty);
    };

    const handleLetterOfRecommendationChange = (event: ChangeEvent<HTMLInputElement>) => {
        if (event.target.files) {
            const file = event.target.files[0];
            if (file) {
                const fileExtension = file.name.split('.').pop()?.toLowerCase();
                if (fileExtension && allowedExtensions.includes(fileExtension)) {
                    formik.setFieldValue('letterOfRecommendation', file);
                    setError(null);
                } else {
                    setError(t('unsupportedFileTypeError', { defaultValue: 'Unsupported file type.' }));
                    setTimeout(() => setError(null), 5000);
                }
            }
        }
    };

    const translateFaculty = (facultyName: string) => {
        const translations = {
            "Faculty of Computer Science": {
                bg: "Факултет по компютърни науки"
            },
            "Faculty of Arts": {
                bg: "Факултет по изкуства"
            },
            "Faculty of Medicine": {
                bg: "Факултет по медицина"
            }
        };

        // @ts-ignore
        return translations[facultyName]?.[i18n.language] || facultyName;
    };

    const translateSpecialty = (specialtyName: string) => {
        const translations = {
            "Computer Engineering": {
                bg: "Компютърно инженерство"
            },
            "Fine Arts": {
                bg: "Изящни изкуства"
            },
            "Medical Doctor": {
                bg: "Медицински доктор"
            }
        };

        // @ts-ignore
        return translations[specialtyName]?.[i18n.language] || specialtyName;
    };

    return (
        <Box p={2}>
            <Typography variant="h4" mb={2}>{i18n.language === 'bg' ? t('studentApplicationFormTitle') : 'Student Application Form'}</Typography>
            {error && <Box mb={2}><Alert severity="error">{error}</Alert></Box>}
            {success && <Box mb={2}><Alert severity="success">{success}</Alert></Box>}
            <form onSubmit={formik.handleSubmit}>
                <Grid container spacing={2}>
                    <Grid item xs={12}>
                        <FormControl fullWidth error={formik.touched.facultyId && Boolean(formik.errors.facultyId)}>
                            <Select
                                labelId="faculty-label"
                                value={selectedFacultyId ?? ''}
                                // @ts-ignore
                                onChange={handleFacultyChange}
                                displayEmpty
                                name="facultyId"
                            >
                                <MenuItem value="" disabled>{i18n.language === 'bg' ? t('selectFaculty') :  'Select Faculty'}</MenuItem>
                                {faculties.map(faculty => (
                                    <MenuItem key={faculty.id}
                                              value={faculty.id}>{translateFaculty(faculty.facultyName)}</MenuItem>
                                ))}
                            </Select>
                            {formik.touched.facultyId && formik.errors.facultyId && (
                                <FormHelperText>{formik.errors.facultyId}</FormHelperText>
                            )}
                        </FormControl>
                    </Grid>
                    {selectedFacultyId !== null && specialties.length > 0 && (
                        <Grid item xs={12}>
                            <FormControl fullWidth error={formik.touched.specialtyId && Boolean(formik.errors.specialtyId)}>
                                <Select
                                    labelId="specialty-label"
                                    value={formik.values.specialtyId === 0 ? '' : formik.values.specialtyId}
                                    // @ts-ignore
                                    onChange={handleSpecialtyChange}
                                    displayEmpty
                                    name="specialtyId"
                                >
                                    <MenuItem value="" disabled>{i18n.language === 'bg' ? t('selectSpecialty') : 'Select Specialty'}</MenuItem>
                                    {specialties.map(specialty => (
                                        <MenuItem key={specialty.id}
                                                  value={specialty.id}>{translateSpecialty(specialty.specialtyName)}</MenuItem>
                                    ))}
                                </Select>
                                {formik.touched.specialtyId && formik.errors.specialtyId && (
                                    <FormHelperText>{formik.errors.specialtyId}</FormHelperText>
                                )}
                            </FormControl>
                        </Grid>
                    )}
                    {formik.values.specialtyId > 0 && (
                        <>
                            <Grid item xs={12}>
                                <TextField
                                    fullWidth
                                    label={t('applicationDescription', { defaultValue: 'Application Description' })}
                                    name="applicationDescription"
                                    value={formik.values.applicationDescription}
                                    onChange={formik.handleChange}
                                    onBlur={formik.handleBlur}
                                    error={formik.touched.applicationDescription && Boolean(formik.errors.applicationDescription)}
                                    helperText={formik.touched.applicationDescription && formik.errors.applicationDescription}
                                    multiline
                                    maxRows={4}
                                />
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <TextField
                                    fullWidth
                                    label={t('averageGrade', { defaultValue: 'Average Grade' })}
                                    name="avgGrade"
                                    type="number"
                                    value={formik.values.avgGrade}
                                    onChange={formik.handleChange}
                                    onBlur={formik.handleBlur}
                                    error={formik.touched.avgGrade && Boolean(formik.errors.avgGrade)}
                                    helperText={formik.touched.avgGrade && formik.errors.avgGrade}
                                />
                            </Grid>
                            {formRequirements.isLanguageProficiencyRequired && (
                                <Grid item xs={12} sm={6}>
                                    <TextField
                                        fullWidth
                                        label={t('languageProficiencyTestResult', { defaultValue: 'Language Proficiency Test Result' })}
                                        name="languageProficiencyTestResult"
                                        type="number"
                                        value={results.languageProficiencyTestResult ?? ''}
                                        onChange={formik.handleChange}
                                        onBlur={formik.handleBlur}
                                        error={formik.touched.languageProficiencyTestResult && Boolean(formik.errors.languageProficiencyTestResult)}
                                        helperText={formik.touched.languageProficiencyTestResult && formik.errors.languageProficiencyTestResult}
                                        disabled
                                    />
                                </Grid>
                            )}
                            <Grid item xs={12} sm={6}>
                                <TextField
                                    fullWidth
                                    label={t('standardizedTestResult', { defaultValue: 'Standardized Test Result' })}
                                    name="standardizedTestResult"
                                    type="number"
                                    value={results.standardizedTestResult ?? ''}
                                    onChange={formik.handleChange}
                                    onBlur={formik.handleBlur}
                                    error={formik.touched.standardizedTestResult && Boolean(formik.errors.standardizedTestResult)}
                                    helperText={formik.touched.standardizedTestResult && formik.errors.standardizedTestResult}
                                    disabled
                                />
                            </Grid>
                            {formRequirements.isLetterOfRecommendationRequired && (
                                <Grid item xs={12} sm={6}>
                                    <FormControl fullWidth>
                                        <TextField
                                            label={t('letterOfRecommendation', { defaultValue: 'Letter of Recommendation' })}
                                            fullWidth
                                            InputProps={{
                                                readOnly: true,
                                                startAdornment: (
                                                    <Box
                                                        display="flex"
                                                        alignItems="center"
                                                        width="100%"
                                                        p={1}
                                                        border={1}
                                                        borderColor="grey.300"
                                                        borderRadius={1}
                                                        bgcolor="background.paper"
                                                    >
                                                        {formik.values.letterOfRecommendation ? (
                                                            <>
                                                                {getFileIcon(formik.values.letterOfRecommendation.name)}
                                                                <Typography
                                                                    ml={1}>{formik.values.letterOfRecommendation.name}</Typography>
                                                                <IconButton
                                                                    aria-label="delete"
                                                                    size="small"
                                                                    onClick={() => formik.setFieldValue('letterOfRecommendation', null)}
                                                                    style={{ marginLeft: 'auto' }}
                                                                >
                                                                    <DeleteIcon fontSize="inherit" />
                                                                </IconButton>
                                                            </>
                                                        ) : (
                                                            <Typography variant="body2" color="textSecondary">{t('noFileUploaded', { defaultValue: 'No file uploaded' })}</Typography>
                                                        )}
                                                    </Box>
                                                ),
                                                endAdornment: (
                                                    <Box position="relative">
                                                        <Button
                                                            variant="contained"
                                                            component="label"
                                                            color="primary"
                                                            startIcon={<UploadFileIcon />}
                                                        >
                                                            {t('upload', { defaultValue: 'Upload' })}
                                                            <input
                                                                type="file"
                                                                hidden
                                                                accept=".pdf,.doc,.docx,.txt"
                                                                onChange={handleLetterOfRecommendationChange}
                                                            />
                                                        </Button>
                                                    </Box>
                                                ),
                                            }}
                                            InputLabelProps={{
                                                shrink: true,
                                            }}
                                        />
                                    </FormControl>
                                </Grid>
                            )}
                            {formRequirements.isPersonalStatementRequired && (
                                <Grid item xs={12} sm={6}>
                                    <TextField
                                        fullWidth
                                        label={t('personalStatement', { defaultValue: 'Personal Statement' })}
                                        name="personalStatement"
                                        value={formik.values.personalStatement}
                                        onChange={formik.handleChange}
                                        onBlur={formik.handleBlur}
                                        error={formik.touched.personalStatement && Boolean(formik.errors.personalStatement)}
                                        helperText={formik.touched.personalStatement && formik.errors.personalStatement}
                                    />
                                </Grid>
                            )}
                            <FileUpload error={setError} handleFileChange={handleFileChange} />
                            <Grid item xs={12}>
                                <LoadingButton
                                    color="primary"
                                    variant="contained"
                                    type="submit"
                                    loading={loading}
                                    loadingPosition="start"
                                    startIcon={<UploadFileIcon />}
                                >
                                    {t('submitApplication', { defaultValue: 'Submit Application' })}
                                </LoadingButton>
                            </Grid>
                        </>
                    )}
                </Grid>
            </form>
        </Box>
    );
};

export default ApplicationForm;
