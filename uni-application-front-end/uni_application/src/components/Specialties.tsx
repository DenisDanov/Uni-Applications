import React, {useState, useEffect} from 'react';
import {Card, CardContent, Divider, Grid, Skeleton,} from '@mui/material';
import {useLocation, useNavigate} from 'react-router-dom';
import {fetchSpecialties} from "../axios/requests";
import {Specialty} from "../types/Specialty";
import SpecialtyCard from "./SpecialtyCard";

const Specialties = () => {
    const [specialties, setSpecialties] = useState<Specialty[]>([]);
    const [filteredSpecialties, setFilteredSpecialties] = useState<Specialty[]>([]);
    const [loading, setLoading] = useState(true);
    const location = useLocation();
    const navigate = useNavigate();

    useEffect(() => {
        const fetchData = async () => {
            try {
                await fetchSpecialties(setSpecialties);
                setLoading(false);
            } catch (err) {
                console.error(err);

                setLoading(false);
            }
        };

        fetchData();
    }, []);

    useEffect(() => {
        const queryParams = new URLSearchParams(location.search);
        const facultyId = queryParams.get('facultyId') as unknown as number;
        const specialtyName = queryParams.get('specialtyName');

        let filtered = specialties;
        if (facultyId) {
            filtered = filtered.filter(specialty => specialty.facultyID == facultyId);
        }

        if (specialtyName) {
            filtered = filtered.filter(specialty => specialty.specialtyName === specialtyName);
        }

        setFilteredSpecialties(filtered);
    }, [specialties, location.search]);

    const handleApplyClick = (facultyId: number, specialtyId: number) => {
        navigate(`/apply?facultyId=${facultyId}&specialtyId=${specialtyId}`);
    };

    if (loading) {
        return (
            <Grid container spacing={2}>
                {Array.from(new Array(6)).map((_, index) => (
                    <Grid item xs={12} sm={6} md={4} lg={3} key={index}>
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
                                <Skeleton variant="text" width="60%" sx={{ fontSize: '1.5rem', mb: 2 }} />
                                <Divider sx={{ mb: 2, borderBottomWidth: 2 }}>
                                    <Skeleton variant="rectangular" width={180} height={40} sx={{ borderRadius: '16px' }} />
                                </Divider>

                                <Skeleton variant="text" width="80%" sx={{ mb: 1 }} />
                                <Skeleton variant="text" width="70%" sx={{ mb: 1 }} />
                                <Skeleton variant="text" width="50%" sx={{ mb: 1 }} />
                                <Skeleton variant="text" width="90%" sx={{ mb: 1 }} />
                                <Skeleton variant="text" width="40%" sx={{ mb: 2 }} />

                                <Divider sx={{ mb: 2, borderBottomWidth: 2 }} />
                                <Skeleton variant="text" width="30%" sx={{ mb: 1 }} />
                                <Skeleton variant="text" width="70%" sx={{ mb: 1 }} />
                                <Skeleton variant="text" width="50%" sx={{ mb: 1 }} />
                                <Skeleton variant="text" width="60%" sx={{ mb: 1 }} />

                                <Divider sx={{ mb: 2, borderBottomWidth: 2 }} />
                                <Skeleton variant="rectangular" width="100%" height={36} sx={{ borderRadius: '999px' }} />
                            </CardContent>
                        </Card>
                    </Grid>
                ))}
            </Grid>
        );
    }

    return (
        <Grid justifyContent={'center'} container spacing={2}>
            {filteredSpecialties.length === 0 ? (
                <div>No Specialties found.</div>
            ) : (
                filteredSpecialties.map((specialty) => (
                    <SpecialtyCard
                        key={specialty.id}
                        specialty={specialty}
                        handleApplyClick={handleApplyClick}
                    />
                ))
            )}
        </Grid>
    );
};

export default Specialties;
