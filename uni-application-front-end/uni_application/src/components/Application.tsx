import React, {useState, useEffect, ChangeEvent} from 'react';
import {
    MenuItem,
    Select,
    TextField,
    Button,
    Grid,
    Box,
    Typography,
    Alert,
    FormControl,
    FormHelperText
} from '@mui/material';
import {useFormik} from 'formik';
import {Specialty} from "../types/Specialty";
import {useDispatch, useSelector} from "react-redux";
import {addFiles, clearFiles, selectSelectedFileNames, selectSelectedFiles} from "../slices/fileSlice";
import {AppDispatch, RootState} from "../store";
import FileUpload from "./FileUpload";
import {validationSchemaApplication} from "../types/validationSchema";
import {fetchFaculties} from '../slices/facultySlice';
import {getSpecialtiesByFaculty, submitApplication} from "../axios/requests";

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
    const dispatch = useDispatch<AppDispatch>();
    const {faculties} = useSelector((state: RootState) => state.faculties);
    const [selectedFacultyId, setSelectedFacultyId] = useState<number | null>(null);
    const specialties = useFetchSpecialties(selectedFacultyId);
    const selectedFiles = useSelector(selectSelectedFiles);
    const selectedFileNames = useSelector(selectSelectedFileNames);

    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);

    const [formRequirements, setFormRequirements] = useState({
        isLanguageProficiencyRequired: false,
        isLetterOfRecommendationRequired: false,
        isPersonalStatementRequired: false,
    });

    useEffect(() => {
        dispatch(fetchFaculties());
    }, [dispatch]);

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
            letterOfRecommendation: '',
            personalStatement: ''
        },
        validationSchema: validationSchemaApplication(formRequirements),
        onSubmit: (values) => {
            console.log(values);

            if (selectedFiles.length === 0) {
                setError("Please upload files first.");
                setTimeout(() => setError(null), 5000);
                return;
            }

            setError(null);
            const {specialty, facultyName, ...dataValues} = values;

            const formDataValues = {
                ...dataValues,
                applicationSentDate: new Date(),
                applicationStatus: {
                    applicationStatus: 'PENDING',
                    applicationDescription: "Application under review by admissions committee"
                }
            };

            const formData = new FormData();
            selectedFiles.forEach((file, index) => formData.append(`file${index}`, file));
            formData.append('studentApplicationCreateDTO', JSON.stringify(formDataValues));
            formData.append('specialtyName', values.specialty.specialtyName);
            formData.append('facultyName', values.facultyName);

            submitApplication(formData)
                .then(() => {
                    setSuccess("Application submitted successfully");
                    setTimeout(() => setSuccess(null), 5000);
                    formik.resetForm();
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
                    setError(err.errors?.[0]?.message || "An error occurred.");
                    setTimeout(() => setError(null), 5000);
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
                dispatch(addFiles({files: filteredFiles, fileNames: filteredFiles.map(file => file.name)}));
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
            formik.values.letterOfRecommendation = '';
        }
        if (!specialty.specialtyRequirement.languageProficiencyTestMinResult) {
            formik.values.languageProficiencyTestResult = '';
        }

        formik.setFieldValue('specialtyId', specialtyId);
        formik.setFieldValue('specialty', specialty);
    };

    return (
        <Box p={2}>
            <Typography variant="h4" mb={2}>Student Application Form</Typography>
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
                                <MenuItem value="" disabled>Select Faculty</MenuItem>
                                {faculties.map(faculty => (
                                    <MenuItem key={faculty.id} value={faculty.id}>{faculty.facultyName}</MenuItem>
                                ))}
                            </Select>
                            {formik.touched.facultyId && formik.errors.facultyId && (
                                <FormHelperText>{formik.errors.facultyId}</FormHelperText>
                            )}
                        </FormControl>
                    </Grid>
                    {selectedFacultyId !== null && specialties.length > 0 && (
                        <Grid item xs={12}>
                            <FormControl fullWidth
                                         error={formik.touched.specialtyId && Boolean(formik.errors.specialtyId)}>
                                <Select
                                    labelId="specialty-label"
                                    value={formik.values.specialtyId === 0 ? '' : formik.values.specialtyId}
                                    // @ts-ignore
                                    onChange={handleSpecialtyChange}
                                    displayEmpty
                                    name="specialtyId"
                                >
                                    <MenuItem value="" disabled>Select Specialty</MenuItem>
                                    {specialties.map(specialty => (
                                        <MenuItem key={specialty.id}
                                                  value={specialty.id}>{specialty.specialtyName}</MenuItem>
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
                                    label="Application Description"
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
                                    label="Average Grade"
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
                                        label="Language Proficiency Test Result"
                                        name="languageProficiencyTestResult"
                                        type="number"
                                        value={formik.values.languageProficiencyTestResult}
                                        onChange={formik.handleChange}
                                        onBlur={formik.handleBlur}
                                        error={formik.touched.languageProficiencyTestResult && Boolean(formik.errors.languageProficiencyTestResult)}
                                        helperText={formik.touched.languageProficiencyTestResult && formik.errors.languageProficiencyTestResult}
                                    />
                                </Grid>
                            )}
                            <Grid item xs={12} sm={6}>
                                <TextField
                                    fullWidth
                                    label="Standardized Test Result"
                                    name="standardizedTestResult"
                                    type="number"
                                    value={formik.values.standardizedTestResult}
                                    onChange={formik.handleChange}
                                    onBlur={formik.handleBlur}
                                    error={formik.touched.standardizedTestResult && Boolean(formik.errors.standardizedTestResult)}
                                    helperText={formik.touched.standardizedTestResult && formik.errors.standardizedTestResult}
                                />
                            </Grid>
                            {formRequirements.isLetterOfRecommendationRequired && (
                                <Grid item xs={12} sm={6}>
                                    <TextField
                                        fullWidth
                                        label="Letter of Recommendation"
                                        name="letterOfRecommendation"
                                        value={formik.values.letterOfRecommendation}
                                        onChange={formik.handleChange}
                                        onBlur={formik.handleBlur}
                                        error={formik.touched.letterOfRecommendation && Boolean(formik.errors.letterOfRecommendation)}
                                        helperText={formik.touched.letterOfRecommendation && formik.errors.letterOfRecommendation}
                                    />
                                </Grid>
                            )}
                            {formRequirements.isPersonalStatementRequired && (
                                <Grid item xs={12} sm={6}>
                                    <TextField
                                        fullWidth
                                        label="Personal Statement"
                                        name="personalStatement"
                                        value={formik.values.personalStatement}
                                        onChange={formik.handleChange}
                                        onBlur={formik.handleBlur}
                                        error={formik.touched.personalStatement && Boolean(formik.errors.personalStatement)}
                                        helperText={formik.touched.personalStatement && formik.errors.personalStatement}
                                    />
                                </Grid>
                            )}
                            <FileUpload handleFileChange={handleFileChange}/>
                            <Grid item xs={12}>
                                <Button color="primary" variant="contained" type="submit">
                                    Submit Application
                                </Button>
                            </Grid>
                        </>
                    )}
                </Grid>
            </form>
        </Box>
    );
};

export default ApplicationForm;
