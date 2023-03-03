package com.chuwa.redbook.dao;

import com.chuwa.redbook.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("select count(u) from Post post join post.collectUsers u where post.id =:postId")
    Integer countCollectsByPostId(@Param("postId")long postId);
    @Query("select count(u) from Post post join post.likeUsers u where post.id =:postId")
    Integer countLikesByPostId(@Param("postId")long postId);

    Integer countPostByCreateDateTimeBetween(LocalDateTime start, LocalDateTime end);
    List<Post> getPostByCreateDateTimeBetween(LocalDateTime start, LocalDateTime end);
    Optional<Post> findById(Long aLong);
}
