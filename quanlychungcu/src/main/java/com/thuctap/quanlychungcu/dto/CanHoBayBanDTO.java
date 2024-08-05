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
public class CanHoBayBanDTO {
    private int idCanHo;
    private String soPhong;
    private int tang;
    private Character lo;
    private LoaiPhongDTO loaiPhong;
    private BigDecimal dienTich;
    private String tienNghi;
    private String moTa;
    private BigDecimal giaThue;
    private int chuKy;
    private int chuKyDong;
    private BigDecimal giaKhuyenMai;
    private int trangThaiThue;
    private List<DieuKhoanDTO> dieuKhoanList;
    private List<byte[]> hinhAnhList;
    
}
