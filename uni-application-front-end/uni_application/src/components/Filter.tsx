import React, { useState } from 'react';
import {
    FormControlLabel,
    Checkbox,
    TextField,
    Button,
    Paper,
    Typography,
    Grid
} from "@mui/material";
import { useTranslation } from 'react-i18next';
import { getReadableLabel } from "../util/FilterLabelName";

export interface FilterConfig {
    name: string;
    type: 'text' | 'number' | 'date' | 'select';
}

interface FilterProps {
    filters: FilterConfig[];
    onSearch: (filters: any) => void;
    filterName: string;
}

const Filter: React.FC<FilterProps> = ({ filterName, filters, onSearch }) => {
    const [filterState, setFilterState] = useState<any>({});
    const [filterValues, setFilterValues] = useState<any>({});
    const { t, i18n } = useTranslation(); // Hook to access translation and language

    const isBgLanguage = i18n.language === 'bg'; // Check if the current language is Bulgarian

    const handleFilterChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFilterState({ ...filterState, [e.target.name]: e.target.checked });
    };

    const handleFilterValueChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFilterValues({ ...filterValues, [e.target.name]: e.target.value });
    };

    const handleSearch = () => {
        const activeFilters: any = filters.reduce((acc: any, filter: FilterConfig) => {
            if (filterState[filter.name]) {
                acc[filter.name] = filterValues[filter.name];
            }
            return acc;
        }, {});
        onSearch(activeFilters);
    };

    return (
        <Paper sx={{ p: 2, mb: 2 }}>
            <Typography variant="h6">
                {isBgLanguage ? t('filterName') : filterName}
            </Typography>
            <Grid container spacing={2} alignItems="center">
                {filters.map((filter) => (
                    <Grid item xs={12} md={6} key={filter.name}>
                        <FormControlLabel
                            control={
                                <Checkbox
                                    checked={filterState[filter.name] || false}
                                    onChange={handleFilterChange}
                                    name={filter.name}
                                />
                            }
                            label={isBgLanguage ? t(getReadableLabel(filter.name)) : getReadableLabel(filter.name)}
                        />
                    </Grid>
                ))}
            </Grid>
            <Grid container spacing={2}>
                {filters.map((filter) =>
                    filterState[filter.name] ? (
                        <Grid item xs={12} md={6} key={filter.name}>
                            <TextField
                                sx={{ mt: 2 }}
                                type={filter.type}
                                label={isBgLanguage ? t(getReadableLabel(filter.name)) : getReadableLabel(filter.name)}
                                name={filter.name}
                                value={filterValues[filter.name] || ""}
                                onChange={handleFilterValueChange}
                                fullWidth
                            />
                        </Grid>
                    ) : null
                )}
            </Grid>
            <Button variant="contained" color="primary" onClick={handleSearch} sx={{ mt: 4 }}>
                {isBgLanguage ? t('filterSearch') : 'Search'}
            </Button>
        </Paper>
    );
};

export default Filter;
