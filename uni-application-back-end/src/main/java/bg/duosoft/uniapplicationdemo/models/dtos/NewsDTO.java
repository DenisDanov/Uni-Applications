package bg.duosoft.uniapplicationdemo.models.dtos;

import bg.duosoft.uniapplicationdemo.models.dtos.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@EqualsAndHashCode(callSuper = true)
public class NewsDTO extends BaseDTO<Integer> implements Serializable {
    private Integer id;
    private String authorUsername;
    private Timestamp creationTime;
    private String newsHeader;
    private String newsText;
}
