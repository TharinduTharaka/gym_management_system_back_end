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
@Table(name = "members")
public class MembersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "status")
    private int status;
    @Column(name = "phone_home")
    private String phoneHome;
    @Column(name = "phone_personal")
    private String phonePersonal;
    @Column(name = "address")
    private String address;
    @Column(name = "age")
    private int age;
    @Column(name = "email")
    private String email;
    @Column(name = "nic")
    private String nic;
    @Column(name = "reg_number")
    private String regNumber;
    @Column(name = "height")
    private double height;
    @Column(name = "weight")
    private double weight;
    @Column(name = "bmi")
    private double bmi;
    @Column(name = "gym_name")
    private String gymName;
    @Column(name = "phone_trainer")
    private String phoneTrainer;
    @Column(name = "trainer_name")
    private String trainerName;
    @Column(name = "health_condition")
    private String healthCondition;
    @Column(name = "medical_report_file_name")
    private String medicalReportFileName;
    @Column(name = "doctor_name")
    private String doctorName;
    @Column(name = "hospital_contact_number")
    private String hospitalContactNumber;
    @Column(name = "hospital_location")
    private String hospitalLocation;
    @Column(name = "payment_file")
    private String paymentFile;

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
