package com.kinteg.frogrammer.db.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.rest.core.annotation.Description;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "usr")
@Data
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "username")
    @NotNull
    @Size(min = 6, max = 15)
    private String username;

    @Column(name = "first_name")
    @NotNull
    @Size(min = 2, max = 15)
    private String firstName;

    @Column(name = "last_name")
    @NotNull
    @Size(min = 2, max = 30)
    private String lastName;

    @Column(name = "email")
    @NotNull
    @Size(min = 6, max = 30)
    private String email;

    @Column(name = "password")
    @NotNull
    private String password;
    transient private String confirmPassword;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Post> posts;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<RoleEntity> role;

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
