package com.thuctap.quanlychungcu.model;
import java.math.BigDecimal;
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
@Table(name = "CANHO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CanHo {
    @Id
    @Column(name = "IDCANHO",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCanHo;

    @Column(name="SOPHONG",nullable = false)
    private int soPhong;

    @Column(name="TANG",nullable = false)
    private int tang;

    @ManyToOne
    @JoinColumn(name = "IDLOAIPHONG")
    private LoaiPhong loaiPhong;

    @Column(name = "DIENTICH",nullable = true)
    private float dienTich;

    @Column(name="TIENNGHI",nullable = true)
    private String tienNghi;

    @Column(name="MOTA",nullable = true)
    private String moTa;

    @Column(name="GIATHUE",nullable = false)
    private BigDecimal giaThue;

    @OneToMany(mappedBy = "canHo")
    List<HinhAnh> hinhAnhList;
}
