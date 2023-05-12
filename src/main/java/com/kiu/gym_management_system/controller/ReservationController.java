package com.kiu.gym_management_system.controller;


import com.kiu.gym_management_system.model.reservation.ReservationModel;
import com.kiu.gym_management_system.repository.ReservationRepository;
import com.kiu.gym_management_system.response.Response;
import com.kiu.gym_management_system.service.impl.ReservationManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gym/reservation")
@CrossOrigin(origins = "*")
public class ReservationController {

    @Autowired
    private ReservationManagerService reservationManagerService;

    @GetMapping("/get-all-reservation")
    public Response getAllReservation() {
        return reservationManagerService.getAllReservation();
    }

    @GetMapping("/get-user-reservation/{emp_id}/{config}")
    public Response getAllUserReservation(@PathVariable String emp_id,@PathVariable String config) {
        return reservationManagerService.getAllUserReservation(emp_id);
    }

    @PostMapping("/create-reservation/{emp_id}")
    public Response createReservation(@PathVariable String emp_id, @RequestBody ReservationModel reservationModel) {
        return reservationManagerService.createReservation(emp_id, reservationModel);
    }

    @PutMapping("/update-reservation/{id}")
    public Response editReservation(@PathVariable int id, @RequestBody ReservationModel reservationModel) {
        return reservationManagerService.editReservation(id, reservationModel);
    }


    @DeleteMapping("/delete-reservation/{id}")
    public Response deleteReservation(@PathVariable int id) {
        return reservationManagerService.deleteReservation(id);
    }

}
