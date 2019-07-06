package me.perol.blake.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
@Data
@Entity
@Table(name = "POST")
@JsonIgnoreProperties("hibernateLazyInitializer")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    private User user;
    @Column(nullable = false)
    private String title;

    @Type(type = "text")
    private String content;
    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Tag> tags = new HashSet<>();

    @Column(length = 32)
    private String name;

    private ZonedDateTime date;

}
