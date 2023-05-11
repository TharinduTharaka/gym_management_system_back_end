package com.kiu.gym_management_system.service.impl;

import com.kiu.gym_management_system.entity.AttendanceEntity;
import com.kiu.gym_management_system.entity.IclockTransactionEntity;
import com.kiu.gym_management_system.entity.InOutTimeEntity;
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
    public Response getAllAttendance() {
        Response response = new Response();
        response.setCode(200);
        response.setMsg("Get All Attendance");
        response.setData(iclockTransactionRepository.findAll());
        return response;
    }

    @Override
    public Response getUserAttendance(String empID) {

        List<AttendanceEntity> attendanceEntities = attendanceRepository.findByEmpCode(empID);

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
            attendanceEntitiesList.add(attendanceEntity);

        }

        Response response = new Response();
        response.setCode(200);
        response.setMsg("Get All User's Attendance");
        response.setData(attendanceEntitiesList);
        return response;
    }
}
