package com.kiu.gym_management_system.service;

import com.kiu.gym_management_system.model.reservation.ReservationModel;
import com.kiu.gym_management_system.response.Response;

public interface ReservationManager {

    Response getAllReservation();

    Response getAllUserReservation(String empID);

    Response createReservation(String empID, ReservationModel reservationModel);

    Response editReservation(int id, ReservationModel reservationModel);

    Response deleteReservation(int id);
}
