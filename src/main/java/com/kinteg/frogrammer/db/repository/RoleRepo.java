package com.kinteg.frogrammer.db.repository;

import com.kinteg.frogrammer.db.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Long> {

    Role findByName(String name);

}
