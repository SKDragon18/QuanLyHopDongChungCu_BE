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
public class DichVuDTO {
    private int idDichVu;
    private String tenDichVu;
    private String ghiChu;
    private BigDecimal giaHienTai;
}
