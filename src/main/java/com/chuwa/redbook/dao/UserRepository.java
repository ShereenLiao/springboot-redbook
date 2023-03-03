package com.chuwa.redbook.dao;

import com.chuwa.redbook.entity.Comment;
import com.chuwa.redbook.entity.Post;
import com.chuwa.redbook.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select distinct user.posts from User user join user.posts where user.userId = ?1 ")
    List<Post> findAllPostsByUserId(Long userId);
    @Query(value = "select distinct p from User user join user.collectedPosts p where user.userId = :userId")
    List<Post> findAllCollectedPostsByUserId(@Param("userId")Long userId);
    @Query("select distinct c from User user join user.comments c where user.userId = :userId")
    List<Comment> findAllCommentsByUserId(@Param("userId")Long userId);
    @Query("select distinct p from User user join user.likedPosts p where user.userId = :userId")
    List<Post> findAllLikedPostsByUserId(@Param("userId")Long userId);

    Optional<User> findByAccount(String username);
    Boolean existsByAccount(String user);


}



