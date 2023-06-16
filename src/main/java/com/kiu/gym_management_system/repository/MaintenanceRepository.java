package com.kiu.gym_management_system.repository;


import com.kiu.gym_management_system.entity.MaintenanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaintenanceRepository extends JpaRepository<MaintenanceEntity, Integer> {

    List<MaintenanceEntity>findAllByEmpCode(@Param("emp_code") String empCode);

//    List<MaintenanceEntity>findAllByEmpCode(@Param("emp_code") String empCode);

    List<MaintenanceEntity>findByStatus(@Param("status") int status);
}
