package com.chuwa.redbook.service.impl;

import com.chuwa.redbook.dao.FriendShipRepository;
import com.chuwa.redbook.dao.UserRepository;
import com.chuwa.redbook.entity.FriendShip;
import com.chuwa.redbook.entity.User;
import com.chuwa.redbook.exception.ResourceNotFoundException;
import com.chuwa.redbook.payload.CountResponse;
import com.chuwa.redbook.payload.UserDto;
import com.chuwa.redbook.service.FriendShipService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendShipServiceImpl implements FriendShipService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendShipRepository friendShipRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public FriendShip followUserById(Long userId) {
        String account = getAuthenticationAccount();

        User follower = userRepository.findByAccount(account).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        User following = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        if(friendShipRepository.existsFriendShipByFollowerAndUser(follower, following)){
            return friendShipRepository.findByFollowerAndUser(follower, following).get();
        }
        FriendShip friendShip = new FriendShip();
        friendShip.setFollower(follower);
        friendShip.setUser(following);
        FriendShip savedFriendShip = friendShipRepository.save(friendShip);
        return savedFriendShip;
    }

    @Override
    public void unfollowUserById(Long userId) {
        String account = getAuthenticationAccount();

        User following = userRepository.findByAccount(account).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        User follower = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        if(!friendShipRepository.existsFriendShipByFollowerAndUser(follower, following)){
            return;
        }
        FriendShip friendShip = friendShipRepository.findByFollowerAndUser(follower, following).orElseThrow(() -> new ResourceNotFoundException("Friendship", "follower", userId));
        friendShipRepository.delete(friendShip);
    }

    @Override
    public List<UserDto> getFollowingsById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        List<FriendShip> friendShips = friendShipRepository.findAllByUser(user);
        List<UserDto> users = friendShips.stream()
                .map(f -> f.getFollower())
                .map(u -> modelMapper.map(u, UserDto.class))
                .collect(Collectors.toList());
        return users;
    }

    @Override
    public List<UserDto> getFollowersById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        List<FriendShip> friendShips = friendShipRepository.findAllByFollower(user);
        List<UserDto> users = friendShips.stream()
                .map(f -> f.getUser())
                .map(u -> modelMapper.map(u, UserDto.class))
                .collect(Collectors.toList());
        return users;
    }

    private String getAuthenticationAccount(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null){
            return null;//todo: ???
        }
        return authentication.getName();
    }

    @Override
    public CountResponse getFollowingsCount(Long userId) {
        List<FriendShip> following = friendShipRepository.findAllFollowingsByUserId(userId);
        return formatCountResponse(userId, following.size());
    }

    @Override
    public CountResponse getFollowersCount(Long userId) {
        List<FriendShip> follower = friendShipRepository.findAllFollowersByUserId(userId);
        return formatCountResponse(userId, follower.size());
    }

    private CountResponse formatCountResponse(Long userId, Integer...lists){
        int num = 0;
        for(Integer i : lists){
            num += i;
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        CountResponse response = new CountResponse();
        response.setCount(num);
        response.setUserName(user.getName());
        response.setUserId(user.getUserId());
        return response;
    }
}
