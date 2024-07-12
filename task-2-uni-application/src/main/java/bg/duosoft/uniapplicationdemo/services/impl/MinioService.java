package bg.duosoft.uniapplicationdemo.services.impl;

import bg.duosoft.uniapplicationdemo.models.dtos.FileMetadataDTO;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MinioService {

    private final MinioClient minioClient;

    public MinioService() {
        minioClient = MinioClient.builder()
                .endpoint("http://localhost:9000")
                .credentials("minioadmin", "minioadmin")
                .build();
    }

    public void uploadFile(MultipartFile file, String applicationID) throws Exception {
        try (InputStream inputStream = file.getInputStream()) {
            String objectName = applicationID + "/" + file.getOriginalFilename();

            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket("uni-app-files")
                    .object(objectName)
                    .contentType(file.getContentType())
                    .stream(inputStream, inputStream.available(), -1)
                    .build();

            minioClient.putObject(args);
        }
    }

    public List<FileMetadataDTO> listFiles(String applicationID) throws Exception {
        List<FileMetadataDTO> fileList = new ArrayList<>();

        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket("uni-app-files").prefix(applicationID + "/").build());

        for (Result<Item> result : results) {
            Item item = result.get();
            String url = getPresignedUrl(item.objectName());
            String fileName = item.objectName().substring(item.objectName().lastIndexOf("/") + 1);
            String fileType = getFileContentType(item.objectName());
            fileList.add(new FileMetadataDTO(fileName, fileType, url));
        }

        return fileList;
    }

    public String getPresignedUrl(String objectName) throws Exception {
        return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket("uni-app-files")
                .object(objectName)
                .expiry(60 * 60 * 24) // Link valid for 24 hours
                .extraQueryParams(Collections.singletonMap("response-content-disposition", "attachment; filename=\"" + objectName + "\""))
                .build());
    }

    private String getFileContentType(String objectName) throws Exception {
        StatObjectResponse stat = minioClient.statObject(StatObjectArgs.builder()
                .bucket("uni-app-files")
                .object(objectName)
                .build());
        return stat.contentType();
    }

    public InputStream getFile(String objectName) throws Exception {
        return minioClient.getObject(GetObjectArgs.builder()
                .bucket("uni-app-files")
                .object(objectName)
                .build());
    }

    public void deleteFolderByUsernamePrefix(String username) throws Exception {
        List<Item> itemsToDelete = new ArrayList<>();

        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket("uni-app-files")
                        .prefix(username)
                        .recursive(true)
                        .build());

        for (Result<Item> result : results) {
            Item item = result.get();
            if (item.objectName().startsWith(username + " ") || item.objectName().startsWith(username + " - ")) {
                itemsToDelete.add(item);
            }
        }

        for (Item itemToDelete : itemsToDelete) {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket("uni-app-files")
                            .object(itemToDelete.objectName())
                            .build());
        }
    }

    public void deleteFolderByFullName(String folderName) throws Exception {
        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder()
                .bucket("uni-app-files")
                .prefix(folderName + "/")
                .recursive(true)
                .build());

        for (Result<Item> result : results) {
            Item item = result.get();
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket("uni-app-files")
                    .object(item.objectName())
                    .build());
        }
    }

    public void deleteFileByName(String folderName, String fileName) throws Exception {
        String objectName = folderName + "/" + fileName;
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket("uni-app-files")
                .object(objectName)
                .build());
    }
}
