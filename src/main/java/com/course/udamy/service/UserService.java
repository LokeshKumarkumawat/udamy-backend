package com.course.udamy.service;

import com.course.udamy.model.User;

import com.course.udamy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getCurrentUser() {
        String sub = ((Jwt) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getClaim("sub");

        return userRepository.findBySub(sub)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find user with sub - " + sub));
    }

    public void addToLikedVideos(String videoId) {
        User currentUser = getCurrentUser();
        currentUser.addToLikeVideos(videoId);
        userRepository.save(currentUser);
    }

    public boolean ifLikedVideo(String videoId) {
        return getCurrentUser().getLikedVideos().stream().anyMatch(likedVideo -> likedVideo.equals(videoId));
    }

    public boolean ifDisLikedVideo(String videoId) {
        return getCurrentUser().getDisLikedVideos().stream().anyMatch(likedVideo -> likedVideo.equals(videoId));
    }

    public void removeFromLikedVideos(String videoId) {
        User currentUser = getCurrentUser();
        currentUser.removeFromLikedVideos(videoId);
        userRepository.save(currentUser);
    }

    public void removeFromDislikedVideos(String videoId) {
        User currentUser = getCurrentUser();
        currentUser.removeFromDislikedVideos(videoId);
        userRepository.save(currentUser);
    }

    public void addToDisLikedVideos(String videoId) {
        User currentUser = getCurrentUser();
        currentUser.addToDislikedVideos(videoId);
        userRepository.save(currentUser);
    }

    public void addVideoToHistory(String videoId) {
        User currentUser = getCurrentUser();
        System.out.println("CURR"+currentUser);
        System.out.println("Videoid"+videoId);
        currentUser.addToVideoHistory(videoId);
        userRepository.save(currentUser);
    }

    public void subscribeUser(String videoUserId) {  // userid=> video userId
        User currentUser = getCurrentUser();


        currentUser.addToSubscribers(videoUserId);


        userRepository.save(currentUser);
    }

    public boolean checkSubscribe(String videoUserId) {  // userid=> video userId
        User currentUser = getCurrentUser();
        Set<String> subscribers =  currentUser.getSubscribers();

        if (subscribers.contains(videoUserId)) {
            // The set contains the desired ID
            // You can perform actions accordingly
            System.out.println("The set contains the desired ID.");
            return true;
        } else {
            // The set does not contain the desired ID
            System.out.println("The set does not contain the desired ID.");
            return false;
        }

    }

    public void unSubscribeUser(String userId) {
        User currentUser = getCurrentUser();
        currentUser.removeFromSubscribers(userId);
        userRepository.save(currentUser);
    }

    public Set<String> userHistory(String userId) {
        User user = getUserById(userId);
        return user.getVideoHistory();
    }

    public void userMyUploadVideo( String videoId) {
        User currentUser = getCurrentUser();
        currentUser.addToMyUploadVideo(videoId);
        userRepository.save(currentUser);
    }

    public User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find user with userId " + userId));
    }

    public Iterable<User> fetchFollowers() {
        User currentUser = getCurrentUser();
        Set<String> subscribers =  currentUser.getSubscribers();
        return userRepository.findAllById(subscribers);
    }

    public Iterable<User> fetchMyLiked() {
        User currentUser = getCurrentUser();
        Set<String> likedVideos =  currentUser.getLikedVideos();
        return userRepository.findAllById(likedVideos);
    }

    public Iterable<User> fetchMyWatched() {
        User currentUser = getCurrentUser();
        Set<String> videoHistory =  currentUser.getVideoHistory();
        return userRepository.findAllById(videoHistory);
    }

    public User getUserData() {
        User currentUser = getCurrentUser();
    return currentUser;
    }


}