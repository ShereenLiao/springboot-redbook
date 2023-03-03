package com.chuwa.redbook.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.print.attribute.standard.Media;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Data
@NoArgsConstructor
@Entity
@Table(
        name = "posts",
        uniqueConstraints = {
             @UniqueConstraint(columnNames = {"title"})
        }
)
@EqualsAndHashCode(exclude = {"comments", "likeUsers", "collectUsers"})
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "content", nullable = false)
    private String content;

    /**
     * posts - user,  n : 1
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    /**
     * posts - comments,  1 : n
     */
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Comment> comments = new HashSet<>();

    /**
     * post -  users like the post,  m : n
     */
    @ManyToMany(mappedBy = "likedPosts")
    @JsonIgnore
    Set<User> likeUsers;

    /**
     * post -  users collect the post,  m : n
     */
    @ManyToMany(mappedBy = "collectedPosts")
    Set<User> collectUsers;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    @ElementCollection
    @CollectionTable(name = "post_pictures", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "picture")
    private List<String> pictures;

    private String video;

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", content='" + content + '\'' +
                ", author = '" + author.getUserId() + '\'' +
                '}';
    }

}
