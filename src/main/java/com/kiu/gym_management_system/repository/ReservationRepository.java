package com.kiu.gym_management_system.repository;

import com.kiu.gym_management_system.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Integer> {
    List<ReservationEntity> findByEmpCode(@Param("emp_code") String empCode);
    Optional<ReservationEntity> findById(@Param("id") Integer id);
    List<ReservationEntity> findByIdAndEmpCode(int id, String empCode);
    List<ReservationEntity>findByStatusAndEmpCode(@Param("status") Integer status, @Param("emp_code") String empCode);
}
