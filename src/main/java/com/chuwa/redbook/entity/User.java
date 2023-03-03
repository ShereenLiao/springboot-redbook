package com.chuwa.redbook.entity;

import com.chuwa.redbook.util.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Data
@NoArgsConstructor
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"account"})
})
@EqualsAndHashCode(exclude = {"roles","posts","comments", "collectedPosts", "likedPosts", "followers", "followings"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long userId;
    private String account;
    private String password;
    private String name;

    /**
     * stores the ordinal of this enumeration constant
     * 0: Female, 1: male, 2: unspecific
     * Default : 2
     * */
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "gender")
    private Gender gender = Gender.UNSPECIFIC;

    @Column(name = "bio")
    private String bio;

    /**
     * user - posts : m : n
     * */
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    )
    private Set<Role> roles;

    /**
     * user - posts : 1 - n
     * */
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Post> posts = new HashSet<>();

    /**
     * user - comments : 1 - n
     * */
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Comment> comments = new HashSet<>();

    /**
     * user - posts collected  : m - n
     * */
    @ManyToMany
    @JoinTable(
            name = "posts_collected",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id"))
    @JsonIgnore
    private Set<Post> collectedPosts = new HashSet<>();

    /**
     * user - posts liked  : m - n
     * */
    @ManyToMany
    @JoinTable(
            name = "posts_liked",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id"))
    @JsonIgnore
    private Set<Post> likedPosts = new HashSet<>();


    /**
     * user - followers  : m - n
     * */
    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<FriendShip> followings = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<FriendShip> followers = new HashSet<>();

    @CreationTimestamp
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                ", bio='" + bio + '\'' +
                '}';
    }
}
