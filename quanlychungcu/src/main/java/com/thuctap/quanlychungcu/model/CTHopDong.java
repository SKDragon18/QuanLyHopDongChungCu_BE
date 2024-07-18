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
@Table(name = "CTHOPDONG")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CTHopDong {
    @Id
    @Column(name = "IDCTHOPDONG",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idCTHopDong;

    @ManyToOne
    @JoinColumn(name = "IDHOPDONG")
    private HopDong hopDong;

    @ManyToOne
    @JoinColumn(name = "MADIEUKHOAN")
    private DieuKhoan dieuKhoan;
}
