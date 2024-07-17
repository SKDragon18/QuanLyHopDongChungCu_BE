package com.thuctap.quanlychungcu.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaiKhoanDTO {
    private String tenDangNhap;
    private String matKhau;
    private QuyenDTO quyen;
    private Boolean khoa;
}
