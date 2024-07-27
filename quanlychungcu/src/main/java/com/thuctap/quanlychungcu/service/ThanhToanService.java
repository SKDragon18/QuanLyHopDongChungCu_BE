package com.thuctap.quanlychungcu.service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.thuctap.quanlychungcu.config.VNPayConfig;
import com.thuctap.quanlychungcu.dto.HopDongDTO;
import com.thuctap.quanlychungcu.dto.ThanhToanDTO;
import com.thuctap.quanlychungcu.dto.YeuCauDichVuDTO;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class ThanhToanService {
    private final Map<String, ThanhToanDTO> thanhToanCache = new ConcurrentHashMap<>();
    public void cacheThanhToan(ThanhToanDTO thanhToanDTO){
        try{
            thanhToanCache.put(thanhToanDTO.getVnp_TxnRef(), thanhToanDTO);
        }
        catch(Exception e){
            throw new Error("Danh sách thanh toán quá tải");
        }
    }
    public void removeThanhToan(String vnp_txnRef){
        thanhToanCache.remove(vnp_txnRef);
    }
    public ThanhToanDTO getThanhToanDTO(String vnp_txnRef){
        System.out.println("vnp_txnRef: "+vnp_txnRef);
        for(String key : thanhToanCache.keySet()){
            System.out.println("Key: "+key);
        }
        return thanhToanCache.get(vnp_txnRef);
    }

    public String getSizeGiaoDich(){
        return String.valueOf(thanhToanCache.size());
    }

    public ThanhToanDTO createVNPPayment(HttpServletRequest req,String loaiGiaoDich, 
    HopDongDTO hopDong, YeuCauDichVuDTO yeuCauDichVu)throws UnsupportedEncodingException{
        try{
            if(hopDong!=null&&yeuCauDichVu!=null)throw new Error("Yêu cầu không hợp lệ");
            BigDecimal mount = null;
            if(hopDong!=null){
                mount = hopDong.getGiaTri();
            }
            else if(yeuCauDichVu!=null){
                mount = yeuCauDichVu.getGiaTra();
            }
            if(mount==null){
                throw new Error("Lỗi lấy thông tin");
            }
            long money = -1;
            try{
                money= mount.multiply(BigDecimal.valueOf(100)).longValueExact();
            }
            catch(Exception e){
                throw new Error("Lỗi chuyển đổi");
            }
            if(money==-1){throw new Error("Lỗi chuyển đổi -1");}
            String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
            String vnp_IpAddr = VNPayConfig.getIpAddress(req);
            String vnp_TmnCode = VNPayConfig.vnp_TmnCode;
            String vnp_Version = VNPayConfig.vnp_Version;
            String vnp_Command = VNPayConfig.vnp_Command;
            

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", vnp_Version);
            vnp_Params.put("vnp_Command", vnp_Command);
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(money));
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_BankCode", "NCB");
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
            vnp_Params.put("vnp_OrderType", "other");
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
            
            cld.add(Calendar.MINUTE, 15);
            String vnp_ExpireDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
            
            //Mã hóa chuỗi thông tin
            List fieldNames = new ArrayList(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            Iterator itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = (String) itr.next();
                String fieldValue = (String) vnp_Params.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    //Build hash data
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    //Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }
            String queryUrl = query.toString();
            String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
            String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;
            ThanhToanDTO thanhToanDTO = ThanhToanDTO.builder()
            .hopDong(hopDong).yeuCauDichVu(yeuCauDichVu).loaiGiaoDich(loaiGiaoDich)
            .vnp_CreateDate(vnp_CreateDate).vnp_TxnRef(vnp_TxnRef).url(paymentUrl).build();
            cacheThanhToan(thanhToanDTO);
            return thanhToanDTO;
        }
        catch(Exception e){
            throw new Error(e.getMessage());
        }
    }
}
