package com.thuctap.quanlychungcu.model;
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
@Table(name = "LOAIPHONG")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoaiPhong {
    @Id
    @Column(name = "IDLOAIPHONG",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idLoaiPhong;

    @Column(name = "TENLOAIPHONG",nullable = false, length = 20)
    private String tenLoaiPhong;

}
