package com.course.udamy.controller;

import com.course.udamy.model.User;
import com.course.udamy.model.Video;
import com.course.udamy.service.UserRegistrationService;
import com.course.udamy.service.UserService;
import com.course.udamy.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRegistrationService userRegistrationService;
    private final UserService userService;
    private final VideoService videoService;

    @GetMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public String register(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();

        System.out.println("JJWWTT"+ authentication.getPrincipal());

        System.out.println("JJWWTT"+ jwt);
        System.out.println("JJWWTT"+ jwt.getTokenValue());




        return userRegistrationService.registerUser(jwt.getTokenValue());
    }


    @GetMapping("/user-data")
    @ResponseStatus(HttpStatus.OK)
    public User getUserData() {

        return userService.getUserData();
    }


    @PostMapping("/{videoUserId}/subscribe")
    @ResponseStatus(HttpStatus.OK)
    public boolean subscribeUser(@PathVariable String videoUserId) {// video userId

        userService.subscribeUser(videoUserId);
        return true;
    }

    @PostMapping("/{videoUserId}/unSubscribe")
    @ResponseStatus(HttpStatus.OK)
    public boolean unSubscribeUser(@PathVariable String videoUserId) {
        userService.unSubscribeUser(videoUserId);
        return true;
    }

    @PostMapping("/{videoUserId}/checkSubscribe")
    @ResponseStatus(HttpStatus.OK)
    public boolean checkSubscribe(@PathVariable String videoUserId) {
        return userService.checkSubscribe(videoUserId);
    }

    @GetMapping("/{userId}/history")
    @ResponseStatus(HttpStatus.OK)
    public Set<String> userHistory(@PathVariable String userId) {
        return userService.userHistory(userId);
    }



    @GetMapping("/my-videos")
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Video> fetchUploadVideos() {
        User currentUser = userService.getCurrentUser();

        System.out.println("USERC"+currentUser);

        return videoService.getVideosByUserUploadIds((Set<String>) currentUser.getMyUploadVideos());
    }

    @GetMapping("/my-follow")
    @ResponseStatus(HttpStatus.OK)
    public Iterable<User> fetchFollowers() {
        return  userService.fetchFollowers();
    }

    @GetMapping("/my-watched")
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Video> fetchMyWatched() {
        User currentUser = userService.getCurrentUser();



        return videoService.fetchMyWatched((Set<String>) currentUser.getVideoHistory());
    }


    @GetMapping("/my-liked")
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Video> fetchMyLiked() {
        User currentUser = userService.getCurrentUser();



        return videoService.fetchMyLiked((Set<String>) currentUser.getLikedVideos());
    }



}



