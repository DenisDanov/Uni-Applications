package bg.duosoft.uniapplicationdemo.util;

import org.springframework.http.MediaType;

public class DetermineFileTypeUtil {

    public static String detectFileType(byte[] fileData) {
        if (fileData.length >= 4) {
            // Check for PDF signature
            if (fileData[0] == '%' && fileData[1] == 'P' && fileData[2] == 'D' && fileData[3] == 'F') {
                return MediaType.APPLICATION_PDF_VALUE;
            }

            // Check for DOCX (ZIP) signature
            if (fileData[0] == 'P' && fileData[1] == 'K' && fileData[2] == 0x03 && fileData[3] == 0x04) {
                return MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            // Check for DOC signature
            if (fileData[0] == (byte) 0xD0 && fileData[1] == (byte) 0xCF && fileData[2] == (byte) 0x11 && fileData[3] == (byte) 0xE0) {
                return MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }
        }

        // Default to text if no other types match
        return MediaType.TEXT_PLAIN_VALUE;
    }

    // Utility method to get file extension from content type
    public static String getFileExtension(String contentType) {
        switch (contentType) {
            case MediaType.APPLICATION_PDF_VALUE:
                return "pdf";
            case MediaType.APPLICATION_OCTET_STREAM_VALUE:
                // Further differentiate between DOC and DOCX if needed
                // Default to DOCX for simplicity here
                return "docx";
            case MediaType.TEXT_PLAIN_VALUE:
                return "txt";
            default:
                return "bin";
        }
    }
}
