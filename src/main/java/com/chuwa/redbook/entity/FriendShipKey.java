package com.chuwa.redbook.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class FriendShipKey implements Serializable {
    private Long followerId;
    private Long userId;

    public FriendShipKey(Long followerId, Long userId) {
        this.followerId = followerId;
        this.userId = userId;
    }

    public FriendShipKey() {
    }

    public Long getFollowerId() {
        return followerId;
    }

    public void setFollowerId(Long followerId) {
        this.followerId = followerId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
