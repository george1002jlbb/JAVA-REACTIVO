package com.carritocompras.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("CUSTOMER")
public class Customer {
    @Id
    @Column("idcustomer")
    private Integer idCustomer;
    private String sdi;
    @Column("firstname")
    private String firstName;
    @Column("lastname")
    private String lastName;
    @Column("phonenumber")
    private String phoneNumber;
    @Column("jobtype")
    private String jobType;
    private String email;
    private String country;
    private String city;
    private String address;
}
