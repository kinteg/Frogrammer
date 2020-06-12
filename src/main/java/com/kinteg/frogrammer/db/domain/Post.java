package com.kinteg.frogrammer.db.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "post")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder(toBuilder = true)
@ToString(callSuper = true)
public class Post extends BaseEntity {

    @Column(name = "post_title")
    @NotNull(message = "Require title isn't null.")
    @NotEmpty(message = "Pleas provide a title.")
    private String title;

    @Column(name = "main_text")
    @NotNull(message = "Require mainText isn't null.")
    @NotEmpty(message = "Pleas provide a mainText.")
    private String mainText;

    @Column(name = "preview")
    @NotNull(message = "Require preview isn't null.")
    @NotEmpty(message = "Pleas provide a preview.")
    private String preview;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "post_tag",
            joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    private Set<Tag> tags = new HashSet<>();

}
