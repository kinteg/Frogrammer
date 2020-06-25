package com.kinteg.frogrammer.db.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tag")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "tag_title")
    private String title;

    @Column(name = "description")
    private String description;

    @CreatedDate
    @Column(name = "created")
    @JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss")
    private Date created;

    @LastModifiedDate
    @Column(name = "updated")
    @JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss")
    private Date updated;

}
