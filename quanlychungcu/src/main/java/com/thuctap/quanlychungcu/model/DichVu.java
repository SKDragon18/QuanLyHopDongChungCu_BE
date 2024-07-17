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
@Table(name = "DICHVU")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DichVu {
    @Id
    @Column(name = "IDDICHVU",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idDichVu;

    @Column(name = "TENDICHVU",nullable = false, length = 50)
    private String tenDichVu;

    @Column(name="GHICHU", nullable = true)
    private String ghiChu;

    @Column(name="GIAHIENTAI", nullable = false)
    private BigDecimal giaHienTai;

}
