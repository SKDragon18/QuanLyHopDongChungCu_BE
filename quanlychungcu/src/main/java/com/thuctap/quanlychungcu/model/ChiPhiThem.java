package com.thuctap.quanlychungcu.model;
import java.math.BigDecimal;
import java.sql.Timestamp;

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
@Table(name = "CHIPHITHEM")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChiPhiThem {
    @Id
    @Column(name = "IDCPTHEM",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idCPThem;

    @ManyToOne
    @JoinColumn(name = "IDHOPDONG")
    private HopDong hopDong;

    @ManyToOne
    @JoinColumn(name = "IDCPKTX")
    private CPKTX chiPhiKTX;

    @Column(name="GIATRA", nullable = false)
    private BigDecimal giaTra;

    @Column(name="NGAYYEUCAU",nullable = false)
    private Timestamp ngayYeuCau;

    @Column(name="THOIHAN", nullable = false)
    private Timestamp thoiHan;


}
