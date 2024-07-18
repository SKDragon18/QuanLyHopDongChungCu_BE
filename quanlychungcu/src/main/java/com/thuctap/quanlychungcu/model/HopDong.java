package com.thuctap.quanlychungcu.model;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "HOPDONG")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HopDong {
    @Id
    @Column(name = "IDHOPDONG",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idHopDong;

    @Column(name="NGAYLAP",nullable = false)
    private Timestamp ngayLap;

    @ManyToOne
    @JoinColumn(name = "MAKHACHHANG")
    private KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "IDCANHO",nullable = true)
    private CanHo canHo;

    @Column(name="GIATRI",nullable = false)
    private BigDecimal giaTri;

    @Column(name="NGAYBATDAU", nullable = false)
    private Timestamp ngayBatDau;

    @Column(name="THOIHAN", nullable = false)
    private Timestamp thoiHan;

    @Column(name="CHUKY", nullable = false)
    private int chuKy;

    @Column(name="TRANGTHAI", nullable = false)
    private Boolean trangThai;

    @OneToMany(mappedBy = "hopDong")
    List<CTHopDong> chiTietHopDongList;
}
