package com.thuctap.quanlychungcu.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "BANQUANLY")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BanQuanLy {
    @Id
    @Column(name = "MA",nullable = false, length = 50)
    private String ma;

    @Column(name="HO",nullable = false, length = 50)
    private String ho;

    @Column(name="TEN",nullable = false,length = 100)
    private String ten;

    @Column(name="SDT",nullable = true,length = 10)
    private String sdt;

    @Column(name="EMAIL",nullable = false,length = 128)
    private String email;

    @Column(name="DIACHI",nullable = true,length = 200)
    private String diaChi;

    @Column(name="CMND",nullable = false, length = 12)
    private String cmnd;

    @Column(name="NGHI",nullable = false)
    private Boolean nghi;

}
