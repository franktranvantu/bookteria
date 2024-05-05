package com.franktranvantu.bookteria.identity.repository;

import com.franktranvantu.bookteria.identity.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {}
