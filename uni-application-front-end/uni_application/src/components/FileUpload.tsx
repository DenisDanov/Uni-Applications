import React from "react";
import {useSelector} from "react-redux";
import {selectSelectedFileNames} from "../slices/fileSlice";
import {Description, PictureAsPdf} from "@mui/icons-material";
import {Box, Button, Grid, List, ListItem, ListItemIcon, ListItemText, Paper, Typography} from "@mui/material";

const FileUpload: React.FC<{
    handleFileChange: (event: React.ChangeEvent<HTMLInputElement>) => void
}> = ({handleFileChange}) => {
    const selectedFileNames = useSelector(selectSelectedFileNames);

    const getFileIcon = (fileName: string) => {
        const extension = fileName.split('.').pop()?.toLowerCase();
        switch (extension) {
            case 'pdf':
                return <PictureAsPdf/>;
            case 'txt':
            case 'doc':
            case 'docx':
                return <Description/>;
            default:
                return null;
        }
    };

    return (
        <Grid item xs={12}>
            <input
                id="file-input"
                accept=".txt,.pdf,.doc,.docx"
                type="file"
                onChange={handleFileChange}
                multiple
                hidden
            />
            <label htmlFor="file-input">
                <Button variant="contained" component="span">Upload Files</Button>
            </label>
            {selectedFileNames.length > 0 ? (
                <Box mt={2}>
                    <Typography variant="body1">Selected Files:</Typography>
                    <List component={Paper}>
                        {selectedFileNames.map((fileName, index) => (
                            <Box key={index} mb={1}>
                                <ListItem>
                                    <ListItemIcon>{getFileIcon(fileName)}</ListItemIcon>
                                    <ListItemText primary={fileName}/>
                                </ListItem>
                            </Box>
                        ))}
                    </List>
                </Box>
            ) : (
                <Typography style={{marginTop: '5px'}} variant="body1">No uploaded files.</Typography>
            )}
        </Grid>
    );
};

export default FileUpload;