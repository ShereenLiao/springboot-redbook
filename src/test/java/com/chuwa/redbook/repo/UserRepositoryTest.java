package com.chuwa.redbook.repo;

import com.chuwa.redbook.dao.PostRepository;
import com.chuwa.redbook.dao.UserRepository;
import com.chuwa.redbook.entity.Comment;
import com.chuwa.redbook.entity.Post;
import com.chuwa.redbook.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testGetAllUsers(){
        List<User> users = userRepository.findAll();
        Assertions.assertEquals(users.size(), 4);
        users.forEach(u -> System.out.println(u));
    }

    @Test
    public void testGetPostsByUserId(){
        Long userId = 10l;
        List<Post> posts = userRepository.findAllPostsByUserId(userId);
        Assertions.assertEquals(posts.size(), 2);
        posts.forEach(u -> System.out.println(u));
    }


    @Test
    public void testGetCollectedPostsByUserId(){
        Long userId = 10l;
        List<Post> posts = userRepository.findAllCollectedPostsByUserId(userId);
        Assertions.assertEquals(posts.size(), 4);
        posts.forEach(u -> System.out.println(u));
    }

    @Test
    public void testGetLikedPostsByUserId(){
        Long userId = 10l;
        List<Post> posts = userRepository.findAllLikedPostsByUserId(userId);
        Assertions.assertEquals(posts.size(), 3);
        posts.forEach(u -> System.out.println(u));
    }

    @Test
    public void testGetCommentsByUserId(){
        Long userId = 10l;
        List<Comment> comment = userRepository.findAllCommentsByUserId(userId);
        Assertions.assertEquals(comment.size(), 1);
        comment.forEach(u -> System.out.println(u));
    }

//    @Test
//    public void testGetFollowersByUserId(){
//        Long userId = 9l;
//        List<User> followers = userRepository.findAllFollowersByUserId(userId);
//        Assertions.assertEquals(followers.size(), 3);
//        followers.forEach(u -> System.out.println(u));
//    }
//
//    @Test
//    public void testGetFollowingsByUserId(){
//        Long userId = 9l;
//        List<User> followers = userRepository.findAllFollowingsByUserId(userId);
//        Assertions.assertEquals(followers.size(), 1);
//        followers.forEach(u -> System.out.println(u));
//    }



}
