import {axiosClientDefault} from "../axios/axiosClient";

export const handleFileDownload = async (folder: string, fileName: string) => {
    try {
        const response = await axiosClientDefault.get(
            `/files/download-file`,
            {
                params: {objectName: `${folder}/${fileName}`},
                responseType: 'blob', // Important
            }
        );

        // URL for the blob object
        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', fileName);
        document.body.appendChild(link);
        link.click();
        link.remove();
    } catch (error) {
        console.error('Failed to download file:', error);
    }
};