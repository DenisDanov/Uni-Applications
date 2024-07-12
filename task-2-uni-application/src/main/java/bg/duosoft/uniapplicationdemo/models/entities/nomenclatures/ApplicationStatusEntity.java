package bg.duosoft.uniapplicationdemo.models.entities.nomenclatures;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "application_statuses", schema = "uni_applications")
@EqualsAndHashCode
public class ApplicationStatusEntity implements Serializable {

    @Id
    @Column(name = "application_status")
    private String applicationStatus;

    @Column(name = "application_description")
    private String applicationDescription;
}
