package com.thuctap.quanlychungcu.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoiMatKhauDTO {
    private String tenDangNhap;
    private String matKhauCu;
    private String matKhauMoi;
}
