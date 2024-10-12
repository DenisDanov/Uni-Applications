import React, {useState, useMemo} from 'react';
import {DataGridPro, GridColDef} from '@mui/x-data-grid-pro';
import {Box, Paper, Typography, FormControl, InputLabel, Select, MenuItem} from '@mui/material';
import {useTranslation} from 'react-i18next';
import {Log} from '../types/Log';
import {format} from 'date-fns';

const formatDate = (date: string | Date) => {
    if (!date) return 'N/A';
    const parsedDate = new Date(date);
    return format(parsedDate, 'MMMM d yyyy, HH:mm:ss');
};

interface LogTableProps {
    logs: Log[];
    logTableName: string;
}

const LogTable: React.FC<LogTableProps> = ({logs, logTableName}) => {
    const {t} = useTranslation();
    const [filter, setFilter] = useState<string>('All');

    const handleFilterChange = (event: React.ChangeEvent<{ value: unknown }>) => {
        setFilter(event.target.value as string);
    };

    // Filter logs based on selected filter
    const filteredLogs = useMemo(() => {
        switch (filter) {
            case 'Creation':
                return logs.filter(log => log.processedStatus === null && log.deletedByUsername === null);
            case 'Update':
                return logs.filter(log => log.processedStatus !== null && log.deletedByUsername === null);
            case 'Deletion':
                return logs.filter(log => log.deletedByUsername !== null && log.deletedTime !== null);
            case 'All':
            default:
                return logs;
        }
    }, [logs, filter]);

    // Translate the column headers
    const columns: GridColDef[] = [
        {field: 'submittedByUsername', headerName: t('submittedByUsername', {defaultValue: 'Submitted By'}), flex: 1},
        {field: 'facultyName', headerName: t('card.faculty', {defaultValue: 'Faculty Name'}), flex: 1.5},
        {field: 'specialtyName', headerName: t('card.specialty', {defaultValue: 'Specialty Name'}), flex: 1.5},
        {
            field: 'submissionTime',
            headerName: t('submissionTime', {defaultValue: 'Submission Time'}),
            flex: 1.5,
            renderCell: (params) => formatDate(params.value),
        },
        {
            field: 'processedStatus',
            headerName: t(`applicationStatusHeader`, {defaultValue: 'Processed Status'}),
            flex: 1,
            renderCell: (params) => t(`applicationStatus.${params.value}`, {defaultValue: params.value ? params.value : 'N/A'}),
        },
        {
            field: 'processedByUsername',
            headerName: t('processedByUsername', {defaultValue: 'Processed By'}),
            flex: 1,
            renderCell: (params) => params.value || 'N/A',
        },
        {
            field: 'processingTime',
            headerName: t('processingTime', {defaultValue: 'Processing Time'}),
            flex: 1.5,
            renderCell: (params) => formatDate(params.value),
        },
        {
            field: 'deletedByUsername',
            headerName: t('deletedByUsername', {defaultValue: 'Deleted By'}),
            flex: 1,
            renderCell: (params) => params.value || 'N/A',
        },
        {
            field: 'deletedTime',
            headerName: t('deletedTime', {defaultValue: 'Deleted Time'}),
            flex: 1.5,
            renderCell: (params) => formatDate(params.value),
        },
    ];

    // Transform logs to fit DataGrid format
    const rows = useMemo(() => filteredLogs.map((log, index) => ({
        id: index,
        ...log,
        facultyName: t(`faculties.${log.facultyName}`, {defaultValue: log.facultyName}),
        specialtyName: t(`specialties.${log.specialtyName}.title`, {defaultValue: log.specialtyName}),
        processedStatus: t(`applicationStatus.${log.processedStatus}`, {defaultValue: log.processedStatus ? log.processedStatus : 'N/A'}),
    })), [filteredLogs, t]);

    return (
        <Box sx={{width: '100%', height: '100%'}}>
            <Paper elevation={3} sx={{height: '100%', width: '100%'}}>
                <Typography variant="h6" sx={{padding: 2}}>
                    {t('applicationActivityLogs', {defaultValue: logTableName})}
                </Typography>
                <div style={{padding: '10px 10px'}}>
                    <FormControl fullWidth margin="normal">
                        <InputLabel>{t('filterName', {defaultValue: 'Filter'})}</InputLabel>
                        <Select
                            value={filter}
                            // @ts-ignore
                            onChange={handleFilterChange}
                            label={t('filterName', {defaultValue: 'Filter'})}
                        >
                            <MenuItem value="All">{t('All', {defaultValue: 'All'})}</MenuItem>
                            <MenuItem value="Creation">{t('Creation', {defaultValue: 'Creation'})}</MenuItem>
                            <MenuItem value="Update">{t('Update', {defaultValue: 'Update'})}</MenuItem>
                            <MenuItem value="Deletion">{t('Deletion', {defaultValue: 'Deletion'})}</MenuItem>
                        </Select>
                    </FormControl>
                </div>
                <Box sx={{height: '100%', width: '100%'}}>
                    <DataGridPro
                        initialState={{
                            pagination: {
                                paginationModel: {pageSize: 10},
                            },
                        }}
                        pagination
                        pageSizeOptions={[5, 10, 15, 25, 50, 100]}
                        rows={rows}
                        columns={columns}
                        checkboxSelection
                        disableRowSelectionOnClick
                        autoHeight
                        disableColumnMenu
                        disableColumnSelector
                    />
                </Box>
            </Paper>
        </Box>
    );
};

export default LogTable;
