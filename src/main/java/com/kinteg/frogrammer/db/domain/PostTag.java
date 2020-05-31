package com.kinteg.frogrammer.db.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "post_tag")
@Data
public class PostTag {

    @Id
    @Column(name = "post_tag_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

}
