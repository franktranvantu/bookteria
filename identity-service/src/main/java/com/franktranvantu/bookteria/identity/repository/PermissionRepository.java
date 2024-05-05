package com.franktranvantu.bookteria.identity.repository;

import com.franktranvantu.bookteria.identity.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {}
