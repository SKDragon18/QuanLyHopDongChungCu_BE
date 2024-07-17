package com.thuctap.quanlychungcu.model;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "GIACPKTX")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiaCPKTX {
    @EmbeddedId
    private GiaCPKTXPK giaCPKTXPK;

    @ManyToOne
    @MapsId("idBangGia")
    @JoinColumn(name = "IDBANGGIA")
    private BangGia bangGia;

    @ManyToOne
    @MapsId("idCPKTX")
    @JoinColumn(name = "IDCPKTX")
    private CPKTX chiPhiKTX;

    @Column(name="GIA", nullable = false)
    private BigDecimal gia;
}
