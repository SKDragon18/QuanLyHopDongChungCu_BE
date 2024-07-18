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
public class CanHoDTO {
    private int idCanHo;
    private String soPhong;
    private int tang;
    private String lo;
    private LoaiPhongDTO loaiPhong;
    private float dienTich;
    private String tienNghi;
    private String moTa;
    private BigDecimal giaThue;
    private Boolean trangThai;
    private int chuKy;
    private List<DieuKhoanDTO> dieuKhoanList;
    private List<byte[]> hinhAnhList;
    
}
