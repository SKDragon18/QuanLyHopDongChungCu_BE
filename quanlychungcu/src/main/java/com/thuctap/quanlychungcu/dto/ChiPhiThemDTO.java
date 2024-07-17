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
public class ChiPhiThemDTO {
    private long idCPThem;
    private HopDongDTO hopDong;
    private CPKTXDTO chiPhiKTX;
    private BigDecimal giaTra;
    private Timestamp ngayYeuCau;
    private Timestamp thoiHan;
}
