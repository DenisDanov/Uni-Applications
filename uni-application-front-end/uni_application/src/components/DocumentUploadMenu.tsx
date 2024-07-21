import React from 'react';
import { Box, Button, Typography, IconButton, Drawer, List, ListItem, ListItemText, AppBar, Toolbar } from '@mui/material';
import UploadFileIcon from '@mui/icons-material/UploadFile';
import DeleteIcon from '@mui/icons-material/Delete';

// @ts-ignore
const DocumentUploadMenu = ({ formik, formRequirements, handleLetterOfRecommendationChange, getFileIcon }) => {
    return (
        <Drawer
            variant="permanent"
            anchor="right"
            sx={{ width: 300, flexShrink: 0, '& .MuiDrawer-paper': { width: 300, boxSizing: 'border-box' } }}
        >
            <AppBar position="static">
                <Toolbar>
                    <Typography variant="h6" noWrap>
                        Upload Documents
                    </Typography>
                </Toolbar>
            </AppBar>
            <Box mt={2} p={2}>
                <List>
                    {formRequirements.isLetterOfRecommendationRequired && (
                        <ListItem>
                            <Box display="flex" flexDirection="column" alignItems="start" width="100%">
                                <Typography variant="body1">Letter of Recommendation</Typography>
                                <Button
                                    variant="contained"
                                    component="label"
                                    color="primary"
                                    startIcon={<UploadFileIcon />}
                                >
                                    Upload
                                    <input
                                        type="file"
                                        hidden
                                        accept=".pdf,.doc,.docx,.txt"
                                        onChange={handleLetterOfRecommendationChange}
                                    />
                                </Button>
                                {formik.values.letterOfRecommendation && (
                                    <Box mt={1} display="flex" alignItems="center" width="100%">
                                        {getFileIcon(formik.values.letterOfRecommendation.name)}
                                        <Typography ml={1} noWrap>{formik.values.letterOfRecommendation.name}</Typography>
                                        <IconButton
                                            aria-label="delete"
                                            size="small"
                                            onClick={() => formik.setFieldValue('letterOfRecommendation', null)}
                                        >
                                            <DeleteIcon fontSize="inherit" />
                                        </IconButton>
                                    </Box>
                                )}
                            </Box>
                        </ListItem>
                    )}
                    {/* Add other document requirements here in a similar fashion */}
                </List>
            </Box>
        </Drawer>
    );
};

export default DocumentUploadMenu;
