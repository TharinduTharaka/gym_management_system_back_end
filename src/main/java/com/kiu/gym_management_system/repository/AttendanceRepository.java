package com.kiu.gym_management_system.repository;

import com.kiu.gym_management_system.entity.AttendanceEntity;
import com.kiu.gym_management_system.entity.IclockTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceEntity, Integer> {
    List<AttendanceEntity> findByEmpCode(@Param("emp_code") String empCode);

}
