package com.anguapp.jwt.dao;

import com.anguapp.jwt.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao extends CrudRepository<User, String> {
    List<User> findAll();
}
