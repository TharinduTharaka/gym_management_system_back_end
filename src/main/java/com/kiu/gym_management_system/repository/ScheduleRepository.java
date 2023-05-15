package com.kiu.gym_management_system.repository;

import com.kiu.gym_management_system.entity.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Integer> {
    List<ScheduleEntity> findByEmpCode(@Param("emp_code") String empCode);

    List<ScheduleEntity> findByIdAndEmpCode(int id, String empCode);
}
