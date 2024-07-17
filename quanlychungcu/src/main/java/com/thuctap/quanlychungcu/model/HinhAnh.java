package com.thuctap.quanlychungcu.model;
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
@Table(name = "HINHANH")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HinhAnh {
    @Id
    @Column(name = "IDHINHANH",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idHinhAnh;

    @Column(name="DUONGDAN",nullable=false)
    private String duongDan;

    @Column(name="TENDANGNHAP", nullable = true, length = 50)
    private String tenDangNhap;

    @ManyToOne
    @JoinColumn(name = "IDCANHO",nullable = true)
    private CanHo canHo;
}
