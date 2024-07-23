import { configureStore } from '@reduxjs/toolkit';
import fileReducer from './slices/fileSlice';
import facultyReducer from './slices/facultySlice';
import languageReducer from './slices/languageSlice';

const store = configureStore({
    reducer: {
        faculties: facultyReducer,
        file: fileReducer,
        language: languageReducer,
    },
    middleware: (getDefaultMiddleware) =>
        getDefaultMiddleware({
            serializableCheck: {
                ignoredActions: ['file/addFiles', 'file/setFileError', 'file/clearFiles'],
                ignoredPaths: ['file.selectedFiles'],
            },
        }),
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
export default store;
