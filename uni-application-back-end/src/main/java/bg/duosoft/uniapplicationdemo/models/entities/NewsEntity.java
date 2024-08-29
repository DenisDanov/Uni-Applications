package bg.duosoft.uniapplicationdemo.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "news", schema = "uni_applications_v2")
public class NewsEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "author_username", nullable = false)
    private String authorUsername;

    @Column(name = "creation_time", nullable = false)
    private Timestamp creationTime;

    @Column(name = "news_header", nullable = false, length = 1000)
    private String newsHeader;

    @Column(name = "news_text", nullable = false, columnDefinition = "TEXT")
    private String newsText;
}
