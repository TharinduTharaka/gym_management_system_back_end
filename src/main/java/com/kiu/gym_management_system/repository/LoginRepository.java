package com.kiu.gym_management_system.repository;

import com.kiu.gym_management_system.entity.LoginEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface LoginRepository extends CrudRepository <LoginEntity, Integer> {
 List<LoginEntity> findByEmailAndPassword(@Param("email") String email, @Param("password") String password);
 List<LoginEntity> findByEmail(@Param("email") String email);
}
