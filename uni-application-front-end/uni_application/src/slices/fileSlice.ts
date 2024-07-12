import {createSlice, PayloadAction} from '@reduxjs/toolkit';
import {RootState} from "../store";

interface FileState {
    selectedFiles: File[];
    selectedFileNames: string[];
    fileError: string | null;
}

const initialState: FileState = {
    selectedFiles: [],
    selectedFileNames: [],
    fileError: null,
};

const fileSlice = createSlice({
    name: 'file',
    initialState,
    reducers: {
        addFiles: (state, action: PayloadAction<{ files: File[]; fileNames: string[] }>) => {
            state.selectedFiles = [...state.selectedFiles, ...action.payload.files];
            state.selectedFileNames = [...state.selectedFileNames, ...action.payload.fileNames];
        },
        setFileError: (state, action: PayloadAction<string | null>) => {
            state.fileError = action.payload;
        },
        clearFiles: (state) => {
            state.selectedFiles = [];
            state.selectedFileNames = [];
        },
    },
});

export const {addFiles, setFileError, clearFiles} = fileSlice.actions;
export default fileSlice.reducer;
export const selectSelectedFileNames = (state: RootState) => state.file.selectedFileNames;
export const selectSelectedFiles = (state: RootState) => state.file.selectedFiles;
