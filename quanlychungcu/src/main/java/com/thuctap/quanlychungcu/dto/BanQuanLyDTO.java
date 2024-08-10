package com.thuctap.quanlychungcu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BanQuanLyDTO {
    private String ma;
    private String ho;
    private String ten;
    private String sdt;
    private String email;
    private String diaChi;
    private Boolean nghi;
}
