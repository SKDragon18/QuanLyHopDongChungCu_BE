package com.thuctap.quanlychungcu.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KhachHangDTO {
    private String maKhachHang;
    private String ho;
    private String ten;
    private String sdt;
    private String email;
    private LocalDate ngaySinh;
}
