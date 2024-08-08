package com.thuctap.quanlychungcu.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.thuctap.quanlychungcu.config.VNPayConfig;
import com.thuctap.quanlychungcu.dto.ApiResponse;
import com.thuctap.quanlychungcu.dto.CheckReponse;
import com.thuctap.quanlychungcu.dto.HoaDonDTO;
import com.thuctap.quanlychungcu.dto.PaymentDTO;
import com.thuctap.quanlychungcu.dto.ThanhToanDTO;
import com.thuctap.quanlychungcu.model.HoaDon;
import com.thuctap.quanlychungcu.service.HoaDonService;
import com.thuctap.quanlychungcu.service.ThanhToanService;
import com.thuctap.quanlychungcu.utils.TimeTool;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/hoadon")
public class QuanLyHoaDonController {
    @Autowired
    HoaDonService hoaDonService;
    @Autowired
    ThanhToanService thanhToanService;

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
    

    @GetMapping("/thanhtoan/{id}")
    public ApiResponse<?> getURLVNPay(HttpServletRequest req,@PathVariable long id) throws UnsupportedEncodingException {
        HoaDon hoaDon = hoaDonService.findById(id);
        if(hoaDon==null){
            return ApiResponse.<ThanhToanDTO>builder().code(404)
            .message("Không tìm thấy hóa đơn").build();
        }
        if(hoaDon.getTrangThai()){
            return ApiResponse.<ThanhToanDTO>builder().code(400)
            .message("Hóa đơn đã thanh toán").build();
        }
        try{
            ThanhToanDTO thanhToanDTO = thanhToanService.createVNPPayment(req, hoaDon);
            return ApiResponse.<ThanhToanDTO>builder().code(200)
            .result(thanhToanDTO).build();
        }
        catch(Exception e){
            return ApiResponse.<ThanhToanDTO>builder().code(400)
            .message(e.getMessage()).build();
        }
    }
    @GetMapping("/payment_infor")
    public RedirectView getInforPayment(
        @RequestParam("vnp_TxnRef")String vnp_TxnRef,
        @RequestParam("vnp_ResponseCode")String vnp_ResponseCode,
        @RequestParam("vnp_TransactionNo")String vnp_TransactionNo) 
    {   
        try{
        //Trường hợp thành công
            if(vnp_ResponseCode.equals("00")){
                ThanhToanDTO thanhToanDTO = thanhToanService.getThanhToanDTO(vnp_TxnRef);
                System.out.println("Size: ");
                System.out.println(thanhToanService.getSizeGiaoDich());
                try{
                    HoaDon hoaDon = thanhToanDTO.getHoaDon();
                    hoaDon.setTrangThai(true);
                    hoaDon.setThoiGianDong(TimeTool.getNow());
                    hoaDon=hoaDonService.save(hoaDon);
                    thanhToanService.removeThanhToan(vnp_TxnRef);
                    return new RedirectView("http://localhost:5173/success");
                }
                catch(Exception e){
                    System.out.println("Lỗi thanh toán: "+e.getMessage());
                    thanhToanService.removeThanhToan(vnp_TxnRef);
                    return new RedirectView("http://localhost:5173/fail");
                }
                
            }
            else{//Hủy hoặc thất bại
                thanhToanService.removeThanhToan(vnp_TxnRef);
                return new RedirectView("http://localhost:5173/fail");
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
            return new RedirectView("http://localhost:5173/fail");
        }
    }

    
    // @GetMapping("/check-payment/{id}")
    // public ApiResponse<?> getInforPayment(@PathVariable long id) 
    // {
    //     HoaDon hoaDon = hoaDonService.findById(id);
    //     if(hoaDon==null){
    //         return ApiResponse.<String>builder().code(400)
    //         .message("Hóa đơn không tồn tại").build();
    //     }
    //     if(hoaDon.getThanhToan()){
    //         return ApiResponse.<String>builder().code(200)
    //         .result("1").build();
    //     }
    //     else{
    //         return ApiResponse.<String>builder().code(400)
    //         .message("Hóa đơn chưa thanh toán").build();
    //     }
    // }
    
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
