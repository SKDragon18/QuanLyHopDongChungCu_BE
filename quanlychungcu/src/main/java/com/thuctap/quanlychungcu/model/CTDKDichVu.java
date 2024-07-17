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
@Table(name = "CTDKDICHVU")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CTDKDichVu {
    @Id
    @Column(name = "IDCTDKDICHVU",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idCTDKDichVu;

    @ManyToOne
    @JoinColumn(name = "IDDICHVU")
    private DichVu dichVu;

    @ManyToOne
    @JoinColumn(name = "MADIEUKHOAN")
    private DieuKhoan dieuKhoan;

}
