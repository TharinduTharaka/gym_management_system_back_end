package com.kiu.gym_management_system.service.impl;


import com.kiu.gym_management_system.entity.ReservationEntity;
import com.kiu.gym_management_system.model.reservation.ExtendedReservationForGetReservation;
import com.kiu.gym_management_system.model.reservation.ReservationModel;
import com.kiu.gym_management_system.repository.ReservationRepository;
import com.kiu.gym_management_system.response.Response;
import com.kiu.gym_management_system.service.ReservationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ReservationManagerService implements ReservationManager {


    @Autowired
    ReservationRepository reservationRepository;

    @Override
    public Response getAllReservation() {
        Response response = new Response();
        response.setCode(200);
        response.setData(reservationRepository.findAll());
        response.setMsg("Get All Reservations");
        return response;
    }


    @Override
    public Response getAllUserReservation(String empID) {

        List<ReservationEntity> reservationEntityList = reservationRepository.findByEmpCode(empID);
        List<ReservationModel> reservationModelList = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

        for (ReservationEntity reservation : reservationEntityList) {
            String strStartDate = dateFormat.format(reservation.getStartDate());
            String strEndDate = dateFormat.format(reservation.getStartDate());
            ReservationModel reservationModel = new ReservationModel();

            reservationModel.setId(reservation.getId());
            reservationModel.setTitle(reservation.getTitle());
            reservationModel.setStart(strStartDate);
            reservationModel.setEnd(strEndDate);

            ExtendedReservationForGetReservation extendedReservationForGetReservation= new ExtendedReservationForGetReservation();
            extendedReservationForGetReservation.setCalendar(reservation.getCalendar());
            extendedReservationForGetReservation.setDescription(reservationModel.getDescription());
            reservationModel.setExtendedProps(extendedReservationForGetReservation);
            reservationModelList.add(reservationModel);

        }

        Response response = new Response();
        response.setCode(200);
        response.setData(reservationModelList);
        response.setMsg("Get user's Reservations");
        return response;
    }

    @Override
    public Response createReservation(String empID, ReservationModel reservationModel) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        Date startDate = new Date();
        Date endDate = new Date();

        try {
            startDate = formatter.parse(reservationModel.getStart());
            endDate = formatter.parse(reservationModel.getEnd());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        ReservationEntity reservationEntity = new ReservationEntity();
        reservationEntity.setEmpCode(empID);
        reservationEntity.setTitle(reservationModel.getTitle());
        reservationEntity.setReservationType(reservationModel.getReservationType());
        reservationEntity.setStatus(reservationModel.getStatus());
        reservationEntity.setStartDate(startDate);
        reservationEntity.setEndDate(endDate);
        reservationEntity.setUserName(reservationModel.getUserName());
        reservationEntity.setDescription(reservationModel.getDescription());

        List<ReservationEntity> reservationEntityList = new ArrayList<>();

        reservationEntityList.add(reservationEntity);
        reservationRepository.saveAll(reservationEntityList);

        Response response = new Response();
        response.setCode(201);
        response.setMsg("Create Reservation Successful");
        response.setData(reservationRepository.findById(reservationEntity.getId()));
        return response;
    }

    @Override
    public Response editReservation(int id, ReservationModel reservationModel) {

        Response response = new Response();
        Optional<ReservationEntity> reservation = reservationRepository.findById(id);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        Date startDate = new Date();
        Date endDate = new Date();


        if (reservation.isPresent()) {
            ReservationEntity obj = reservation.get();
            try {
                startDate = formatter.parse(reservationModel.getStart());
                endDate = formatter.parse(reservationModel.getEnd());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            obj.setTitle(reservationModel.getTitle());
            obj.setReservationType(reservationModel.getReservationType());
            obj.setStatus(reservationModel.getStatus());
            obj.setStartDate(startDate);
            obj.setEndDate(endDate);
            obj.setUserName(reservationModel.getUserName());
            obj.setDescription(reservationModel.getDescription());

            List<ReservationEntity> reservationEntityList = new ArrayList<>();

            reservationEntityList.add(obj);
            reservationRepository.saveAll(reservationEntityList);


            response.setCode(200);
            response.setMsg("Update Reservation Successful");
            response.setData(reservationRepository.findById(obj.getId()));
            return response;
        } else {
            response.setCode(404);
            response.setMsg("Not Found");
            return response;
        }

    }

    @Override
    public Response deleteReservation(int id) {
        Response response = new Response();
        response.setCode(200);
        response.setMsg("Delete Reservation Successful");
        return response;
    }
}
