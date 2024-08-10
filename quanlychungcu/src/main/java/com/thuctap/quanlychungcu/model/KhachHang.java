package com.thuctap.quanlychungcu.model;
import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "KHACHHANG")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KhachHang {
    @Id
    @Column(name = "MAKHACHHANG",nullable = false, length = 50)
    private String maKhachHang;

    @Column(name="HO",nullable = false, length = 50)
    private String ho;

    @Column(name="TEN", nullable = false, length = 100)
    private String ten;

    @Column(name="SDT",nullable = false, length = 10)
    private String sdt;

    @Column(name="EMAIL", nullable = false, length = 128)
    private String email;

    @Column(name="NGAYSINH",nullable = true, length = 12)
    private LocalDate ngaySinh;
}
