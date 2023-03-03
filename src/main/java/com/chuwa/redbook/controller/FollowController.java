package com.chuwa.redbook.controller;

import com.chuwa.redbook.entity.FriendShip;
import com.chuwa.redbook.entity.FriendShipKey;
import com.chuwa.redbook.payload.CountResponse;
import com.chuwa.redbook.payload.UserDto;
import com.chuwa.redbook.service.FriendShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class FollowController {
    @Autowired
    private FriendShipService friendShipService;

    @PostMapping("/users/{id}/following")
    public ResponseEntity<FriendShipKey> followUserById(@PathVariable("id") Long userId){
        FriendShip savedFriendShip = friendShipService.followUserById(userId);
        FriendShipKey key = savedFriendShip.getId();
        return new ResponseEntity<>(key, HttpStatus.CREATED);
    }


    @DeleteMapping("/users/{id}/following")
    public ResponseEntity<String> unfollowUserById(@PathVariable("id") Long followingId){
        friendShipService.unfollowUserById(followingId);
        return new ResponseEntity<>("Remove following successfully", HttpStatus.CREATED);
    }


    @GetMapping("/users/{id}/followings")
    public ResponseEntity<List<UserDto>> getfollowingsById(@PathVariable("id") Long userId){
        List<UserDto> users = friendShipService.getFollowingsById(userId);
        return new ResponseEntity<>(users, HttpStatus.CREATED);
    }

    @GetMapping("/users/{id}/followers")
    public ResponseEntity<List<UserDto>> getfollowersById(@PathVariable("id") Long userId){
        List<UserDto> users = friendShipService.getFollowersById(userId);
        return new ResponseEntity<>(users, HttpStatus.CREATED);
    }

    @GetMapping("/users/{id}/followers-count")
    public ResponseEntity<CountResponse> getFollowersCount(@PathVariable(name = "id") long id) {
        CountResponse cnt = friendShipService.getFollowersCount(id);
        return new ResponseEntity<>(cnt, HttpStatus.OK);
    }

    @GetMapping("/users/{id}/followings-count")
    public ResponseEntity<CountResponse> getFollowingsCount(@PathVariable(name = "id") long id) {
        CountResponse cnt = friendShipService.getFollowingsCount(id);
        return new ResponseEntity<>(cnt, HttpStatus.OK);
    }
}

