package com.kiu.gym_management_system.service.impl;

import com.kiu.gym_management_system.entity.ReservationEntity;
import com.kiu.gym_management_system.entity.ScheduleEntity;
import com.kiu.gym_management_system.model.schedule.ScheduleModel;
import com.kiu.gym_management_system.repository.ScheduleRepository;
import com.kiu.gym_management_system.response.Response;
import com.kiu.gym_management_system.service.ScheduleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ScheduleManagerService implements ScheduleManager {


    @Autowired
    ScheduleRepository scheduleRepository;

    @Override
    public Response getAllSchedule() {
        Response response = new Response();
        response.setCode(200);
        response.setData(scheduleRepository.findAll());
        response.setMsg("Get All Schedule");
        return response;
    }


    @Override
    public Response getUserAllSchedule(String empID) {

        Response response = new Response();
        response.setCode(200);
        response.setData(scheduleRepository.findByEmpCode(empID));
        response.setMsg("Get user's All Schedule");
        return response;
    }

    public Response getUserSchedule(String empID, int id) {

        Response response = new Response();
        response.setCode(200);
        response.setData(scheduleRepository.findByIdAndEmpCode(id, empID));
        response.setMsg("Get user's Schedule");
        return response;
    }

    public Response getSchedule(int id) {
        Response response = new Response();
        response.setCode(200);
        response.setData(scheduleRepository.findById(id));
        response.setMsg("Get admin Schedule");
        return response;
    }

    @Override
    public Response getUserFilterScheduleData(String empID, int status) {
        Response response = new Response();
        response.setCode(200);
        response.setData(scheduleRepository.findByStatusAndEmpCode(status, empID));
        response.setMsg("Get user's filter Schedule Data");
        return response;

    }

    @Override
    public Response createSchedule(String empID, ScheduleModel scheduleModel) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

//        Date startDate = new Date();
//        Date endDate = new Date();
//
//        try {
//            startDate = formatter.parse(scheduleModel.getStartDate());
//            endDate = formatter.parse(scheduleModel.getEndDate());
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        }

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setEmpCode(empID);
        scheduleEntity.setTitle(scheduleModel.getTitle());
        scheduleEntity.setWeekDay(scheduleModel.getWeekDay());
        scheduleEntity.setStatus(scheduleModel.getStatus());
        scheduleEntity.setStartDate(scheduleModel.getStartDate());
        scheduleEntity.setEndDate(scheduleModel.getEndDate());
        scheduleEntity.setDescription(scheduleModel.getDescription());
        scheduleEntity.setInstructorName(scheduleModel.getInstructorName());

        List<ScheduleEntity> scheduleEntityList = new ArrayList<>();

        scheduleEntityList.add(scheduleEntity);
        scheduleRepository.saveAll(scheduleEntityList);

        Response response = new Response();
        response.setCode(201);
        response.setMsg("Create Reservation Successful");
        response.setData(scheduleRepository.findById(scheduleEntity.getId()));
        return response;
    }

    @Override
    public Response editScheduleStatus(int status, int id) {

        Response response = new Response();
        Optional<ScheduleEntity> scheduleEntityOptional = scheduleRepository.findById(id);

        if (scheduleEntityOptional.isPresent()) {
            ScheduleEntity obj = scheduleEntityOptional.get();
            obj.setStatus(status);
            scheduleRepository.save(obj);
            response.setCode(200);
            response.setMsg("Update Reservation Successful");
//            response.setData(scheduleRepository.findById(obj.getId()));
            return response;
        } else {
            response.setCode(404);
            response.setMsg("Not Found");
            return response;
        }

    }

    @Override
    public Response editSchedule(ScheduleModel scheduleModel, int id) {

        Response response = new Response();
        Optional<ScheduleEntity> scheduleEntityOptional = scheduleRepository.findById(id);

        if (scheduleEntityOptional.isPresent()) {
            ScheduleEntity obj = scheduleEntityOptional.get();
            obj.setTitle(scheduleModel.getTitle());
            obj.setEmpCode(scheduleModel.getEmpCode());
            obj.setDescription(scheduleModel.getDescription());
            obj.setWeekDay(scheduleModel.getWeekDay());
            obj.setStartDate(scheduleModel.getStartDate());
            obj.setEndDate(scheduleModel.getEndDate());
            obj.setStatus(scheduleModel.getStatus());
            obj.setInstructorName(scheduleModel.getInstructorName());
            scheduleRepository.save(obj);
            response.setCode(200);
            response.setMsg("Update Schedule Successful");
//            response.setData(scheduleRepository.findById(obj.getId()));
            return response;
        } else {
            response.setCode(404);
            response.setMsg("Not Found");
            return response;
        }

    }

    @Override
    public Response deleteSchedule(int status, int id) {

        Response response = new Response();
        Optional<ScheduleEntity> scheduleEntityOptional = scheduleRepository.findById(id);

        if (scheduleEntityOptional.isPresent()) {
            ScheduleEntity obj = scheduleEntityOptional.get();
            obj.setStatus(status);
            scheduleRepository.save(obj);
            response.setCode(200);
            response.setMsg("Delete Reservation Successful");
//            response.setData(scheduleRepository.findById(obj.getId()));
            return response;
        } else {
            response.setCode(404);
            response.setMsg("Not Found");
            return response;
        }

    }
}
