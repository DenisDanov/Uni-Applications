import React from 'react';
import {
    Grid,
    Card,
    CardContent,
    Typography,
    Box,
    Button,
    Divider,
    Chip, AccordionSummary, Accordion, AccordionDetails, List, ListItem, ListItemAvatar, Avatar, ListItemText
} from '@mui/material';
import {Specialty} from "../types/Specialty";
import {useTranslation} from "react-i18next";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import {
    AccessTime,
    CalendarToday,
    CreditScore, Equalizer, Language, ListAlt,
    Note,
    RateReview,
    School,
    Star,
    Verified,
    Work
} from "@mui/icons-material";

interface SpecialtyCardProps {
    specialty: Specialty;
    handleApplyClick: (facultyID: number, specialtyID: number) => void;
}

const getTranslatedText = (key: string, fallback: string, isBgLanguage: boolean, t: (key: string) => string) => {
    return isBgLanguage ? t(key) : fallback;
};

const SpecialtyCard: React.FC<SpecialtyCardProps> = ({specialty, handleApplyClick}) => {
    const {t, i18n} = useTranslation();
    const isBgLanguage = i18n.language === 'bg';

    return (
        <Grid className={"specialties-cards"} item xs={12} sm={6} md={4} lg={3}>
            <Card
                elevation={4}
                sx={{
                    maxWidth: 500,
                    margin: 'auto',
                    borderRadius: '16px',
                    overflow: 'hidden',
                    transition: 'transform 0.3s ease-in-out',
                    '&:hover': {
                        transform: 'scale(1.03)',
                        boxShadow: 6
                    }
                }}
            >
                <CardContent>
                    <Typography variant="h5" sx={{fontWeight: 'bold', color: 'primary.main', mb: 2}}>
                        {getTranslatedText(`specialties.${specialty.specialtyName}.title`, specialty.specialtyName, isBgLanguage, t)}
                    </Typography>

                    <Divider sx={{mb: 2, borderBottomWidth: 2}}>
                        <Chip
                            label={getTranslatedText('specialties.programDetails', 'Program Details', isBgLanguage, t)}
                            sx={{
                                backgroundColor: 'primary.main',
                                color: 'common.white',
                                fontWeight: 'bold',
                                fontSize: '1rem',
                                padding: '4px 16px'
                            }}
                        />
                    </Divider>

                    <Typography
                        variant="body1"
                        sx={{
                            display: 'flex',
                            justifyContent: 'flex-start',
                            mb: 1
                        }}
                    >
                        <School sx={{color: 'primary.main', mr: 1}}/>
                        <span>
                            {getTranslatedText('specialties.program', 'Program', isBgLanguage, t)}{": "}
                            {getTranslatedText(`specialties.${specialty.specialtyName}.title`, specialty.specialtyName, isBgLanguage, t)}
                        </span>
                    </Typography>

                    <Typography
                        variant="body2"
                        sx={{
                            display: 'flex',
                            justifyContent: 'flex-start',
                            mb: 1
                        }}
                    >
                        <Star sx={{color: 'primary.main', mr: 1}}/>
                        <span>
                            {getTranslatedText('specialties.degreeType', 'Degree Type', isBgLanguage, t)}{": "}
                            {getTranslatedText(`specialties.${specialty.specialtyName}.degreeType`, specialty.specialtyProgram.degreeType.degreeDescription, isBgLanguage, t)}
                        </span>
                    </Typography>

                    <Typography
                        variant="body2"
                        sx={{
                            display: 'flex',
                            justifyContent: 'flex-start',
                            mb: 1
                        }}
                    >
                        <Verified sx={{color: 'primary.main', mr: 1}}/>
                        <span>
                            {getTranslatedText('specialties.accreditation', 'Accreditation', isBgLanguage, t)}{": "}
                            {getTranslatedText(
                                `specialties.${specialty.specialtyName}.accreditation`,
                                `${specialty.specialtyProgram.accreditationStatus.accreditationType} - ${specialty.specialtyProgram.accreditationStatus.accreditationDescription}`,
                                isBgLanguage,
                                t
                            )}
                        </span>
                    </Typography>

                    <Typography
                        variant="body2"
                        sx={{
                            display: 'flex',
                            justifyContent: 'flex-start',
                            mb: 1
                        }}
                    >
                        <CalendarToday sx={{color: 'primary.main', mr: 1}}/>
                        <span>
                            {getTranslatedText('specialties.programDuration', 'Program Duration', isBgLanguage, t)}{": "}
                            {new Date(specialty.specialtyProgram.startsOn).toLocaleDateString()} {isBgLanguage ? "до" : "to"} {new Date(specialty.specialtyProgram.endsOn).toLocaleDateString()}
                        </span>
                    </Typography>

                    <Typography
                        variant="body2"
                        sx={{
                            display: 'flex',
                            justifyContent: 'flex-start',
                            mb: 2
                        }}
                    >
                        <AccessTime sx={{color: 'primary.main', mr: 1}}/>
                        <span>
                            {getTranslatedText('specialties.duration', 'Duration', isBgLanguage, t)}{": "}
                            {specialty.specialtyProgram.durationDays} {getTranslatedText('specialties.days', 'days', isBgLanguage, t)}
                        </span>
                    </Typography>

                    <Divider sx={{mb: 2, borderBottomWidth: 2}}>
                        <Chip
                            label={getTranslatedText('specialties.requirements', 'Requirements', isBgLanguage, t)}
                            sx={{
                                backgroundColor: 'primary.main',
                                color: 'common.white',
                                fontWeight: 'bold',
                                fontSize: '1rem',
                                padding: '4px 16px'
                            }}
                        />
                    </Divider>
                    <Typography
                        variant="body2"
                        sx={{
                            display: 'flex',
                            justifyContent: 'flex-start',
                            mb: 1
                        }}
                    >
                        <Equalizer sx={{color: 'primary.main', mr: 1}}/>
                        <span>
                            {getTranslatedText('specialties.minGrade', 'Minimum Grade', isBgLanguage, t)}{": "}
                            {specialty.specialtyRequirement.minGrade}
                        </span>
                    </Typography>

                    {specialty.specialtyRequirement.languageProficiencyTestMinResult && (
                        <Typography
                            variant="body2"
                            sx={{
                                display: 'flex',
                                justifyContent: 'flex-start',
                                mb: 1
                            }}
                        >
                            <Language sx={{color: 'primary.main', mr: 1}}/>
                            <span>
                                {getTranslatedText('specialties.languageProficiency', 'Language Proficiency Test Minimum Score', isBgLanguage, t)}{": "}
                                {specialty.specialtyRequirement.languageProficiencyTestMinResult}
                                {isBgLanguage ? ' точки' : " points"}
                            </span>
                        </Typography>
                    )}

                    <Typography
                        variant="body2"
                        sx={{
                            display: 'flex',
                            justifyContent: 'flex-start',
                            mb: 1
                        }}
                    >
                        <ListAlt sx={{color: 'primary.main', mr: 1}}/>
                        <span>
                            {getTranslatedText('specialties.standardizedTest', 'Standardized Test Minimum Score', isBgLanguage, t)}{": "}
                            {specialty.specialtyRequirement.standardizedTestMinResult}
                            {isBgLanguage ? ' точки' : " points"}
                        </span>
                    </Typography>

                    <Typography
                        variant="body2"
                        sx={{
                            display: 'flex',
                            justifyContent: 'flex-start',
                            mb: 1
                        }}
                    >
                        <RateReview sx={{color: 'primary.main', mr: 1}}/>
                        <span>
                            {getTranslatedText('specialties.letterOfRecommendation', 'Letter of Recommendation Required', isBgLanguage, t)}{": "}
                            {specialty.specialtyRequirement.letterOfRecommendationRequired
                                ? getTranslatedText('yes', 'Yes', isBgLanguage, t)
                                : getTranslatedText('no', 'No', isBgLanguage, t)}
                        </span>
                    </Typography>

                    <Typography
                        variant="body2"
                        sx={{
                            display: 'flex',
                            justifyContent: 'flex-start',
                            mb: 1
                        }}
                    >
                        <Note sx={{color: 'primary.main', mr: 1}}/>
                        <span>
                            {getTranslatedText('specialties.personalStatement', 'Personal Statement Required', isBgLanguage, t)}{": "}
                            {specialty.specialtyRequirement.personalStatementRequired
                                ? getTranslatedText('yes', 'Yes', isBgLanguage, t)
                                : getTranslatedText('no', 'No', isBgLanguage, t)}
                        </span>
                    </Typography>

                    <Divider sx={{mb: 2, borderBottomWidth: 2}}>
                        <Chip
                            label={getTranslatedText('specialties.additionalInfo', 'Additional Information', isBgLanguage, t)}
                            sx={{
                                backgroundColor: 'primary.main',
                                color: 'common.white',
                                fontWeight: 'bold',
                                fontSize: '1rem',
                                padding: '4px 16px'
                            }}
                        />
                    </Divider>

                    <Typography
                        variant="body2"
                        sx={{
                            display: 'flex',
                            justifyContent: 'flex-start',
                            mb: 1
                        }}
                    >
                        <Work sx={{color: 'primary.main', mr: 1}}/>
                        <span>
                            {getTranslatedText('specialties.employmentRate', 'Employment Rate', isBgLanguage, t)}{": "}
                            {specialty.employmentRate}%
                        </span>
                    </Typography>

                    <Typography
                        variant="body2"
                        sx={{
                            display: 'flex',
                            justifyContent: 'flex-start',
                            mb: 1
                        }}
                    >
                        <CreditScore sx={{color: 'primary.main', mr: 1}}/>
                        <span>
                            {getTranslatedText('specialties.totalCreditsRequired', 'Total Credits Required', isBgLanguage, t)}{": "}
                            {specialty.totalCreditsRequired}
                        </span>
                    </Typography>

                    <Divider sx={{mb: 2, borderBottomWidth: 2}}>
                        <Chip
                            label={getTranslatedText('specialties.subjectsHeader', 'Subjects', isBgLanguage, t)}
                            sx={{
                                backgroundColor: 'primary.main',
                                color: 'common.white',
                                fontWeight: 'bold',
                                fontSize: '1rem',
                                padding: '4px 16px'
                            }}
                        />
                    </Divider>

                    {specialty.subjects.map(subject => (
                        <Accordion key={subject.id} sx={{mb: 2}}>
                            <AccordionSummary
                                expandIcon={<ExpandMoreIcon/>}
                                aria-controls={`panel-content-${subject.id}`}
                                id={`panel-header-${subject.id}`}
                                sx={{backgroundColor: 'background.paper'}}
                            >
                                <Typography variant="body1" sx={{fontWeight: 'bold'}}>
                                    {getTranslatedText(`specialties.${specialty.specialtyName}.subjects.${subject.subjectName}`, subject.subjectName, isBgLanguage, t)}
                                </Typography>
                            </AccordionSummary>
                            <AccordionDetails sx={{backgroundColor: 'background.default'}}>
                                <Typography variant="body2" sx={{mb: 2}}>
                                    {getTranslatedText(`specialties.${specialty.specialtyName}.subjects.${subject.subjectDescription}`, subject.subjectDescription, isBgLanguage, t)}
                                </Typography>

                                <Divider sx={{mb: 2}}>
                                    <Chip
                                        label={getTranslatedText('specialties.teachersHeader', 'Teachers', isBgLanguage, t)}
                                        sx={{
                                            color: 'common.white',
                                            fontWeight: 'bold',
                                            fontSize: '1rem',
                                            padding: '4px 16px',
                                            backgroundColor: 'primary.main',
                                        }}
                                    />
                                </Divider>

                                <List>
                                    {subject.teachers.map(teacher => (
                                        <ListItem key={teacher.id}>
                                            <ListItemAvatar>
                                                <Avatar sx={{backgroundColor: 'primary.main', color: 'common.white'}}>
                                                    {teacher.teacherName.charAt(0)}
                                                </Avatar>
                                            </ListItemAvatar>
                                            <ListItemText
                                                primary={teacher.teacherName}
                                                sx={{color: 'text.primary'}}
                                            />
                                        </ListItem>
                                    ))}
                                </List>
                            </AccordionDetails>
                        </Accordion>
                    ))}

                    <Box mt={2} display="flex" justifyContent="center">
                        <Button
                            variant="contained"
                            color="primary"
                            sx={{borderRadius: '999px', padding: '8px 24px', textTransform: 'none'}}
                            onClick={() => handleApplyClick(specialty.facultyID, specialty.id)}
                        >
                            {getTranslatedText('specialties.applyButton', 'Apply', isBgLanguage, t)}
                        </Button>
                    </Box>
                </CardContent>
            </Card>
        </Grid>
    )
};

export default SpecialtyCard;
