package com.thuctap.quanlychungcu.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThanhToanDTO {
    private String url;
    private String vnp_TxnRef;
    private String vnp_CreateDate;
    private String loaiGiaoDich;
    private HopDongDTO hopDong;
    private YeuCauDichVuDTO yeuCauDichVu;
}
