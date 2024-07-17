package com.thuctap.quanlychungcu.model;
import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiaCPKTXPK implements Serializable{
    @Column(name="IDBANGGIA", nullable = false)
    private long idBangGia;

    @Column(name="IDCPKTX", nullable = false)
    private long idCPKTX;
}
