package com.chuwa.redbook.service;

import com.chuwa.redbook.entity.Post;
import com.chuwa.redbook.payload.*;

import java.util.List;
import java.util.Set;

/**
 * 1.  user profile - user basic information, such. as bio, name, gender
 * 2.  User‘s posts
 * 3.  User's Collects
 * 4.  User's Likes
 * 5.  number of following, number of followers, number of likes & co
 * */
public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto getUserById(long userId);

    UserDto updateUser(Long userId, UserDto userDto);

    void deleteUser(Long userId);

    List<PostDto> getPostsByUserId(Long userId);

    List<PostDto> getCollectedPostsByUserId(Long userId);
    List<PostDto> getLikedPostsByUserId(Long userId);

    CountResponse getCollectsAndLikes(Long userId);

    void likePostById(LikeDto likeDto, Long userId);

    void collectPostById(LikeDto likeDto, Long userId);

    void unlikePostById(LikeDto likeDto, Long userId);

    void uncollectPostById(LikeDto likeDto, Long userId);
}
