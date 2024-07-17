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
@Table(name = "DIEUKHOAN")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DieuKhoan {
    @Id
    @Column(name = "MA",nullable = false, length = 10)
    private String ma;

    @Column(name="NOIDUNG", nullable = false)
    private String noiDung;

}
