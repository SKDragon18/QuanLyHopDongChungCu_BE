package com.thuctap.quanlychungcu.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DangKyDTO {
    private String tenDangNhap;
    private String matKhau;
    private String matKhauNhapLai;
    private String ho;
    private String ten;
    private String sdt;
    private String email;
    private String cmnd;
}
