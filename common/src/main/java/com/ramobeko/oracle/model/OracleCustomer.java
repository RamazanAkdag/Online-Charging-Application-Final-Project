package com.ramobeko.oracle.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ramobeko.oracle.util.Role;
import jakarta.persistence.*;


import java.util.Date;

@Entity
@Table(name = "t_customer", schema = "AOM")
public class OracleCustomer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cust_id")
    private Long id;

    @Column(name = "cust_name", nullable = false)
    private String name;

    @Column(name = "cust_mail", nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(name = "cust_password", nullable = false)
    private String password;  // 🔑 Required for Spring Security

    @Column(name = "cust_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role; // 🎭 Enum for roles (USER, ADMIN)

    @Column(name = "cust_start_date")
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(name = "cust_address")
    private String address;

    @Column(name = "cust_status")
    private String status;

   }