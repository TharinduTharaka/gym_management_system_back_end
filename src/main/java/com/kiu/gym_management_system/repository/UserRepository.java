package com.kiu.gym_management_system.repository;

import com.kiu.gym_management_system.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends CrudRepository <UserEntity, Integer> {
    public List<UserEntity> findByEmail(@Param("email") String email);
}
