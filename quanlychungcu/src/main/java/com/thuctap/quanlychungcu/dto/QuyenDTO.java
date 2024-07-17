package com.thuctap.quanlychungcu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuyenDTO {
    private int idQuyen;
    private String tenQuyen;
}
