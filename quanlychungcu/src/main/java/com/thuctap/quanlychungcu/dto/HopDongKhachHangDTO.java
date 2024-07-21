package com.thuctap.quanlychungcu.dto;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HopDongKhachHangDTO {
    private long idHopDong;
    private Timestamp ngayLap;
    private BigDecimal giaTri;
    private Timestamp ngayBatDau;
    private Timestamp thoiHan;
    private int chuKy;
    private Boolean trangThai;
    private Boolean giaHan;
    private KhachHangDTO khachHang;
    private CanHoDTO canHo;
    List<DieuKhoanDTO> dieuKhoanList;
}
