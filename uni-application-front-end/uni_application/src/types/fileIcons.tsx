import PictureAsPdfIcon from "@mui/icons-material/PictureAsPdf";
import ImageIcon from "@mui/icons-material/Image";
import DescriptionIcon from "@mui/icons-material/Description";
import InsertDriveFileIcon from "@mui/icons-material/InsertDriveFile";
import React from "react";

export const getFileIcon = (fileType: string) => {
    switch (fileType) {
        case 'application/pdf':
            return <PictureAsPdfIcon/>;
        case 'image/png':
        case 'image/jpeg':
        case 'image/jpg':
            return <ImageIcon/>;
        case 'text/plain':
            return <DescriptionIcon/>;
        default:
            return <InsertDriveFileIcon/>;
    }
};
