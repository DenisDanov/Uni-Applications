package bg.duosoft.uniapplicationdemo.services;

import bg.duosoft.uniapplicationdemo.models.dtos.FileMetadataDTO;
import bg.duosoft.uniapplicationdemo.services.impl.MinioService;
import io.minio.*;
import io.minio.messages.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MinioServiceTest {

    @Mock
    private MinioClient minioClient;

    private MinioService minioService;

    @BeforeEach
    void setUp() {
        minioService = new MinioService();
        minioService.setMinioClient(minioClient);
    }

    @Test
    void testUploadFile() throws Exception {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream("content".getBytes()));
        when(file.getOriginalFilename()).thenReturn("file.txt");
        when(file.getContentType()).thenReturn("text/plain");

        minioService.uploadFile(file, "app123");

        verify(minioClient).putObject(any(PutObjectArgs.class));
    }

    @Test
    void testListFiles() throws Exception {
        Item item = mock(Item.class);
        when(item.objectName()).thenReturn("app123/file.txt");

        Result<Item> result = new Result<>(item);
        Iterable<Result<Item>> results = Collections.singletonList(result);

        when(minioClient.listObjects(any(ListObjectsArgs.class))).thenReturn(results);
        when(minioClient.getPresignedObjectUrl(any(GetPresignedObjectUrlArgs.class)))
                .thenReturn("http://presignedurl");

        StatObjectResponse statResponse = mock(StatObjectResponse.class);
        when(statResponse.contentType()).thenReturn("text/plain");
        when(minioClient.statObject(any(StatObjectArgs.class))).thenReturn(statResponse);

        List<FileMetadataDTO> resultList = minioService.listFiles("app123");

        assertEquals(1, resultList.size());
        FileMetadataDTO metadata = resultList.get(0);
        assertEquals("file.txt", metadata.getFileName());
        assertEquals("text/plain", metadata.getFileType());
        assertEquals("http://presignedurl", metadata.getUrl());
    }

    @Test
    void testGetPresignedUrl() throws Exception {
        when(minioClient.getPresignedObjectUrl(any(GetPresignedObjectUrlArgs.class)))
                .thenReturn("http://presignedurl");

        String result = minioService.getPresignedUrl("app123/file.txt");

        assertEquals("http://presignedurl", result);
    }

    @Test
    void testGetFileContentType() throws Exception {
        StatObjectResponse statResponse = mock(StatObjectResponse.class);
        when(statResponse.contentType()).thenReturn("text/plain");
        when(minioClient.statObject(any(StatObjectArgs.class))).thenReturn(statResponse);

        String result = minioService.getFileContentType("app123/file.txt");

        assertEquals("text/plain", result);
    }

    @Test
    void testGetFile() throws Exception {
        // Create an InputStream with the desired content
        InputStream mockInputStream = new ByteArrayInputStream("content".getBytes());

        // Mock GetObjectResponse to return the InputStream
        GetObjectResponse mockGetObjectResponse = mock(GetObjectResponse.class);
        when(mockGetObjectResponse.readAllBytes()).thenReturn("content".getBytes());

        // Configure the MinioClient mock to return the GetObjectResponse
        when(minioClient.getObject(any(GetObjectArgs.class))).thenReturn(mockGetObjectResponse);

        // Call the method under test
        InputStream result = minioService.getFile("app123/file.txt");

        // Verify the result
        assertNotNull(result);
        assertEquals("content", new String(result.readAllBytes()));

        // Ensure the InputStream is closed after reading
        result.close();
    }


    @Test
    void testDeleteFolderByUsernamePrefix() throws Exception {
        Item item1 = mock(Item.class);
        when(item1.objectName()).thenReturn("user123/file1.txt");
        Item item2 = mock(Item.class);
        when(item2.objectName()).thenReturn("user123 - file2.txt");

        Result<Item> result1 = new Result<>(item1);
        Result<Item> result2 = new Result<>(item2);
        Iterable<Result<Item>> results = List.of(result1, result2);

        when(minioClient.listObjects(any(ListObjectsArgs.class))).thenReturn(results);

        minioService.deleteFolderByUsernamePrefix("user123");

        verify(minioClient, times(1)).removeObject(any(RemoveObjectArgs.class));
    }

    @Test
    void testDeleteFolderByFullName() throws Exception {
        Item item = mock(Item.class);
        when(item.objectName()).thenReturn("folder123/file.txt");

        Result<Item> result = new Result<>(item);
        Iterable<Result<Item>> results = Collections.singletonList(result);

        when(minioClient.listObjects(any(ListObjectsArgs.class))).thenReturn(results);

        minioService.deleteFolderByFullName("folder123");

        verify(minioClient).removeObject(any(RemoveObjectArgs.class));
    }

    @Test
    void testDeleteFileByName() throws Exception {
        minioService.deleteFileByName("folder123", "file.txt");

        verify(minioClient).removeObject(any(RemoveObjectArgs.class));
    }
}
