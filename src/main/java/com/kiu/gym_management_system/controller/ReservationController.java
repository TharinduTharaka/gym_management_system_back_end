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

    @GetMapping("/get-all-reservation/{config}")
    public Response getAllReservation(@PathVariable String config) {
        return reservationManagerService.getAllReservation();
    }

    @GetMapping("/get-user-all-reservation/{emp_id}/{config}")
    public Response getAllUserReservation(@PathVariable String emp_id,@PathVariable String config) {
        return reservationManagerService.getAllUserReservation(emp_id);
    }

    @GetMapping("/get-user-reservation/{emp_id}/{id}")
    public Response getUserReservation(@PathVariable String emp_id,
                                    @PathVariable int id) {
        return reservationManagerService.getUserReservation(emp_id, id);
    }

    @GetMapping("/get-admin-reservation-data/{id}")
    public Response getAdminReservationData(@PathVariable int id) {
        return reservationManagerService.getAdminReservationData(id);
    }

    @GetMapping("/get-user-filter-reservation/{emp_id}")
    public Response getUserFilterReservationData(@PathVariable String emp_id,
                                             @RequestParam(value = "status") int status) {
        return reservationManagerService.getUserFilterReservationData(emp_id, status);
    }

    @PostMapping("/create-reservation/{emp_id}")
    public Response createReservation(@PathVariable String emp_id,
                                      @RequestBody ReservationModel reservationModel) {
        return reservationManagerService.createReservation(emp_id, reservationModel);
    }

    @PutMapping("/update-reservation/{id}")
    public Response editReservation(@PathVariable int id,
                                    @RequestBody ReservationModel reservationModel) {
        return reservationManagerService.editReservation(id, reservationModel);
    }

    @PutMapping("/update-status-reservation/{id}")
    public Response editStatusReservation(@PathVariable int id,
                                    @RequestParam(value = "status") int status) {
        return reservationManagerService.editStatusReservation(status,id);
    }


    @PutMapping("/delete-reservation/{id}")
    public Response deleteReservation(@PathVariable int id,
                                      @RequestParam(value = "status") int status) {
        return reservationManagerService.deleteReservation(status,id);
    }

}
