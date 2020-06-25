package com.kinteg.frogrammer.db.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
@EntityListeners(AuditingEntityListener.class)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "text")
    @NotNull(message = "Require text isn't null.")
    @NotEmpty(message = "Pleas provide a text.")
    private String text;

    @Column(name = "parent_id")
    @ColumnDefault(value = "0")
    private Long dependedId;

    @Column(name = "parent_path")
    private Long dependedAuthor;

    @Column(name = "has_child")
    private Boolean hasChild;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private Post post;

    @CreatedDate
    @Column(name = "created")
    @JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss")
    private Date created;

    @LastModifiedDate
    @Column(name = "updated")
    @JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss")
    private Date updated;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

}