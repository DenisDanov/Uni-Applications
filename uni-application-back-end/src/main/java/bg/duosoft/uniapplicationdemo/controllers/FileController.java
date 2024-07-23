package bg.duosoft.uniapplicationdemo.controllers;

import bg.duosoft.uniapplicationdemo.models.dtos.SpecialtyDTO;
import bg.duosoft.uniapplicationdemo.models.dtos.SpecialtySubjectInfoDTO;
import bg.duosoft.uniapplicationdemo.models.dtos.StudentApplicationDTOUsers;
import bg.duosoft.uniapplicationdemo.services.SpecialtyService;
import bg.duosoft.uniapplicationdemo.services.SpecialtySubjectInfoService;
import bg.duosoft.uniapplicationdemo.services.StudentApplicationService;
import bg.duosoft.uniapplicationdemo.services.TeacherService;
import bg.duosoft.uniapplicationdemo.services.impl.MinioService;
import com.lowagie.text.DocumentException;
import freemarker.template.TemplateException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.*;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static bg.duosoft.uniapplicationdemo.util.DetermineFileTypeUtil.detectFileType;
import static bg.duosoft.uniapplicationdemo.util.DetermineFileTypeUtil.getFileExtension;

@RestController
@RequestMapping("api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final MinioService minioService;

    private final FreeMarkerConfigurationFactoryBean freeMarkerConfig;

    private final SpecialtyService specialtyService;

    private final SpecialtySubjectInfoService specialtySubjectInfoService;

    private final StudentApplicationService studentApplicationService;

    private final TeacherService teacherService;

    @GetMapping("/download-file/{username}/{facultyName}/{courseName}")
    public ResponseEntity<InputStreamResource> downloadFileTest(@PathVariable String username, @PathVariable String facultyName, @PathVariable String courseName) {
        try {
            // Retrieve the byte array from the database
            byte[] fileData = studentApplicationService.findByStudentUsernameAndFacultyAndSpecialty(username, facultyName, courseName)
                    .get(0)
                    .getLetterOfRecommendation();

            // Detect the file type
            String contentType = detectFileType(fileData);

            // Convert the byte array to InputStream
            InputStream inputStream = new ByteArrayInputStream(fileData);

            // Set the headers for file download
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"recommendation_letter." + getFileExtension(contentType) + "\"");
            headers.add("X-File-Type", contentType);  // Custom header to include the file type

            // Return the file as an InputStreamResource
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(new InputStreamResource(inputStream));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/download-file")
    public ResponseEntity<InputStreamResource> downloadFile(@RequestParam String objectName) {
        try {
            InputStream inputStream = minioService.getFile(objectName);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + objectName + "\"");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(inputStream));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/application/pdf")
    @ResponseBody
    public void getApplicationPdf(@RequestBody StudentApplicationDTOUsers application, HttpServletResponse response) throws IOException, DocumentException, TemplateException {
        Map<String, Object> model = new HashMap<>();
        model.put("application", application);

        generatePdf("Application.ftl", model, "Application.pdf", response);
    }

    @PostMapping("/application/program")
    @ResponseBody
    public void getApplicationProgram(@RequestBody SpecialtyDTO specialtyDTO, HttpServletResponse response) throws IOException, DocumentException, TemplateException {
        Map<String, Object> model = new HashMap<>();
        specialtyDTO = specialtyService.getById(specialtyDTO.getId());
        model.put("specialtyDTO", specialtyDTO);

        SpecialtyDTO finalSpecialtyDTO = specialtyDTO;
        List<SpecialtySubjectInfoDTO> matchedSubjectsInfo = specialtySubjectInfoService.getAll().stream()
                .filter(subjectInfo ->
                        finalSpecialtyDTO.getSubjects().stream()
                                .anyMatch(subject ->
                                        subject.getId().equals(subjectInfo.getSubjectId()) &&
                                                finalSpecialtyDTO.getId().equals(subjectInfo.getSpecialtyId())))
                .toList();

        model.put("teacherInfo", teacherService.getAll().stream()
                .filter(teacherDTO ->
                        teacherDTO.getSubjects().stream().anyMatch(
                                teacherSubject -> finalSpecialtyDTO.getSubjects().stream().anyMatch(
                                        specialtySubject -> specialtySubject.getId().equals(teacherSubject.getId())
                                )
                        )
                )
                .collect(Collectors.toList()));
        model.put("subjectInfo", matchedSubjectsInfo);

        generatePdf("ApplicationProgram.ftl", model, "ApplicationProgram.pdf", response);
    }

    private void generatePdf(String templateName, Map<String, Object> model, String outputFileName, HttpServletResponse response) throws IOException, DocumentException, TemplateException {
        // Process template into HTML
        String htmlContent = FreeMarkerTemplateUtils.processTemplateIntoString(
                freeMarkerConfig.createConfiguration().getTemplate(templateName), model);

        // Convert HTML to PDF
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();

        // Write to response
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        renderer.createPDF(baos);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=" + outputFileName);
        response.setContentLength(baos.size());
        baos.writeTo(response.getOutputStream());
    }
}
