import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { axiosClientDefault } from '../axios/axiosClient';
import { Faculty } from '../types/Faculty';

interface FacultiesState {
    faculties: Faculty[];
    status: 'idle' | 'loading' | 'failed';
    error: string | null;
}

const initialState: FacultiesState = {
    faculties: [],
    status: 'idle',
    error: null,
};

export const fetchFaculties = createAsyncThunk('faculties/fetchFaculties', async () => {
    const response = await axiosClientDefault.get('/faculty');
    return response.data;
});

const facultiesSlice = createSlice({
    name: 'faculties',
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(fetchFaculties.pending, (state) => {
                state.status = 'loading';
            })
            .addCase(fetchFaculties.fulfilled, (state, action) => {
                state.status = 'idle';
                state.faculties = action.payload;
            })
            .addCase(fetchFaculties.rejected, (state, action) => {
                state.status = 'failed';
                state.error = action.error.message ?? null;
            });
    },
});

export default facultiesSlice.reducer;
