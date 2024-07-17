package com.thuctap.quanlychungcu.model;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CPKTX")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CPKTX {
    @Id
    @Column(name = "IDCPKTX",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCPKTX;

    @Column(name= "TENDICHVU", nullable = false, length = 50)
    private String tenDichVu;

    @Column(name= "GIAHIENTAI", nullable = false)
    private BigDecimal giaHienTai;
}
