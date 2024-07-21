import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { RootState } from '../store';

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
            state.fileError = null; // Clear any existing error when files are added
        },
        removeFile: (state, action: PayloadAction<string>) => {
            const index = state.selectedFileNames.indexOf(action.payload);
            if (index !== -1) {
                state.selectedFileNames.splice(index, 1);
                state.selectedFiles.splice(index, 1);
            }
        },
        setFileError: (state, action: PayloadAction<string | null>) => {
            state.fileError = action.payload;
        },
        clearFiles: (state) => {
            state.selectedFiles = [];
            state.selectedFileNames = [];
            state.fileError = null; // Clear any existing error when files are cleared
        },
    },
});

export const { addFiles, setFileError, clearFiles, removeFile } = fileSlice.actions;
export default fileSlice.reducer;
export const selectSelectedFileNames = (state: RootState) => state.file.selectedFileNames;
export const selectFileError = (state: RootState) => state.file.fileError;
export const selectSelectedFiles = (state: RootState) => state.file.selectedFiles;
