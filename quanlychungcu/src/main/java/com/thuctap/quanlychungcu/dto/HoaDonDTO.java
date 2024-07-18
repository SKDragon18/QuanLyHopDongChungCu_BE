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
public class HoaDonDTO {
    private long soHoaDon;
    private Timestamp thoiGianDong;
    private BigDecimal tongHoaDon;
    private HopDongDTO hopDong;
    private YeuCauDichVuDTO yeuCauDichVu;

}
