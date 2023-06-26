package com.kiu.gym_management_system.controller;


import com.kiu.gym_management_system.model.attendance.AttendanceModel;
import com.kiu.gym_management_system.model.maintenance.CategoryModel;
import com.kiu.gym_management_system.repository.AttendanceRepository;
import com.kiu.gym_management_system.response.Response;
import com.kiu.gym_management_system.service.impl.AttendanceManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/api/gym/attendance")
@CrossOrigin(origins = "*")
public class AttendanceController {


    @Autowired
    private AttendanceManagerService attendanceManagerService;

    @GetMapping("/get-all-attendance")
    public Response getAllAttendance() {
        return attendanceManagerService.getAllAttendance();
    }

    @GetMapping("/get-all-user-attendance/{role}")
    public Response getAllUserAttendance(@PathVariable String role) {
        return attendanceManagerService.getAllUserAttendance(role);
    }


    @GetMapping("/get-user-attendance/{id}/{role}")
    public Response getUserAttendance(@PathVariable String id, @PathVariable String role) throws ParseException {
        return attendanceManagerService.getUserAttendance(id, role);
    }

    @PostMapping("/create-attendance/{emp_id}")
    public Response createFingerprintsAttendance(@PathVariable String emp_id,
                                              @RequestBody AttendanceModel attendanceModel) {
        return attendanceManagerService.createFingerprintsAttendance(emp_id, attendanceModel);
    }




}
