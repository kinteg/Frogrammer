package com.kinteg.frogrammer.db.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "post")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.FullPost.class)
    private Long id;

    @Column(name = "post_title")
    @NotNull(message = "Require title isn't null.")
    @NotEmpty(message = "Pleas provide a title.")
    @JsonView(Views.FullPost.class)
    private String title;

    @Column(name = "main_text")
    @NotNull(message = "Require mainText isn't null.")
    @NotEmpty(message = "Pleas provide a mainText.")
    @JsonView(Views.FullPost.class)
    private String mainText;

    @Column(name = "preview")
    @NotNull(message = "Require preview isn't null.")
    @NotEmpty(message = "Pleas provide a preview.")
    @JsonView(Views.FullPost.class)
    private String preview;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonView(Views.FullPost.class)
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "post_tag",
            joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    @JsonView(Views.FullPost.class)
    private Set<Tag> tags = new HashSet<>();

    @CreatedDate
    @Column(name = "created")
    @JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss")
    @JsonView(Views.FullPost.class)
    private Date created;

    @LastModifiedDate
    @Column(name = "updated")
    @JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss")
    @JsonView(Views.FullPost.class)
    private Date updated;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

}
