package com.chuwa.redbook.service.impl;

import com.chuwa.redbook.dao.FriendShipRepository;
import com.chuwa.redbook.dao.PostRepository;
import com.chuwa.redbook.dao.UserRepository;
import com.chuwa.redbook.entity.Post;
import com.chuwa.redbook.entity.User;
import com.chuwa.redbook.exception.ResourceNotFoundException;
import com.chuwa.redbook.payload.CountResponse;
import com.chuwa.redbook.payload.LikeDto;
import com.chuwa.redbook.payload.PostDto;
import com.chuwa.redbook.payload.UserDto;
import com.chuwa.redbook.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private FriendShipRepository friendShipRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto getUserById(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        return modelMapper.map(user, UserDto.class);
    }


    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        user.setBio(user.getBio());
        user.setName(user.getName());
        user.setGender(userDto.getGender());

        User updatedUser = userRepository.save(user);
        return modelMapper.map(updatedUser, UserDto.class);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        userRepository.delete(user);
    }

    @Override
    public List<PostDto> getPostsByUserId(Long userId) {
        List<Post> posts = userRepository.findAllPostsByUserId(userId);
        List<PostDto> postDtos = posts.stream().map(post -> modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
        return postDtos;
    }

    @Override
    public List<PostDto> getCollectedPostsByUserId(Long userId) {
        List<Post> posts = userRepository.findAllCollectedPostsByUserId(userId);
        List<PostDto> postDtos = posts.stream().map(post -> modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
        return postDtos;
    }

    @Override
    public List<PostDto> getLikedPostsByUserId(Long userId) {
        List<Post> posts = userRepository.findAllLikedPostsByUserId(userId);
        List<PostDto> postDtos = posts.stream().map(post -> modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
        return postDtos;
    }

    @Override
    public CountResponse getCollectsAndLikes(Long userId) {
        List<Post> posts = userRepository.findAllPostsByUserId(userId);
        Integer likes = posts.stream().mapToInt(p -> postRepository.countLikesByPostId(p.getId())).sum();
        Integer collects = posts.stream().mapToInt(p -> postRepository.countCollectsByPostId(p.getId())).sum();

        return formatCountResponse(userId, likes, collects);
    }

    @Override
    public void likePostById(LikeDto likeDto, Long userId){
        User user = getAuthenticationUser();
        if(!user.getUserId().equals(userId)){
            throw new AccessDeniedException("The ID dosen't belong to the authenticating user.");
        }
        Long postId = likeDto.getPostId();
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        Set<Post> likedPosts = user.getLikedPosts();
        likedPosts.add(post);
        user.setLikedPosts(likedPosts);
        userRepository.save(user);
    }

    @Override
    public void collectPostById(LikeDto likeDto, Long userId){
        User user = getAuthenticationUser();
        if(!user.getUserId().equals(userId)){
            throw new AccessDeniedException("The ID dosen't belong to the authenticating user.");
        }
        Long postId = likeDto.getPostId();
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        Set<Post> posts = user.getCollectedPosts();
        posts.add(post);
        user.setCollectedPosts(posts);
        userRepository.save(user);
    }

    @Override
    public void unlikePostById(LikeDto likeDto, Long userId){
        User user = getAuthenticationUser();
        if(!user.getUserId().equals(userId)){
            throw new AccessDeniedException("The ID dosen't belong to the authenticating user.");
        }
        Long postId = likeDto.getPostId();
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        Set<Post> likedPosts = user.getLikedPosts();
        likedPosts.remove(post);
        user.setLikedPosts(likedPosts);
        userRepository.save(user);
    }

    @Override
    public void uncollectPostById(LikeDto likeDto, Long userId){
        User user = getAuthenticationUser();
        if(!user.getUserId().equals(userId)){
            throw new AccessDeniedException("The ID dosen't belong to the authenticating user.");
        }
        Long postId = likeDto.getPostId();
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        Set<Post> posts = user.getCollectedPosts();
        posts.remove(post);
        user.setCollectedPosts(posts);
        userRepository.save(user);
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

    private User getAuthenticationUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null){
            return null;//todo: ???
        }
        User user = userRepository.findByAccount(authentication.getName()).orElseThrow(() -> new ResourceNotFoundException("User", "account", 0L));
        return user;
    }

}
