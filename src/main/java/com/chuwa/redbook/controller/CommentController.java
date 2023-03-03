package com.chuwa.redbook.controller;

import com.chuwa.redbook.aspect.IsAuthor;
import com.chuwa.redbook.entity.Post;
import com.chuwa.redbook.payload.CommentDto;
import com.chuwa.redbook.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author b1go
 * @date 6/23/22 11:30 PM
 */
@RestController
@RequestMapping("/api/v1")
public class CommentController {

    /**
     * TODO: Questions
     * why intellij give us this warning? constructor injection.
     * how many ways we can do Dependency Injection?
     * which way is the best one?
     */
    @Autowired
    private CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable(value = "postId") long id,
                                                    @Valid @RequestBody CommentDto commentDto) {
        return new ResponseEntity<>(commentService.createComment(id, commentDto), HttpStatus.CREATED);
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentDto>> getCommentsByPostId(@PathVariable(value = "postId") Long postId) {
        List<CommentDto> comments = commentService.getCommentsByPostId(postId);
        return new ResponseEntity<>(comments, HttpStatus.FOUND);
    }

    @GetMapping("/posts/{postId}/comments-count")
    public ResponseEntity<Integer> getCommentsCountByPostId(@PathVariable(value = "postId") Long postId) {
        Integer num = commentService.getCommentsCountByPostId(postId);
        return new ResponseEntity<>(num, HttpStatus.FOUND);
    }

    @GetMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDto> getCommentsById(
            @PathVariable(value = "postId") Long postId,
            @PathVariable(value = "id") Long commentId) {

        CommentDto commentDto = commentService.getCommentById(postId, commentId);
        return new ResponseEntity<>(commentDto, HttpStatus.OK);
    }

    @IsAuthor
    @PutMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDto> updateComment(@Valid @RequestBody CommentDto commentDto,
                                                    @PathVariable(value = "id") Long commentId,
                                                    @PathVariable(value = "postId") Long postId) {
        CommentDto updateComment = commentService.updateComment(postId, commentId, commentDto);
        return new ResponseEntity<>(updateComment, HttpStatus.OK);
    }
    
    @IsAuthor
    @DeleteMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable(value = "id") Long commentId,
                                                @PathVariable(value = "postId") Long postId) {
        commentService.deleteComment(postId, commentId);
        return new ResponseEntity<>("Comment deleted Successfully", HttpStatus.OK);
    }
}
