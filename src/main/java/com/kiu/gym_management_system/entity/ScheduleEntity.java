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
    @Column(name = "emp_code")
    private String empCode;
    @Column(name = "title")
    private String title;
    @Column(name = "start_date")
    private String startDate;
    @Column(name = "end_date")
    private String endDate;
    @Column(name = "week_day")
    private int weekDay;
    @Column(name = "status")
    private int status;
    @Column(name = "description")
    private String description;
    @Column(name = "instructor_name")
    private String instructorName;

    @Column(name = "created_by")
    private String createBy;

    @Column(name = "deleted_by")
    private String deletedBy;

    @Column(name = "updated_by")
    private String updatedBy;
    @Column(name = "created_at")
    private Date createDate;

    @Column(name = "deleted_at")
    private Date deletedDate;

    @Column(name = "updated_at")
    private Date updatedDate;


}
