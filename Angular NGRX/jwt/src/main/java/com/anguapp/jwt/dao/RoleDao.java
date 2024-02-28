package com.anguapp.jwt.dao;

import com.anguapp.jwt.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDao extends CrudRepository<Role,String> {
}
