package com.thuctap.quanlychungcu.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TAIKHOAN")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaiKhoan {
    @Id
    @Column(name = "TENDANGNHAP", nullable = false, length = 50)
    private String tenDangNhap;

    @Column(name="MATKHAU",nullable = false, length = 50)
    private String matKhau;

    @Column(name="KHOA",nullable = false)
    private Boolean khoa;

    @ManyToOne
    @JoinColumn(name = "IDQUYEN")
    private Quyen quyen;

    @OneToMany(mappedBy = "taiKhoan")
    List<HinhAnh> hinhAnhList;
}
