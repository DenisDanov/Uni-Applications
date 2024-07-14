package bg.duosoft.uniapplicationdemo.controllers;

import bg.duosoft.uniapplicationdemo.models.dtos.SpecialtyDTO;
import bg.duosoft.uniapplicationdemo.models.dtos.SpecialtySubjectInfoDTO;
import bg.duosoft.uniapplicationdemo.models.dtos.StudentApplicationDTOUsers;
import bg.duosoft.uniapplicationdemo.services.SpecialtyService;
import bg.duosoft.uniapplicationdemo.services.SpecialtySubjectInfoService;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final MinioService minioService;

    private final FreeMarkerConfigurationFactoryBean freeMarkerConfig;

    private final SpecialtyService specialtyService;

    private final SpecialtySubjectInfoService specialtySubjectInfoService;

    private final TeacherService teacherService;

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