package bg.duosoft.uniapplicationdemo.models.entities.nomenclatures;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "roles", schema = "uni_applications")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoleEntity implements Serializable {

    @Id
    @Column(name = "role_name", insertable = false, updatable = false)
    private String role;

    @Column(name = "role_description")
    private String roleDescription;
}
