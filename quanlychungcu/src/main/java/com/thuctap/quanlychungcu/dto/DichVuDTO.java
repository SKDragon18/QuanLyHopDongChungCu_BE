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
public class DichVuDTO {
    private int idDichVu;
    private String tenDichVu;
    private String ghiChu;
    private int chuKy;
    private BigDecimal giaHienTai;
    private Boolean trangThai;
    private List<DieuKhoanDTO> dieuKhoanList;
}
