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
@Table("SUPPLIER")
public class Supplier {
    @Id
    @Column("idsupplier")
    private Integer idSupplier;
    private String name;
    @Column("jobtype")
    private String jobType;
    private String country;
    private String city;
    private String address;
}
