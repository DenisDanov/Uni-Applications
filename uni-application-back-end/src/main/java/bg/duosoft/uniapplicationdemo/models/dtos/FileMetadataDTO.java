package bg.duosoft.uniapplicationdemo.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileMetadataDTO implements Serializable {
    private String fileName;
    private String fileType;
    private String url;
}
