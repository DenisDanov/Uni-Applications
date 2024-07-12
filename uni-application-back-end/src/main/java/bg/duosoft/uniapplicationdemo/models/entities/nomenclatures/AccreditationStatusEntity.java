package bg.duosoft.uniapplicationdemo.models.entities.nomenclatures;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "accreditation_statuses", schema = "uni_applications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AccreditationStatusEntity implements Serializable {
    @Id
    @Column(name = "accreditation_type")
    private String accreditationType;

    @Column(name = "accreditation_description")
    private String accreditationDescription;
}
