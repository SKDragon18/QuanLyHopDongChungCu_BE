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
@Table(name = "GIADICHVU")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiaDichVu {
    @EmbeddedId
    private GiaDichVuPK giaDichVuPK;

    @ManyToOne
    @MapsId("idBangGia")
    @JoinColumn(name = "IDBANGGIA")
    private BangGia bangGia;

    @ManyToOne
    @MapsId("idDichVu")
    @JoinColumn(name = "IDDICHVU")
    private DichVu dichVu;

    @Column(name="GIA", nullable = false)
    private BigDecimal gia;
}
