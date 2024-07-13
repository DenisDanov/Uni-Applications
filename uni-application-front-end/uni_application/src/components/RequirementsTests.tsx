import React, {useEffect, useRef, useState} from 'react';
import moment from 'moment';
import {
    Button,
    Card,
    CardContent,
    Typography,
    Collapse,
    Box,
    Alert,
    Radio,
    RadioGroup,
    FormControlLabel,
    FormControl,
    FormLabel,
    Container,
} from '@mui/material';
import {axiosClientDefault} from '../axios/axiosClient';
import {useKeycloak} from '../keycloak';
import {StudentsRequirementsResultsDTO} from '../types/StudentRequirementsResultsDTO';
import {languageProficiencyQuestions} from '../types/LanguageProficiencyTestQuestions';

const RequirementsTests: React.FC = () => {
    const [results, setResults] = useState<StudentsRequirementsResultsDTO | null>(null);
    const [languageTestOpen, setLanguageTestOpen] = useState(false);
    const [standardTestOpen, setStandardTestOpen] = useState(false);
    const [timeRemaining, setTimeRemaining] = useState<number | null>(null);
    const timerRef = useRef<NodeJS.Timeout | null>(null);
    const [answers, setAnswers] = useState<number[]>([]);
    const [testStartTime, setTestStartTime] = useState<number | null>(null);
    const {keycloak} = useKeycloak();

    useEffect(() => {
        axiosClientDefault
            .get<StudentsRequirementsResultsDTO>(`/students-requirements-results/${keycloak.tokenParsed?.preferred_username}`)
            .then((response) => {
                setResults(response.data);
                setAnswers(new Array(languageProficiencyQuestions.length).fill(-1));
            })
            .catch((error) => {
                console.error('Error fetching results:', error);
            });
    }, [keycloak.tokenParsed?.preferred_username]);

    useEffect(() => {
        window.addEventListener('beforeunload', saveTestState);
        return () => {
            window.removeEventListener('beforeunload', saveTestState);
            if (timerRef.current) {
                clearTimeout(timerRef.current);
            }
        };
    }, []);

    useEffect(() => {
        if (timeRemaining !== null && (languageTestOpen || standardTestOpen)) {
            if (timeRemaining > 0) {
                timerRef.current = setTimeout(() => {
                    setTimeRemaining(timeRemaining - 1);
                }, 1000);
            } else {
                handleTimeExpired();
            }
        }
    }, [timeRemaining, languageTestOpen, standardTestOpen]);

    const saveTestState = (newAnswers: any) => {
        if ((languageTestOpen || standardTestOpen) && testStartTime !== null) {
            axiosClientDefault
                .post('/test/save-test-state', {
                    username: keycloak.tokenParsed?.preferred_username,
                    answers: newAnswers,
                    testStartTime: testStartTime,
                })
                .then((response) => {
                    console.log('Test state saved:', response.data);
                })
                .catch((error) => {
                    console.error('Error saving test state:', error);
                });
        }
    };

    const handleStartTest = (testType: 'language' | 'standard') => {
        const startTime = moment().unix();
        setTestStartTime(startTime);
        setTimeRemaining(3599); // 1 hour in seconds
        if (testType === 'language') {
            setLanguageTestOpen(true);
        } else {
            setStandardTestOpen(true);
        }
        // Save startTime and other relevant state to localStorage or sessionStorage
        localStorage.setItem('testStartTime', startTime.toString());
        // You may also save answers or other relevant state here if needed
    };

    useEffect(() => {
        if (languageTestOpen || standardTestOpen) {
            saveTestState([]);
        }
    }, [languageTestOpen, standardTestOpen]);

    const handleTimeExpired = () => {
        setLanguageTestOpen(false);
        setStandardTestOpen(false);
        axiosClientDefault
            .post('/submit-test', {answers})
            .then((response) => {
                console.log('Test submitted:', response.data);
            })
            .catch((error) => {
                console.error('Error submitting test:', error);
            });
    };

    const handleSubmit = () => {
        if (timeRemaining === 0) {
            return;
        }
        axiosClientDefault
            .post('/submit-test', {answers})
            .then((response) => {
                console.log('Test submitted:', response.data);
            })
            .catch((error) => {
                console.error('Error submitting test:', error);
            });
    };

    const handleAnswerChange = (questionIndex: number, answerIndex: number) => {
        const newAnswers = [...answers];
        newAnswers[questionIndex] = answerIndex;
        setAnswers(newAnswers);
        saveTestState(newAnswers);
    };

    const formatTime = (seconds: number) => {
        const duration = moment.duration(seconds, 'seconds');
        return `${duration.hours()}:${duration.minutes()}:${duration.seconds()}`;
    };

    return (
        <Container sx={{display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '100vh'}}>
            <Box sx={{width: '100%', maxWidth: 600}}>
                {results && (
                    <>
                        {results.languageProficiencyTestResult === null && (
                            <Card sx={{mb: 2}}>
                                <CardContent>
                                    <Typography variant="h5" align="center">Language Proficiency Test</Typography>
                                    {!languageTestOpen ? (
                                        <Box sx={{textAlign: 'center'}}>
                                            <Typography variant="body2">Time to complete: 1 hr</Typography>
                                            <Button variant="contained" color="primary"
                                                    onClick={() => handleStartTest('language')}>
                                                Start Test
                                            </Button>
                                        </Box>
                                    ) : (
                                        <Collapse in={languageTestOpen}>
                                            <Box>
                                                <Typography variant="body2" align="center">Time
                                                    Remaining: {formatTime(timeRemaining!)}</Typography>
                                                <FormControl component="fieldset" fullWidth>
                                                    {languageProficiencyQuestions.map((q, index) => (
                                                        <Box key={index} sx={{mt: 2}}>
                                                            <FormLabel component="legend">{q.question}</FormLabel>
                                                            <RadioGroup
                                                                value={answers[index]}
                                                                onChange={(e) => handleAnswerChange(index, parseInt(e.target.value))}
                                                            >
                                                                {q.options.map((option, i) => (
                                                                    <FormControlLabel key={i} value={i}
                                                                                      control={<Radio/>}
                                                                                      label={option}/>
                                                                ))}
                                                            </RadioGroup>
                                                        </Box>
                                                    ))}
                                                </FormControl>
                                                <Box sx={{textAlign: 'center', mt: 2}}>
                                                    <Button
                                                        variant="contained"
                                                        color="secondary"
                                                        onClick={handleSubmit}
                                                        disabled={timeRemaining === 0}
                                                    >
                                                        Submit
                                                    </Button>
                                                    {timeRemaining === 0 &&
                                                        <Alert severity="error">Time expired</Alert>}
                                                </Box>
                                            </Box>
                                        </Collapse>
                                    )}
                                </CardContent>
                            </Card>
                        )}
                        {results.standardizedTestResult === null && (
                            <Card>
                                <CardContent>
                                    <Typography variant="h5" align="center">Standardized Test</Typography>
                                    {!standardTestOpen ? (
                                        <Box sx={{textAlign: 'center'}}>
                                            <Typography variant="body2">Time to complete: 1 hr</Typography>
                                            <Button variant="contained" color="primary"
                                                    onClick={() => handleStartTest('standard')}>
                                                Start Test
                                            </Button>
                                        </Box>
                                    ) : (
                                        <Collapse in={standardTestOpen}>
                                            <Box>
                                                <Typography variant="body2" align="center">Time
                                                    Remaining: {formatTime(timeRemaining!)}</Typography>
                                                {/* Test questions go here */}
                                                <Box sx={{textAlign: 'center', mt: 2}}>
                                                    <Button variant="contained" color="secondary" onClick={handleSubmit}
                                                            disabled={timeRemaining === 0}>
                                                        Submit
                                                    </Button>
                                                    {timeRemaining === 0 &&
                                                        <Alert severity="error">Time expired</Alert>}
                                                </Box>
                                            </Box>
                                        </Collapse>
                                    )}
                                </CardContent>
                            </Card>
                        )}
                    </>
                )}
            </Box>
        </Container>
    );
};

export default RequirementsTests;
