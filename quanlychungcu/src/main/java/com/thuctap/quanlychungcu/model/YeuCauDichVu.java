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
@Table(name = "YEUCAUDICHVU")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YeuCauDichVu {
    @Id
    @Column(name = "IDYEUCAUDICHVU",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idYeuCauDichVu;

    @ManyToOne
    @JoinColumn(name = "MAKHACHHANG")
    private KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "IDDICHVU")
    private DichVu dichVu;

    @ManyToOne
    @JoinColumn(name = "MABANQUANLY")
    private BanQuanLy banQuanLy;

    @Column(name="GIATRA", nullable = false)
    private BigDecimal giaTra;

    @Column(name="NGAYYEUCAU",nullable = false)
    private Timestamp ngayYeuCau;

    @Column(name="THOIHAN", nullable = false)
    private Timestamp thoiHan;

    @Column(name = "CHUKY", nullable = false)
    private int chuKy;

    @Column(name="TRANGTHAI", nullable = false)
    private Boolean trangThai;

    @Column(name="YEUCAU",nullable = true)
    private int yeuCau;

    @Column(name="DUYET",nullable = true)
    private int duyet;

    @OneToMany(mappedBy = "yeuCauDichVu")
    List<CTYeuCauDichVu> chiTietYeuCauDichVuList;
}
