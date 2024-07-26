package com.thuctap.quanlychungcu.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO {
    private long soHoaDon;
    private String url;
    private String vnp_TxnRef;
    private String vnp_CreateDate;
}
