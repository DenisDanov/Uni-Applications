import React, {useEffect, useState} from 'react';
import {Container, CircularProgress, Box, Alert} from '@mui/material';
import LogTable from '../components/LogTable';
import {Log} from "../types/Log";
import {axiosClientDefault} from "../axios/axiosClient";
import '../adminDashboard.css';

const AdminDashboard: React.FC = () => {
    const [logs, setLogs] = useState<Log[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        // Fetch logs from your API
        axiosClientDefault.get('/logs')
            .then(response => {
                if (!response.data) {
                    throw new Error('Network response was not ok');
                }
                // @ts-ignore
                // TODO FIX THIS TO NOT BREAK OTHER PAGES LAYOUT WHEN LEAVING THIS PAGE
                document.querySelector(`.css-1qsxih2`).style.maxWidth = '100%';
                return response.data;
            })
            .then(data => {
                setLogs(data);
                setLoading(false);
            })
            .catch(error => {
                setError(error.message);
                setLoading(false);
            });
    }, []);

    return (
        <Container>
            {loading ? (
                <Box display="flex" justifyContent="center" alignItems="center" height="100px">
                    <CircularProgress/>
                </Box>
            ) : error ? (
                <Alert severity="error">{error}</Alert>
            ) : (
                <LogTable logTableName={"Applications Activity Logs"} logs={logs}/>
            )}
        </Container>
    );
};

export default AdminDashboard;
