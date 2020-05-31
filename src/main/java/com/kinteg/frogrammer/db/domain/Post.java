package com.kinteg.frogrammer.db.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "post")
@Data
public class Post {

    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "post_title")
    private String title;

    @Column(name = "main_text")
    private String mainText;

    @Column(name = "preview")
    private String preview;

    @OneToMany(mappedBy = "post")
    private Set<PostTag> postTags;

}
