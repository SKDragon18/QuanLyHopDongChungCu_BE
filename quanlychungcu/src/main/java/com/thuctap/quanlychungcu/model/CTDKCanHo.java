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
@Table(name = "CTDKCANHO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CTDKCanHo {
    @Id
    @Column(name = "IDCTDKCANHO",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idCTDKCanHo;

    @ManyToOne
    @JoinColumn(name = "IDCANHO")
    private CanHo canHo;

    @ManyToOne
    @JoinColumn(name = "MADIEUKHOAN")
    private DieuKhoan dieuKhoan;

}
