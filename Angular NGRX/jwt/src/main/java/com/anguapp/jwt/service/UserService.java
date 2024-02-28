package com.anguapp.jwt.service;

import com.anguapp.jwt.dao.RoleDao;
import com.anguapp.jwt.dao.UserDao;
import com.anguapp.jwt.entity.Role;
import com.anguapp.jwt.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PasswordEncoder passwordEncoder;



    public User registerNewUser(User user){

        Role role = roleDao.findById("User").get();

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRole(roles);
        user.setUserPassword(getEncodedPassword(user.getUserPassword()));
        return userDao.save(user);
    }

    public void initRolesAndUser(){
        Role adminRole = new Role();
        adminRole.setRoleName("Admin");
        adminRole.setRoleDescription("Admin role");
        roleDao.save(adminRole);

        Role userRole = new Role();
        userRole.setRoleName("User");
        userRole.setRoleDescription("Default role for new records");
        roleDao.save(userRole);

        User adminUser = new User();
        adminUser.setUserFirstName("admin");
        adminUser.setUserLastName("admin");
        adminUser.setUserName("admin123");
        adminUser.setUserPassword(getEncodedPassword("admin@pass"));
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        adminUser.setRole(adminRoles);
        userDao.save(adminUser);

        User user = new User();
        user.setUserFirstName("neo");
        user.setUserLastName("neo");
        user.setUserName("neo");
        user.setUserPassword(getEncodedPassword("neo"));



        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);
        user.setRole(userRoles);
        userDao.save(user);






    }

    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public List<User> getAllUser() {
        return userDao.findAll();
    }

    public void deleteUser(String id) {
        Optional<User> optionalUser = userDao.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.getRole().clear(); // Remove all associated roles
            userDao.save(user); // Save the user without roles
            userDao.deleteById(id); // Now delete the user
        } else {
            // Handle the case where the user with the given ID doesn't exist
            throw new NoSuchElementException("User not found with ID: " + id);
        }
    }


    public User updateUser(User user) {
        return userDao.save(user);
    }

    public Optional<User> getCurrentUser(String username){
        Optional<User> user = userDao.findById(username);
        return user;
    }
}
