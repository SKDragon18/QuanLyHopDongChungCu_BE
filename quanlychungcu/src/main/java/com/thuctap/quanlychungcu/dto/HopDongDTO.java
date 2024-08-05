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
public class HopDongDTO {
    private long idHopDong;
    private Timestamp ngayLap;
    private KhachHangDTO khachHang;
    private CanHoDTO canHo;
    private BigDecimal giaTri;
    private Timestamp ngayBatDau;
    private Timestamp thoiHan;
    private Timestamp thoiGianDong;
    private int chuKy;
    private int chuKyDong;
    private Boolean trangThai;
    private int yeuCau;
    private int duyet;
    List<CTHopDongDTO> chiTietHopDongList;
}
