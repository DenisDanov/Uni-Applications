package bg.duosoft.uniapplicationdemo.models.entities.nomenclatures;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "degree_types", schema = "uni_applications")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class DegreeTypeEntity implements Serializable {

    @Id
    @Column(name = "degree_type")
    private String degreeType;

    @Column(name = "degree_description")
    private String degreeDescription;

}
