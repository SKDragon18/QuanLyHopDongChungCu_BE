package com.thuctap.quanlychungcu.dto;
import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DichVuBangGiaDTO {
    private int idDichVu;
    private String tenDichVu;
    private BigDecimal giaGoc;
    private BigDecimal giaKhuyenMai;
}
