package com.thuctap.quanlychungcu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CTHopDongDTO {
    private long idCTHopDong;
    private HopDongDTO hopDong;
    private DieuKhoanDTO dieuKhoan;
}
