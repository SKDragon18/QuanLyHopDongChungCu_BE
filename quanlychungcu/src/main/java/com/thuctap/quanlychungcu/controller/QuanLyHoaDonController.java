package com.thuctap.quanlychungcu.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.responseVNPay.CheckReponse;
import com.thuctap.quanlychungcu.config.VNPayConfig;
import com.thuctap.quanlychungcu.dto.ApiResponse;
import com.thuctap.quanlychungcu.dto.HoaDonDTO;
import com.thuctap.quanlychungcu.dto.PaymentDTO;
import com.thuctap.quanlychungcu.model.HoaDon;
import com.thuctap.quanlychungcu.service.HoaDonService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/hoadon")
public class QuanLyHoaDonController {
    @Autowired
    HoaDonService hoaDonService;

    public Timestamp getNow(){
        Date date = new Date();
        return new Timestamp(date.getTime());
    }

    @GetMapping
    public ApiResponse<List<HoaDonDTO>> getAllHoaDon(){
        List<HoaDon> hoaDonList = hoaDonService.findAll();
        List<HoaDonDTO> hoaDonDTOList = hoaDonList.stream()
        .map(hoaDon -> hoaDonService.mapToHoaDonDTO(hoaDon)).toList();
        return ApiResponse.<List<HoaDonDTO>>builder().code(200)
                .result(hoaDonDTOList).build();
    }

    @GetMapping("/{id}")
    public ApiResponse<HoaDonDTO> getHoaDon(@PathVariable("id") long id){
        HoaDon hoaDon = hoaDonService.findById(id);
        if(hoaDon==null){
            return ApiResponse.<HoaDonDTO>builder().code(404)
                .message("Không tìm thấy").build();
        }
        HoaDonDTO hoaDonDTO = hoaDonService.mapToHoaDonDTO(hoaDon);
        return ApiResponse.<HoaDonDTO>builder().code(200)
                .result(hoaDonDTO).build();
    }

    @GetMapping("/khachhang/{id}")
    public ApiResponse<List<HoaDonDTO>> getAllHoaDon(@PathVariable("id") String id){
        List<HoaDon> hoaDonList = hoaDonService.findAll();
        List<HoaDonDTO> hoaDonDTOList = new ArrayList<>();
        for(HoaDon hoaDon:hoaDonList){
            if(hoaDon.getHopDong()!=null&&hoaDon.getHopDong().getKhachHang().getMaKhachHang().equals(id)){
                hoaDonDTOList.add(hoaDonService.mapToHoaDonDTO(hoaDon));
            }
            else if(hoaDon.getYeuCauDichVu()!=null&&hoaDon.getYeuCauDichVu()
            .getHopDong().getKhachHang().getMaKhachHang().equals(id)){
                hoaDonDTOList.add(hoaDonService.mapToHoaDonDTO(hoaDon));
            }
        }
        return ApiResponse.<List<HoaDonDTO>>builder().code(200)
                .result(hoaDonDTOList).build();
    }
    

    @PostMapping
    public ApiResponse<HoaDonDTO> insertHoaDon(@RequestBody HoaDonDTO hoaDonDTO) {
        HoaDon hoaDon = hoaDonService.mapToHoaDon(hoaDonDTO);
        try{
            hoaDon = hoaDonService.save(hoaDon);
            if(hoaDonService.isExistsById(hoaDon.getSoHoaDon())){
                HoaDonDTO hoaDonDTO2 = hoaDonService.mapToHoaDonDTO(hoaDon);
                return ApiResponse.<HoaDonDTO>builder().code(200)
                .result(hoaDonDTO2).build();
            }
            else{
                return ApiResponse.<HoaDonDTO>builder().code(400)
                .message("Thêm thất bại").build();
            }
        }
        catch(Exception e){
            return ApiResponse.<HoaDonDTO>builder().code(400)
                .message(e.getMessage()).build();
        }
    }

    @PostMapping("/create-payment")
    public ApiResponse<?> createVNPay(HttpServletRequest req,@RequestBody String amount) throws UnsupportedEncodingException {
        try{
            String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
            String vnp_IpAddr = VNPayConfig.getIpAddress(req);
            String vnp_TmnCode = VNPayConfig.vnp_TmnCode;
            String vnp_Version = VNPayConfig.vnp_Version;
            String vnp_Command = VNPayConfig.vnp_Command;
            long money = Integer.parseInt(amount)*100;

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

            HoaDon hoaDon = HoaDon.builder()
            .tongHoaDon(new BigDecimal(amount))
            .thoiGianDong(getNow())
            .thanhToan(false)
            .vnpCode(vnp_TxnRef)
            .build();
            hoaDon=hoaDonService.save(hoaDon);
            if(!hoaDonService.isExistsById(hoaDon.getSoHoaDon())){
                return ApiResponse.<PaymentDTO>builder().code(400)
                .message("Không thể tạo hóa đơn").build();
            }
            return ApiResponse.<PaymentDTO>builder().code(200)
            .result(new PaymentDTO(hoaDon.getSoHoaDon(),paymentUrl,vnp_TxnRef,vnp_CreateDate)).build();
        }
        catch(Exception e){
            return ApiResponse.<String>builder().code(400)
            .message("Lỗi tạo đơn thanh toán VNPay: "+e.getMessage()).build();
        }
    }

