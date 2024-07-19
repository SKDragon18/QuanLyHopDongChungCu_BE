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
public class CanHoBangGiaDTO {
    private int idCanHo;
    private String soPhong;
    private int tang;
    private String lo;
    private BigDecimal giaGoc;
    private BigDecimal giaKhuyenMai;
}
