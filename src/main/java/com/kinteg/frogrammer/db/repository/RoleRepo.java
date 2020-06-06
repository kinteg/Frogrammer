package com.kinteg.frogrammer.db.repository;

import com.kinteg.frogrammer.db.domain.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<RoleEntity, Long> {

    RoleEntity findByName(String name);

}
