package com.thuctap.quanlychungcu.model;

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
@Table(name = "BANGGIA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BangGia {
    
    @Id
    @Column(name = "IDBANGGIA",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idBangGia;

    @Column(name="NOIDUNG",nullable = true)
    private String noiDung;

    @Column(name="THOIGIANBATDAU",nullable=false)
    private Timestamp thoiGianBatDau;

    @Column(name="THOIGIANKETTHUC",nullable=false)
    private Timestamp thoiGianKetThuc;

    @Column(name="APDUNG",nullable = false)
    private Boolean apDung;

    @ManyToOne
    @JoinColumn(name = "MABANQUANLY")
    private BanQuanLy banQuanLy;
}
