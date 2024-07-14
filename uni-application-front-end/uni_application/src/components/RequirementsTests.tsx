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
    const [testName, setTestName] = useState<string | null>(null);
    const [submissionError, setSubmissionError] = useState<string | null>(null);
    const [submissionSuccess, setSubmissionSuccess] = useState<string | null>(null);

    useEffect(() => {
        axiosClientDefault
            .get<StudentsRequirementsResultsDTO>(`/students-requirements-results/${keycloak.tokenParsed?.preferred_username}`)
            .then((response) => {
                setResults(response.data);
                if (response.data.languageProficiencyTestResult === null || response.data.standardizedTestResult === null) {
                    checkExistingTestState();
                }
            })
            .catch((error) => {
                console.error('Error fetching results:', error);
            });
    }, [keycloak.tokenParsed?.preferred_username]);

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

    const checkExistingTestState = () => {
        axiosClientDefault
            .get(`/test/get/${keycloak.tokenParsed?.preferred_username}`)
            .then((response) => {
                const {answers, testStartTime, testName} = response.data;
                if (answers !== null && testStartTime !== null && testName !== null) {
                    setTestName(testName);
                    setAnswers(answers);
                    setTestStartTime(testStartTime);

                    const timeElapsed = moment().unix() - testStartTime;
                    const remainingTime = 3600 - timeElapsed;
                    setTimeRemaining(remainingTime > 0 ? remainingTime : 0);

                    if (testName === 'language') {
                        setLanguageTestOpen(true);
                    } else if (testName === 'standard') {
                        setStandardTestOpen(true);
                    }
                }
            })
            .catch((error) => {
                console.error('Error fetching test state:', error);
            });
    };

    const saveTestState = (newAnswers: number[], testOpen: boolean, startTime: number | null, testName: string | null) => {
        if (testOpen && startTime !== null) {
            axiosClientDefault
                .post('/test/save-test-state', {
                    username: keycloak.tokenParsed?.preferred_username,
                    answers: newAnswers,
                    testStartTime: startTime,
                    testName: testName,
                    secondsLeft: timeRemaining
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
        setAnswers(new Array(languageProficiencyQuestions.length).fill(-1));
        setTestStartTime(startTime);
        setTimeRemaining(3599); // 1 hour in seconds
        setTestName(testType);
        if (testType === 'language') {
            setLanguageTestOpen(true);
        } else {
            setStandardTestOpen(true);
        }
        saveTestState(new Array(languageProficiencyQuestions.length).fill(-1), true, startTime, testType);
    };

    const handleTimeExpired = () => {
        setLanguageTestOpen(false);
        setStandardTestOpen(false);
        axiosClientDefault
            .post('/students-requirements-results/submit-test', {
                username: keycloak.tokenParsed?.preferred_username,
                testName: testName,
                result: computeTestResult(answers)
            })
            .then((response) => {
                console.log('Test submitted:', response.data);
            })
            .catch((error) => {
                console.error('Error submitting test:', error);
            });
    };

    const handleSubmit = () => {
        setSubmissionError(null);

        // Check if all questions are answered
        const unansweredQuestions = answers.includes(-1);
        if (unansweredQuestions) {
            setSubmissionError("You must answer all the questions before submitting the test.");
            setTimeout(() => setSubmissionError(null), 5000);
            return;
        }

        axiosClientDefault
            .post('/students-requirements-results/submit-test', {
                username: keycloak.tokenParsed?.preferred_username,
                testName: testName,
                answers: answers,
                result: computeTestResult(answers)
            })
            .then((response) => {
                console.log('Test submitted:', response.data);
                setResults(response.data);
                setLanguageTestOpen(false);
                setSubmissionSuccess("Test submitted, you can check your result from the profile page.")
                setTimeout(() => setSubmissionSuccess(null), 5000);
            })
            .catch((error) => {
                console.error('Error submitting test:', error);
                setSubmissionError("Error submitting test: " + error);
                setTimeout(() => setSubmissionError(null), 5000);
            });
    };

    const computeTestResult = (answers: number[]) => {
        let correctAnswers = 0;
        if (testName === 'language') {
            languageProficiencyQuestions.forEach((question, index) => {
                if (answers[index] === question.correctAnswer) {
                    correctAnswers += 1;
                }
            });
        }
        return correctAnswers;
    };

    const handleAnswerChange = (questionIndex: number, answerIndex: number) => {
        const newAnswers = [...answers];
        newAnswers[questionIndex] = answerIndex;
        setAnswers(newAnswers);
        saveTestState(newAnswers, true, testStartTime, testName);
    };

    const formatTime = (seconds: number) => {
        const duration = moment.duration(seconds, 'seconds');
        return `${duration.minutes() < 10 ? 0 : ''}${duration.minutes()}:${duration.seconds() < 10 ? 0 : ''}${duration.seconds()}`;
    };

    return (
        <Container sx={{display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '100vh'}}>
            <Box sx={{width: '100%', maxWidth: 600}}>
                {submissionError && (
                    <Alert severity="error">{submissionError}</Alert>
                )}
                {submissionSuccess && (
                    <Alert severity="success">{submissionSuccess}</Alert>
                )}
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
                                                    disabled={standardTestOpen}
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
                                                    disabled={languageTestOpen}
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
