package com.chuwa.redbook.aspect;

import com.chuwa.redbook.dao.CommentRepository;
import com.chuwa.redbook.dao.PostRepository;
import com.chuwa.redbook.dao.UserRepository;
import com.chuwa.redbook.entity.Comment;
import com.chuwa.redbook.entity.Post;
import com.chuwa.redbook.entity.User;
import com.chuwa.redbook.exception.BlogAPIException;
import com.chuwa.redbook.exception.ResourceNotFoundException;
import com.chuwa.redbook.payload.CommentDto;
import com.chuwa.redbook.payload.PostDto;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Aspect
@Component
public class IsAuthorAspect {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;

    @Before("@annotation(com.chuwa.redbook.aspect.IsAuthor)&&execution(* com.chuwa.redbook.controller.*.update*(..))")
    public void isAuthorUpdateCheck(JoinPoint joinPoint) {
        checkAuthentication();
        String account = SecurityContextHolder.getContext().getAuthentication().getName();
        Object[] args = joinPoint.getArgs();
        //update method
        if (args[0] instanceof PostDto) {
            Long id = (Long) args[1];
            Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
            User user = userRepository.findByAccount(account).orElseThrow(() -> new ResourceNotFoundException("User", "account", 0L));
            if (!post.getAuthor().equals(user)) {
                throw new AccessDeniedException("The author of the post is not the user. ");
            }
        } else if (args[0] instanceof CommentDto) {
            Long id = (Long) args[1];
            Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", id));
            User user = userRepository.findByAccount(account).orElseThrow(() -> new ResourceNotFoundException("User", "account", 0L));
            if (!comment.getAuthor().equals(user)) {
                throw new AccessDeniedException("The author of the post is not the user. ");
            }
        }
    }


    @Before("@annotation(com.chuwa.redbook.aspect.IsAuthor)&&execution(* com.chuwa.redbook.controller.*.deletePost*(..))")
    public void isAuthorPostDeleteCheck(JoinPoint joinPoint) {
        checkAuthentication();
        String account = SecurityContextHolder.getContext().getAuthentication().getName();

        Object[] args = joinPoint.getArgs();
        Long id = (Long) args[0];
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        User user = userRepository.findByAccount(account).orElseThrow(() -> new ResourceNotFoundException("User", "account", 0L));
        if (!post.getAuthor().equals(user)) {
            throw new AccessDeniedException("The author of the post is not the user. ");
        }
    }

    @Before("@annotation(com.chuwa.redbook.aspect.IsAuthor)&&execution(* com.chuwa.redbook.controller.*.deleteComment*(..))")
    public void isAuthorCommentDeleteCheck(JoinPoint joinPoint) {
        checkAuthentication();
        String account = SecurityContextHolder.getContext().getAuthentication().getName();

        Object[] args = joinPoint.getArgs();
        Long commentId = (Long) args[0];
        Long postId = (Long) args[1];
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
        if (!post.getComments().contains(comment)) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "The comment#" + commentId + "doesn't belong to the post#" + postId + ".");
        }
        User user = userRepository.findByAccount(account).orElseThrow(() -> new ResourceNotFoundException("User", "account", 0L));
        if (!post.getAuthor().equals(user)) {
            throw new AccessDeniedException("The author of the post is not the user. ");
        }
    }

    private void checkAuthentication() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (!Objects.isNull(securityContext)) {
            Authentication authentication = securityContext.getAuthentication();
            if (!Objects.isNull(authentication)) {
                return;
            }
        }
        throw new AccessDeniedException("Authentication fails.");
    }

}
