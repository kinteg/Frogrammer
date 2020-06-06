package com.kinteg.frogrammer.db.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "role")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RoleEntity extends BaseEntity {


    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private List<User> users;

}
