import React from "react";
import {
    Box,
    List,
    ListItem,
    ListItemIcon,
    ListItemText,
    IconButton,
    Typography,
    Collapse,
} from "@mui/material";
import { getFileIcon } from "./fileIcons";
import { handleFileDownload } from "../types/fileDownloads";
import { ExpandMore, ExpandLess } from "@mui/icons-material";
import { useTranslation } from 'react-i18next';

interface AttachedFilesMenuProps {
    application: any;
    setExpanded: (expanded: boolean) => void;
}

const AttachedFilesMenu: React.FC<AttachedFilesMenuProps> = ({ application, setExpanded }) => {
    const { t, i18n } = useTranslation();
    const isBgLanguage = i18n.language === 'bg'; // Check if the current language is Bulgarian

    const handleExpandClick = () => {
        setExpanded(!application.expanded); // Toggle expanded state
    };

    const getTranslatedText = (key: string, fallback: string) => {
        return isBgLanguage ? t(key) : fallback;
    };

    return (
        <Box mt={2}>
            <ListItem button onClick={handleExpandClick}>
                <ListItemIcon>
                    {application.expanded ? <ExpandLess /> : <ExpandMore />}
                </ListItemIcon>
                <ListItemText>
                    <Typography variant="h6">
                        {getTranslatedText('attachedFiles', 'Attached Files')}
                    </Typography>
                </ListItemText>
            </ListItem>
            <Collapse in={application.expanded} timeout="auto" unmountOnExit>
                <List component="div" disablePadding>
                    {application.applicationFiles?.map((file: any, fileIndex: number) => (
                        <ListItem key={fileIndex}>
                            <ListItemIcon>
                                {getFileIcon(file.fileType)}
                            </ListItemIcon>
                            <ListItemText>
                                <IconButton style={{ fontSize: "1.1rem" }}
                                            onClick={() =>
                                                handleFileDownload(
                                                    `${application.username} - Specialty - ${application.specialtyId} - Faculty - ${application.facultyId}`,
                                                    file.fileName
                                                )
                                            }
                                            color="primary"
                                >
                                    {file.fileName}
                                </IconButton>
                            </ListItemText>
                        </ListItem>
                    ))}
                </List>
            </Collapse>
        </Box>
    );
};

export default AttachedFilesMenu;
