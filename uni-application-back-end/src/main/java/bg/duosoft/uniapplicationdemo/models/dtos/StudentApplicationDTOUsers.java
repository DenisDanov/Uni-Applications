package bg.duosoft.uniapplicationdemo.models.dtos;

import lombok.*;

import java.util.List;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentApplicationDTOUsers extends StudentApplicationDTO {
    private String specialtyName;

    private String facultyName;

    private List<FileMetadataDTO> applicationFiles;
}
