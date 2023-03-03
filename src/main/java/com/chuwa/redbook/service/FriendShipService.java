package com.chuwa.redbook.service;

import com.chuwa.redbook.entity.FriendShip;
import com.chuwa.redbook.entity.User;
import com.chuwa.redbook.payload.CountResponse;
import com.chuwa.redbook.payload.UserDto;

import java.util.List;

public interface FriendShipService {
    FriendShip followUserById(Long userId);
    void unfollowUserById(Long userId);
    List<UserDto> getFollowingsById(Long userId);
    List<UserDto> getFollowersById(Long userId);
    CountResponse getFollowingsCount(Long userId);
    CountResponse getFollowersCount(Long userId);
}
