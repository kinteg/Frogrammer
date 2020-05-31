package com.kinteg.frogrammer.db.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "tag")
@Data
public class Tag {

    @Id
    @Column(name = "tag_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "tag_title")
    private String title;

    @OneToMany(mappedBy = "tag")
    private Set<PostTag> postTags;

}
