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
@Table(name = "iclock_transaction")
public class IclockTransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private int id;

    @Column(name= "punch_time")
    private Date punch_time;

    @Column(name= "emp_code")
    private String empCode;

    @Column(name= "area_alias")
    private String areaAlias;

    @Column(name= "terminal_alias")
    private String terminalAlias;
}
