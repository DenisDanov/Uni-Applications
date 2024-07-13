package bg.duosoft.uniapplicationdemo.models.entities.nomenclatures;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "access_levels", schema = "uni_applications")
public class AccessLevelEntity implements Serializable {

    @Id
    @Column(name = "access_type")
    private String accessType;

    @Column(name = "access_description")
    private String accessDescription;
}
