package com.thuctap.quanlychungcu.dto;
import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YeuCauDichVuDTO {
    private long idYeuCauDichVu;
    private HopDongDTO hopDong;
    private DichVuDTO dichVu;
    private BigDecimal giaTra;
    private Timestamp ngayYeuCau;
    private Timestamp thoiHan;
    private int chuKy;
    private Boolean trangThai;
}
