package com.thuctap.quanlychungcu.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "QUYEN")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quyen {
    
    @Id
    @Column(name = "IDQUYEN",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idQuyen;

    @Column(name = "TENQUYEN", nullable = false, length = 15)
    private String tenQuyen;

    @OneToMany(mappedBy = "quyen")
    private List<TaiKhoan> taiKhoan;
}