    @GetMapping("/payment_infor")
    public ApiResponse<?> getInforPayment(
        @RequestParam("vnp_TxnRef")String vnp_TxnRef,
        @RequestParam("vnp_ResponseCode")String vnp_ResponseCode,
        @RequestParam("vnp_TransactionNo")String vnp_TransactionNo) 
    {   
        try{
            HoaDon hoaDon = hoaDonService.findByVnpCode(vnp_TxnRef);
            if(hoaDon==null){
                return ApiResponse.<String>builder().code(400)
                .message("Không tìm thấy hóa đơn").build();
            }
            if(vnp_ResponseCode.equals("00")){
                hoaDon.setThanhToan(true);
                hoaDonService.save(hoaDon);
                return ApiResponse.<String>builder().code(200)
                .result("Thành công").build();
            }
            else{
                hoaDonService.deleteById(hoaDon.getSoHoaDon());
                return ApiResponse.<String>builder().code(400)
                .message("Thất bại").build();
            }
        }catch(Exception e){
            return ApiResponse.<String>builder().code(400)
                .message("Lỗi: "+ e.getMessage()).build();
        }
    }

    @GetMapping("/check-payment/{id}")
    public ApiResponse<?> getInforPayment(@PathVariable long id) 
    {
        HoaDon hoaDon = hoaDonService.findById(id);
        if(hoaDon==null){
            return ApiResponse.<String>builder().code(400)
            .message("Hóa đơn không tồn tại").build();
        }
        if(hoaDon.getThanhToan()){
            return ApiResponse.<String>builder().code(200)
            .result("1").build();
        }
        else{
            return ApiResponse.<String>builder().code(400)
            .message("Hóa đơn chưa thanh toán").build();
        }
    }
    
    @PostMapping("/checkpayment")
    public ApiResponse<?> checkPayment(HttpServletRequest req, @RequestBody PaymentDTO paymentDTO) throws IOException {
        //Command:querydr

        String vnp_RequestId = VNPayConfig.getRandomNumber(8);
        String vnp_Version = "2.1.0";
        String vnp_Command = "querydr";
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;
        String vnp_TxnRef = paymentDTO.getVnp_TxnRef();
        String vnp_OrderInfo = "Kiem tra ket qua GD OrderId:" + vnp_TxnRef;
        //String vnp_TransactionNo = req.getParameter("transactionNo");
        String vnp_TransDate = paymentDTO.getVnp_CreateDate();
        
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        
        String vnp_IpAddr = VNPayConfig.getIpAddress(req);
        
        JsonObject  vnp_Params = new JsonObject ();
        
        vnp_Params.addProperty("vnp_RequestId", vnp_RequestId);
        vnp_Params.addProperty("vnp_Version", vnp_Version);
        vnp_Params.addProperty("vnp_Command", vnp_Command);
        vnp_Params.addProperty("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.addProperty("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.addProperty("vnp_OrderInfo", vnp_OrderInfo);
        //vnp_Params.put("vnp_TransactionNo", vnp_TransactionNo);
        vnp_Params.addProperty("vnp_TransactionDate", vnp_TransDate);
        vnp_Params.addProperty("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.addProperty("vnp_IpAddr", vnp_IpAddr);
        
        String hash_Data= String.join("|", vnp_RequestId, vnp_Version, vnp_Command, vnp_TmnCode, vnp_TxnRef, vnp_TransDate, vnp_CreateDate, vnp_IpAddr, vnp_OrderInfo);
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hash_Data.toString());
        
        vnp_Params.addProperty("vnp_SecureHash", vnp_SecureHash);
        
        URL url = new URL (VNPayConfig.vnp_ApiUrl);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(vnp_Params.toString());
        wr.flush();
        wr.close();
        int responseCode = con.getResponseCode();
        System.out.println("nSending 'POST' request to URL : " + url);
        System.out.println("Post Data : " + vnp_Params);
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
        new InputStreamReader(con.getInputStream()));
        String output;
        StringBuffer response = new StringBuffer();
        while ((output = in.readLine()) != null) {
        response.append(output);
        }
        in.close();
        String responseString = response.toString();
        System.out.println(responseString);

        ObjectMapper objectMapper = new ObjectMapper();
        CheckReponse checkReponse = objectMapper.readValue(responseString, CheckReponse.class);

        return ApiResponse.<String>builder().code(200).result(checkReponse.getVnpTransactionStatus()).build();
    }
    
}
