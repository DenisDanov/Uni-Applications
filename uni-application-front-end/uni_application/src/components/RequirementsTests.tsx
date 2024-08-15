import React, {useEffect, useState} from 'react';
import moment from 'moment';
import Countdown, {zeroPad} from 'react-countdown';
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
import {entranceExamQuestions} from '../types/StandartTestQuestions';
import {useTranslation} from 'react-i18next';

const RequirementsTests: React.FC = () => {
    const {t, i18n} = useTranslation();
    const [results, setResults] = useState<StudentsRequirementsResultsDTO | null>(null);
    const [languageTestOpen, setLanguageTestOpen] = useState(false);
    const [standardTestOpen, setStandardTestOpen] = useState(false);
    const [deadline, setDeadline] = useState<Date | null>(null);
    const [answers, setAnswers] = useState<number[]>([]);
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

    const checkExistingTestState = () => {
        axiosClientDefault
            .get(`/test/get/${keycloak.tokenParsed?.preferred_username}`)
            .then((response) => {
                const {answers, testStartTime, testName} = response.data;
                if (answers !== null && testStartTime !== null && testName !== null) {
                    setTestName(testName);
                    setAnswers(answers);

                    const startTime = moment.unix(testStartTime).toDate();
                    const deadline = moment(startTime).add(1, 'hour').subtract(1, 'second').toDate();
                    setDeadline(deadline);

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

    const saveTestState = (newAnswers: number[], testOpen: boolean, deadline: Date | null, testName: string | null) => {
        if (testOpen && deadline !== null) {
            axiosClientDefault
                .post('/test/save-test-state', {
                    username: keycloak.tokenParsed?.preferred_username,
                    answers: newAnswers,
                    testStartTime: moment(deadline).unix(),
                    testName: testName,
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
        const startTime = new Date();
        setAnswers(new Array(languageProficiencyQuestions.length).fill(-1));
        const deadline = moment(startTime).add(1, 'hour').subtract(1, 'second').toDate();
        setDeadline(deadline);
        setTestName(testType);
        if (testType === 'language') {
            setLanguageTestOpen(true);
        } else {
            setStandardTestOpen(true);
        }
        saveTestState(new Array(languageProficiencyQuestions.length).fill(-1), true, deadline, testType);
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
                setResults(response.data);
                setSubmissionSuccess(i18n.language === 'bg' ? t('testSubmitted') : 'Test submitted successfully.');
                setTimeout(() => setSubmissionSuccess(null), 5000);
            })
            .catch((error) => {
                console.error('Error submitting test:', error);
                setSubmissionError((i18n.language === 'bg' ? t('testSubmissionError') : 'Error submitting test: ') + error.message);
                setTimeout(() => setSubmissionError(null), 5000);
            });
    };

    const handleSubmit = () => {
        setSubmissionError(null);

        // Check if all questions are answered
        const unansweredQuestions = answers.includes(-1);
        if (unansweredQuestions) {
            setSubmissionError(i18n.language === 'bg' ? t('unansweredQuestionsError') : 'Please answer all questions before submitting.');
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
                setSubmissionSuccess(i18n.language === 'bg' ? t('testSubmitted') : 'Test submitted successfully.');
                if (languageTestOpen) {
                    setLanguageTestOpen(false);
                } else {
                    setStandardTestOpen(false);
                }
                setTimeout(() => setSubmissionSuccess(null), 5000);
            })
            .catch((error) => {
                console.error('Error submitting test:', error);
                setSubmissionError((i18n.language === 'bg' ? t('testSubmissionError') : 'Error submitting test: ') + error.message);
                setTimeout(() => setSubmissionError(null), 5000);
            });
    };

    const computeTestResult = (answers: number[]) => {
        let correctAnswers = 0;
        const questions = testName === 'language' ? languageProficiencyQuestions : entranceExamQuestions;

        questions.forEach((question, index) => {
            if (answers[index] === question.correctAnswer) {
                correctAnswers += 1;
            }
        });

        return correctAnswers;
    };

    const handleAnswerChange = (questionIndex: number, answerIndex: number) => {
        const newAnswers = [...answers];
        newAnswers[questionIndex] = answerIndex;
        setAnswers(newAnswers);
        saveTestState(newAnswers, true, deadline, testName);
    };

    const renderCountdown = ({minutes, seconds, completed}: any) => {
        if (completed) {
            handleTimeExpired();
            return <Alert
                severity="error">{i18n.language === 'bg' ? t('timeExpired') : 'Time expired. Please submit your test.'}</Alert>;
        } else {
            return (
                <Typography variant="body2" align="center">
                    {i18n.language === 'bg' ? t('timeRemaining') : 'Time remaining'}: {zeroPad(minutes)}:{zeroPad(seconds)}
                </Typography>
            );
        }
    };

    const getQuestionText = (question: any) => {
        return i18n.language === 'bg' ? question.bg : question.en;
    };

    const getOptionText = (option: any) => {
        return i18n.language === 'bg' ? option.bg : option.en;
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
                                    <Typography variant="h5"
                                                align="center">{i18n.language === 'bg' ? t('languageProficiencyTest') : 'Language Proficiency Test'}</Typography>
                                    {!languageTestOpen ? (
                                        <Box sx={{textAlign: 'center'}}>
                                            <Typography
                                                variant="body2">{i18n.language === 'bg' ? t('timeToComplete') : 'Time to complete: 1 hr'}</Typography>
                                            <Button id={'start-language-test'} variant="contained" color="primary"
                                                    disabled={standardTestOpen}
                                                    onClick={() => handleStartTest('language')}>
                                                {i18n.language === 'bg' ? t('startTest') : 'Start Test'}
                                            </Button>
                                        </Box>
                                    ) : (
                                        <Collapse in={languageTestOpen}>
                                            <Box>
                                                <Countdown
                                                    date={deadline!}
                                                    renderer={renderCountdown}
                                                />
                                                <FormControl component="fieldset" fullWidth>
                                                    {languageProficiencyQuestions.map((q, index) => (
                                                        <Box key={index} sx={{mt: 2}}>
                                                            <FormLabel
                                                                component="legend">{q.question}</FormLabel>
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
                                                        // @ts-ignore
                                                        disabled={deadline && new Date() > deadline}
                                                    >
                                                        {i18n.language === 'bg' ? t('submit') : 'Submit'}
                                                    </Button>
                                                    {deadline && new Date() > deadline &&
                                                        <Alert
                                                            severity="error">{i18n.language === 'bg' ? t('timeExpired') : 'Time expired. Please submit your test.'}</Alert>}
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
                                    <Typography variant="h5"
                                                align="center">{i18n.language === 'bg' ? t('standardizedTest') : 'Standardized Test'}</Typography>
                                    {!standardTestOpen ? (
                                        <Box sx={{textAlign: 'center'}}>
                                            <Typography
                                                variant="body2">{i18n.language === 'bg' ? t('timeToComplete') : 'Time to complete: 1 hr'}</Typography>
                                            <Button id={'start-standard-test'} variant="contained" color="primary"
                                                    disabled={languageTestOpen}
                                                    onClick={() => handleStartTest('standard')}>
                                                {i18n.language === 'bg' ? t('startTest') : 'Start Test'}
                                            </Button>
                                        </Box>
                                    ) : (
                                        <Collapse in={standardTestOpen}>
                                            <Box>
                                                <Countdown
                                                    date={deadline!}
                                                    renderer={renderCountdown}
                                                />
                                                <FormControl component="fieldset" fullWidth>
                                                    {entranceExamQuestions.map((q, index) => (
                                                        <Box key={index} sx={{mt: 2}}>
                                                            <FormLabel
                                                                component="legend">{getQuestionText(q.question)}</FormLabel>
                                                            <RadioGroup
                                                                value={answers[index]}
                                                                onChange={(e) => handleAnswerChange(index, parseInt(e.target.value))}
                                                            >
                                                                {q.options.map((option, i) => (
                                                                    <FormControlLabel key={i} value={i}
                                                                                      control={<Radio/>}
                                                                                      label={getOptionText(option)}/>
                                                                ))}
                                                            </RadioGroup>
                                                        </Box>
                                                    ))}
                                                </FormControl>
                                                <Box sx={{textAlign: 'center', mt: 2}}>
                                                    <Button variant="contained" color="secondary" onClick={handleSubmit}
                                                        // @ts-ignore
                                                            disabled={deadline && new Date() > deadline}>
                                                        {i18n.language === 'bg' ? t('submit') : 'Submit'}
                                                    </Button>
                                                    {deadline && new Date() > deadline &&
                                                        <Alert
                                                            severity="error">{i18n.language === 'bg' ? t('timeExpired') : 'Time expired. Submitting your test.'}</Alert>}
                                                </Box>
                                            </Box>
                                        </Collapse>
                                    )}
                                </CardContent>
                            </Card>
                        )}
                        {(results.languageProficiencyTestResult !== null && results.standardizedTestResult !== null) &&
                            <Alert
                                severity="success">{i18n.language === 'bg' ? t('completedAllTests') : 'You have successfully completed all tests.'}</Alert>
                        }
                    </>
                )}
            </Box>
        </Container>
    );
};

export default RequirementsTests;
