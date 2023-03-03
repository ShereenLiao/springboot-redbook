package com.chuwa.redbook.dao;

import com.chuwa.redbook.entity.FriendShip;
import com.chuwa.redbook.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendShipRepository extends JpaRepository<FriendShip, Long> {
    @Query("select distinct f from FriendShip f where f.follower.userId = :userId")
    List<FriendShip> findAllFollowingsByUserId(@Param("userId")Long userId);
    @Query("select distinct f from FriendShip f where f.user.userId = :userId")
    List<FriendShip> findAllFollowersByUserId(@Param("userId")Long userId);

    Optional<FriendShip> findByFollowerAndUser(User follower, User user);

    List<FriendShip> findAllByUser(User user);

    List<FriendShip> findAllByFollower(User user);

    Boolean existsFriendShipByFollowerAndUser(User follower, User user);
}
