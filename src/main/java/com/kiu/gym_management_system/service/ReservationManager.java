package com.kiu.gym_management_system.service;

import com.kiu.gym_management_system.model.reservation.ReservationModel;
import com.kiu.gym_management_system.response.Response;

public interface ReservationManager {

    Response getAllReservation();

    Response getAllUserReservation(String empID);

    Response getUserReservation(String empID, int id);

    Response getAdminFilterReservationData(int id, int status);
    Response getAdminReservationData(int id);

    Response getAllAdminReservationData(int id);

    Response createReservation(String empID, ReservationModel reservationModel);

    Response editReservation(int id, ReservationModel reservationModel);
    Response editStatusReservation(int status, int id);

    Response getUserFilterReservationData(String empID, int status);

    Response deleteReservation(int status,int id);
}
