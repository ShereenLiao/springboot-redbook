package com.chuwa.redbook.controller;

import com.chuwa.redbook.payload.CountResponse;
import com.chuwa.redbook.payload.LikeDto;
import com.chuwa.redbook.payload.PostDto;
import com.chuwa.redbook.payload.UserDto;
import com.chuwa.redbook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable(value = "id") Long id){
        UserDto userDto = userService.getUserById(id);
        return new ResponseEntity<>(userDto, HttpStatus.FOUND);
    }

    @PostMapping()
    public ResponseEntity<UserDto> createPost(@Valid @RequestBody UserDto userDto) {
        UserDto user = userService.createUser(userDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUserById(@Valid @RequestBody UserDto userDto, @PathVariable(name = "id") long id) {
        UserDto user = userService.updateUser(id, userDto);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(name = "id") long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>("User entity deleted successfully.", HttpStatus.OK);
    }

    @GetMapping("/{id}/posts")
    public ResponseEntity<List<PostDto>> getAllPosts(@PathVariable(name = "id") long id) {
        List<PostDto> postDtos = userService.getPostsByUserId(id);
        return new ResponseEntity<>(postDtos, HttpStatus.OK);
    }

    @PostMapping("/{id}/likes")
    public ResponseEntity<String> likePostById(@PathVariable(name = "id") long userId, @RequestBody LikeDto likedto) {
        userService.likePostById(likedto, userId);
        return new ResponseEntity<>("Like the post successfully", HttpStatus.OK);
    }

    @PostMapping("/{id}/collects")
    public ResponseEntity<String> collectPostById(@PathVariable(name = "id") long userId, @RequestBody LikeDto likedto) {
        userService.collectPostById(likedto, userId);
        return new ResponseEntity<>("Collect the post successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}/likes")
    public ResponseEntity<String> unlikePostById(@PathVariable(name = "id") long userId, @RequestBody LikeDto likedto) {
        userService.unlikePostById(likedto, userId);
        return new ResponseEntity<>("Unlike the post successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}/collects")
    public ResponseEntity<String> uncollectPostById(@PathVariable(name = "id") long userId, @RequestBody LikeDto likedto) {
        userService.uncollectPostById(likedto, userId);
        return new ResponseEntity<>("Uncollect the post successfully", HttpStatus.OK);
    }

    @GetMapping("/{id}/collects")
    public ResponseEntity<List<PostDto>> getAllCollectedPosts(@PathVariable(name = "id") long id) {
        List<PostDto> postDtos = userService.getCollectedPostsByUserId(id);
        return new ResponseEntity<>(postDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}/likes")
    public ResponseEntity<List<PostDto>> getAllLikedPosts(@PathVariable(name = "id") long id) {
        List<PostDto> postDtos = userService.getLikedPostsByUserId(id);
        return new ResponseEntity<>(postDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}/likes-collects-count")
    public ResponseEntity<CountResponse> getLikesAndCollectsCount(@PathVariable(name = "id") long id) {
        CountResponse cnt = userService.getCollectsAndLikes(id);
        return new ResponseEntity<>(cnt, HttpStatus.OK);
    }

}
