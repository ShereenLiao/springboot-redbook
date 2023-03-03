package com.chuwa.redbook.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@Table(name = "friendships")
public class FriendShip {
    @EmbeddedId
    FriendShipKey id = new FriendShipKey();

    @ManyToOne
    @MapsId("followerId")
    @JoinColumn(name = "follower_id")
    @JsonIgnore
    User follower;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    @JsonIgnore
    User user;
}
