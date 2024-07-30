package com.thuctap.quanlychungcu.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BangGiaCTDTO {
    private long idBangGia;
    private String noiDung;
    private BanQuanLyDTO banQuanLy;
    List<CanHoBangGiaDTO> canHoList;
    List<DichVuBangGiaDTO> dichVuList;

}
