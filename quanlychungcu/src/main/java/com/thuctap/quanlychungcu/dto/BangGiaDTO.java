package com.thuctap.quanlychungcu.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BangGiaDTO {
    private long idBangGia;
    private String noiDung;
    private Timestamp thoiGianBatDau;
    private Timestamp thoiGianKetThuc;
    private Boolean apDung;
    private BanQuanLyDTO banQuanLy;
}
