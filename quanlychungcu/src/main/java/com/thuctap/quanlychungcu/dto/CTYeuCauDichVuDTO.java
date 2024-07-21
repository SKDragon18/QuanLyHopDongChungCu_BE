package com.thuctap.quanlychungcu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CTYeuCauDichVuDTO {
    private long idCTYeuCauDichVu;
    private YeuCauDichVuDTO yeuCauDichVu;
    private DieuKhoanDTO dieuKhoan;
}
