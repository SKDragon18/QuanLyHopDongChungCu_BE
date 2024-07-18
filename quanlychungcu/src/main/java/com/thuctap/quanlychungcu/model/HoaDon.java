package com.thuctap.quanlychungcu.model;
import java.math.BigDecimal;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "HOADON")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HoaDon {
    @Id
    @Column(name = "SOHOADON",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long soHoaDon;

    @Column(name="THOIGIANDONG",nullable = false)
    private Timestamp thoiGianDong;

    @Column(name="TONGHOADON", nullable = false)
    private BigDecimal tongHoaDon;

    @ManyToOne
    @JoinColumn(name = "IDHOPDONG", nullable = true)
    private HopDong hopDong;

    @ManyToOne
    @JoinColumn(name = "IDYEUCAUDICHVU", nullable = true)
    private YeuCauDichVu yeuCauDichVu;

}
