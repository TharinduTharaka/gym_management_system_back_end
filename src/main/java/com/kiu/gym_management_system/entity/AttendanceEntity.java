package com.kiu.gym_management_system.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "attendance")
public class AttendanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private int id;

    @Column(name= "emp_code")
    private String empCode;

    @Column(name= "empName")
    private  String empName;

    @Column(name= "date")
    private  String date;


    @Column(name= "inTime")
    private String inTime;

    @Column(name= "outTime")
    private String outTime;

    @Column(name= "workDuration")
    private String workDuration;



}
