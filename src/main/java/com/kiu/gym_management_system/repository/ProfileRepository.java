package com.kiu.gym_management_system.repository;

import com.kiu.gym_management_system.entity.MembersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProfileRepository extends JpaRepository<MembersEntity, Integer> {

    List<MembersEntity> findByStatus(@Param("status") int status);

    List<MembersEntity> findByIdAndStatus(@Param("id") int id,@Param("status") int status);
}
