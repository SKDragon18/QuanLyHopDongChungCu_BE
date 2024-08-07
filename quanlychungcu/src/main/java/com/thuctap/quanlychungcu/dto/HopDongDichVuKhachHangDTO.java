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
public class HopDongDichVuKhachHangDTO {
    private long idYeuCauDichVu;
    private HopDongDTO hopDong;
    private DichVuDTO dichVu;
    private BanQuanLyDTO banQuanLy;
    private BigDecimal giaTra;
    private Timestamp ngayYeuCau;
    private Timestamp thoiHan;
    private int chuKy;
    private Boolean trangThai;
    private Boolean giaHan;
    private int yeuCau;
    private int duyet;
    List<DieuKhoanDTO> dieuKhoanList;
}
