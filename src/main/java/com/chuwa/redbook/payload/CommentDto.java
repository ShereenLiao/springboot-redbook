package com.chuwa.redbook.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


@Data
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private Long authorUserId;
    private String authorName;

    @NotEmpty
    @Size(min = 5, message = "Comment body must be minimum 5 characters")
    private String body;

}
