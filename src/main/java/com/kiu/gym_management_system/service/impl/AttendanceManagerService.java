package com.kiu.gym_management_system.service.impl;

import com.kiu.gym_management_system.entity.AttendanceEntity;
import com.kiu.gym_management_system.entity.IclockTransactionEntity;
import com.kiu.gym_management_system.entity.InOutTimeEntity;
import com.kiu.gym_management_system.model.attendance.AttendanceModel;
import com.kiu.gym_management_system.repository.AttendanceRepository;
import com.kiu.gym_management_system.repository.IclockTransactionRepository;
import com.kiu.gym_management_system.response.Response;
import com.kiu.gym_management_system.service.AttendanceManager;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class AttendanceManagerService implements AttendanceManager {

    @Autowired
    AttendanceRepository attendanceRepository;

    @Autowired
    IclockTransactionRepository iclockTransactionRepository;


    @Override
    public Response createFingerprintsAttendance(String empID, AttendanceModel attendanceModel) {
        int employee_ID = Integer.parseInt(empID);

        AttendanceEntity attendanceEntity = new AttendanceEntity();
        List<AttendanceEntity> attendanceEntityList = new ArrayList<>();

        attendanceEntity.setEmpCode(attendanceModel.getEmpCode());
        attendanceEntity.setEmpName(attendanceModel.getEmpName());
        attendanceEntity.setDate(attendanceModel.getDate());
        attendanceEntity.setInTime(attendanceModel.getInTime());
        attendanceEntity.setOutTime(attendanceModel.getOutTime());
        attendanceEntity.setWorkDuration(attendanceModel.getWorkDuration());
        attendanceEntity.setRole(attendanceModel.getRole());
        attendanceEntity.setStatus(attendanceModel.getStatus());
        attendanceEntity.setCreateBy(empID);
        attendanceEntity.setCreateDate(new Date());
        attendanceEntityList.add(attendanceEntity);
        attendanceRepository.saveAll(attendanceEntityList);

        Response response = new Response();
        response.setCode(201);
        response.setMsg("Create Finger Prints Attendance Successful");
        return response;
    }

    @Override
    public Response getAllAttendance() {
        Response response = new Response();
        response.setCode(200);
        response.setMsg("Get All Attendance");
        response.setData(attendanceRepository.findAll());
        return response;
    }

    @Override
    public Response getAllUserAttendance(String role) {

//        List<AttendanceEntity> attendanceEntities = attendanceRepository.findByRole(role);
//
//        List<AttendanceEntity> attendanceEntitiesList = new ArrayList<>();
//
//        for (AttendanceEntity attendance : attendanceEntities) {
//
//            AttendanceEntity attendanceEntity = new AttendanceEntity();
//            attendanceEntity.setId(attendance.getId());
//            attendanceEntity.setEmpCode(attendance.getEmpCode());
//            attendanceEntity.setEmpName(attendance.getEmpName());
//            attendanceEntity.setDate(attendance.getDate());
//            attendanceEntity.setInTime(attendance.getInTime());
//            attendanceEntity.setOutTime(attendance.getOutTime());
//            attendanceEntity.setWorkDuration(attendance.getWorkDuration());
//            attendanceEntitiesList.add(attendanceEntity);
//
//        }

        Response response = new Response();
        response.setCode(200);
        response.setMsg("Get All User  Attendance");
        response.setData(attendanceRepository.findByRole(role));
        return response;
    }


    @Override
    public Response getUserAttendance(String empID, String role) {

        List<AttendanceEntity> attendanceEntities = attendanceRepository.findByEmpCodeAndRole(empID, role);

        List<AttendanceEntity> attendanceEntitiesList = new ArrayList<>();
        for (AttendanceEntity attendance : attendanceEntities) {
            AttendanceEntity attendanceEntity = new AttendanceEntity();
            attendanceEntity.setId(attendance.getId());
            attendanceEntity.setEmpCode(attendance.getEmpCode());
            attendanceEntity.setEmpName(attendance.getEmpName());
            attendanceEntity.setDate(attendance.getDate());
            attendanceEntity.setInTime(attendance.getInTime());
            attendanceEntity.setOutTime(attendance.getOutTime());
            attendanceEntity.setWorkDuration(attendance.getWorkDuration());
            attendanceEntity.setStatus(attendance.getStatus());
            attendanceEntity.setRole(attendance.getRole());
            attendanceEntitiesList.add(attendanceEntity);

        }

        Response response = new Response();
        response.setCode(200);
        response.setMsg("Get All  Attendance");
        response.setData(attendanceEntitiesList);
        return response;
    }
}
