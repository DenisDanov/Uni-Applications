import React from "react";
import { useSelector, useDispatch } from "react-redux";
import { selectSelectedFileNames, removeFile, addFiles } from "../slices/fileSlice";
import { Description, PictureAsPdf, Delete } from "@mui/icons-material";
import { Box, Button, Grid, List, ListItem, ListItemIcon, ListItemText, IconButton, Paper, Typography } from "@mui/material";

const FileUpload: React.FC<{
    handleFileChange: (event: React.ChangeEvent<HTMLInputElement>) => void,
    error?: (message: string | null) => void
}> = ({ handleFileChange, error }) => {
    const dispatch = useDispatch();
    const selectedFileNames = useSelector(selectSelectedFileNames);

    const allowedExtensions = ['pdf', 'doc', 'docx', 'txt'];

    const getFileIcon = (fileName: string) => {
        const extension = fileName.split('.').pop()?.toLowerCase();
        switch (extension) {
            case 'pdf':
                return <PictureAsPdf />;
            case 'txt':
            case 'doc':
            case 'docx':
                return <Description />;
            default:
                return null;
        }
    };

    const handleDeleteFile = (fileName: string) => {
        dispatch(removeFile(fileName));
    };

    const handleFileUpload = (event: React.ChangeEvent<HTMLInputElement>) => {
        if (event.target.files) {
            const newFiles = Array.from(event.target.files);
            const filteredFiles = newFiles.filter(file => {
                const fileExtension = file.name.split('.').pop()?.toLowerCase();
                return fileExtension && allowedExtensions.includes(fileExtension);
            });

            if (filteredFiles.length !== newFiles.length) {
                // Call the error function passed from the parent
                if (error) {
                    error('Unsupported file type. Please upload a PDF, DOC, DOCX, or TXT file.');
                    setTimeout(() => error(null), 5000);
                }
            } else {
                // Clear the error if no unsupported files
                if (error) {
                    error(null);
                }
            }

            if (filteredFiles.length > 0) {
                const fileNames = filteredFiles.map(file => file.name);
                dispatch(addFiles({ files: filteredFiles, fileNames }));
            }
        }
    };

    return (
        <Grid item xs={12}>
            <input
                id="file-input"
                accept=".txt,.pdf,.doc,.docx"
                type="file"
                onChange={handleFileUpload}
                multiple
                hidden
            />
            <label htmlFor="file-input">
                <Button variant="contained" component="span">Upload additional documents</Button>
            </label>
            {selectedFileNames.length > 0 ? (
                <Box mt={2}>
                    <Typography variant="body1">Selected Files:</Typography>
                    <List component={Paper}>
                        {selectedFileNames.map((fileName, index) => (
                            <Box key={index} mb={1}>
                                <ListItem
                                    secondaryAction={
                                        <IconButton edge="end" aria-label="delete" onClick={() => handleDeleteFile(fileName)}>
                                            <Delete />
                                        </IconButton>
                                    }
                                >
                                    <ListItemIcon>{getFileIcon(fileName)}</ListItemIcon>
                                    <ListItemText primary={fileName} />
                                </ListItem>
                            </Box>
                        ))}
                    </List>
                </Box>
            ) : (
                <Typography style={{ marginTop: '5px' }} variant="body1">No uploaded files.</Typography>
            )}
        </Grid>
    );
};

export default FileUpload;
