package com.thuctap.quanlychungcu.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaiKhoanDTO {
    private String tenDangNhap;
    private QuyenDTO quyen;
    private Boolean khoa;
    private KhachHangDTO khachHang;
    private BanQuanLyDTO banQuanLy;
    private String token;
    private List<byte[]> hinhAnhList;
}
