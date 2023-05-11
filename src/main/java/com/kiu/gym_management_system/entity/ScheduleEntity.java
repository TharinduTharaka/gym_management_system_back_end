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
@Table(name = "schedule")
public class ScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private int id;

    @Column(name = "title")
    private String title;
    @Column(name = "date")
    private Date date;
    @Column(name = "week_day")
    private String weekDay;
    @Column(name = "description")
    private String description;
    @Column(name = "instructor_name")
    private String instructorName;
    @Column(name = "status")
    private int Status;

}
