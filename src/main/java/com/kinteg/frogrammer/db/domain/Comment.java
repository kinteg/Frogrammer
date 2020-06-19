package com.kinteg.frogrammer.db.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "comment")
@Data
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.FullPost.class)
    private Long id;

    @Column(name = "text")
    @NotNull(message = "Require text isn't null.")
    @NotEmpty(message = "Pleas provide a text.")
    @JsonView(Views.FullPost.class)
    private String text;

    @Column(name = "parent_id")
    @JsonView(Views.FullPost.class)
    private Long dependedId;

    @Column(name = "parent_path")
    @JsonView(Views.FullPost.class)
    private Long dependedPath;

    @Column(name = "has_child")
    @JsonView(Views.FullPost.class)
    private Boolean hasChild;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonView(Views.FullPost.class)
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private Post post;

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