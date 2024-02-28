package com.anguapp.jwt.controller;

import com.anguapp.jwt.dao.UserDao;
import com.anguapp.jwt.entity.ImageModel;
import com.anguapp.jwt.entity.User;
import com.anguapp.jwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @PostConstruct
    public void initRolesAndUsers(){
        userService.initRolesAndUser();
    }


    @PostMapping({"/registerNewUser"})
    public User registerNewUser(@RequestBody User user) {
        return userService.registerNewUser(user);
    }

    @DeleteMapping("/deleteUser/{id}")
    public void deleteUser(@PathVariable String id) {
        System.out.println("hi");
        userService.deleteUser(id);
    }

    @GetMapping({"/forAdmin"})
    @PreAuthorize("hasRole('Admin')")
    public String forAdmin(){

        return "This URL is only accessible to admin";
    }

    @GetMapping({"/forUser"})
    @PreAuthorize("hasRole('User')")
    public String forUser(){
        return "This URL is only accessible to the user";
    }

    @GetMapping({"/getUser"})
    @PreAuthorize("hasRole('Admin')")
    public List<User> getAllUser(){ return userService.getAllUser(); }


    @PostMapping("/updateUser")
    public User updateUser(@RequestBody User user){
        return userService.updateUser(user);
    }


    @GetMapping("/getCurrentUser")
    public Optional<User> getCurrentUser(Principal principal) {
        return userService.getCurrentUser(principal.getName());
    }

    @PostMapping(value = {"/addProfilePic"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public User addProfilePic(@RequestPart("imageFile")MultipartFile[] file, Principal principal) {

        try {
            Set<ImageModel> images = uploadImage(file);
            User currentUser = userDao.findById(principal.getName()).orElse(null);
            currentUser.setUserImages(images);
            return userService.updateUser(currentUser);
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }

    }

    public Set<ImageModel> uploadImage(MultipartFile[] multipartFiles) throws IOException {
        Set<ImageModel> imageModels = new HashSet<>();
        for (MultipartFile file: multipartFiles) {
            ImageModel imageModel = new ImageModel(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
            );
            imageModels.add(imageModel);
        }
        return imageModels;
    }



}
