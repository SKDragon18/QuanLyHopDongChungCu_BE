package com.thuctap.quanlychungcu.dto;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CPKTXDTO {
    private int idCPKTX;
    private String tenDichVu;
    private BigDecimal giaHienTai;
}
