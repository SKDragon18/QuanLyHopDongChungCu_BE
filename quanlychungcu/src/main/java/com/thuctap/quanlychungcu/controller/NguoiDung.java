package com.thuctap.quanlychungcu.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.thuctap.quanlychungcu.dto.ApiResponse;
import com.thuctap.quanlychungcu.dto.AuthencicationResponse;
import com.thuctap.quanlychungcu.dto.BanQuanLyDTO;
import com.thuctap.quanlychungcu.dto.CanHoDTO;
import com.thuctap.quanlychungcu.dto.DangKyDTO;
import com.thuctap.quanlychungcu.dto.DangNhapDTO;
import com.thuctap.quanlychungcu.dto.DoiMatKhauDTO;
import com.thuctap.quanlychungcu.dto.KhachHangDTO;
import com.thuctap.quanlychungcu.dto.TaiKhoanDTO;
import com.thuctap.quanlychungcu.dto.VerifyDTO;
import com.thuctap.quanlychungcu.model.BanQuanLy;
import com.thuctap.quanlychungcu.model.CanHo;
import com.thuctap.quanlychungcu.model.KhachHang;
import com.thuctap.quanlychungcu.model.TaiKhoan;
import com.thuctap.quanlychungcu.service.AuthenticateService;
import com.thuctap.quanlychungcu.service.BanQuanLyService;
import com.thuctap.quanlychungcu.service.EmailService;
import com.thuctap.quanlychungcu.service.HinhAnhService;
import com.thuctap.quanlychungcu.service.KhachHangService;
import com.thuctap.quanlychungcu.service.TaiKhoanService;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/nguoidung")
public class NguoiDung {
    //dangnhap
    //dangky
    //doimatkhau
    //trangcanhan
    //doihinhanh
    //khachhang

    @Autowired
    TaiKhoanService taiKhoanService;
    @Autowired
    KhachHangService khachHangService;
    @Autowired
    BanQuanLyService banQuanLyService;
    @Autowired
    HinhAnhService hinhAnhService;
    @Autowired
    AuthenticateService authenticateService;
    @Autowired
    EmailService emailService;

    @PostMapping("/dangnhap")
    public ApiResponse<TaiKhoanDTO> dangNhap(@RequestBody DangNhapDTO dangNhapDTO) {
        if(dangNhapDTO.getTenDangNhap()==null||dangNhapDTO.getTenDangNhap().isEmpty()){
            return ApiResponse.<TaiKhoanDTO>builder().code(400)
            .message("Vui lòng nhập tên đăng nhập").build();
        }
        if(dangNhapDTO.getMatKhau()==null||dangNhapDTO.getMatKhau().isEmpty()){
            return ApiResponse.<TaiKhoanDTO>builder().code(400)
            .message("Vui lòng nhập mật khẩu").build();
        }
        TaiKhoan taiKhoan = taiKhoanService.findById(dangNhapDTO.getTenDangNhap());
        if(taiKhoan.getKhoa()){
            return ApiResponse.<TaiKhoanDTO>builder().code(403)
            .message("Tài khoản đã bị khóa").build();
        }
        String token = authenticateService.authenticate(dangNhapDTO, taiKhoan);
        if(token==null){
            return ApiResponse.<TaiKhoanDTO>builder().code(400)
            .message("Mật khẩu không đúng").build();
        }
        TaiKhoanDTO taiKhoanDTO = taiKhoanService.mapToTaiKhoanDTO(taiKhoan,true);
        taiKhoanDTO.setToken(token);
        return ApiResponse.<TaiKhoanDTO>builder().code(200)
            .result(taiKhoanDTO).build();
    }

    @PostMapping("/dangky")
    public ApiResponse<String> dangKy(@RequestBody DangKyDTO dangKyDTO) {
        if(!dangKyDTO.getMatKhau().equals(dangKyDTO.getMatKhauNhapLai())){
            return ApiResponse.<String>builder().code(400)
                .message("Mật khẩu nhập lại không đúng").build();
        }
        try{
            TaiKhoan taiKhoan = taiKhoanService.register(dangKyDTO);
            if(!taiKhoanService.isExistsById(taiKhoan.getTenDangNhap())){
                return ApiResponse.<String>builder().code(400)
                .message("Đăng ký thất bại").build();
            }
            else{
                return ApiResponse.<String>builder().code(200)
                .result("Đăng ký thành công").build();
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return ApiResponse.<String>builder().code(400)
                .message("Lỗi hệ thống: "+ e.getMessage()).build();
        }
        
    }

    @PostMapping("/verify")
    public ApiResponse<String> verifyToken(@RequestBody VerifyDTO verifyDTO) {
        // TaiKhoan taiKhoan = taiKhoanService.findById(dangNhapDTO.getTenDangNhap());
        // if(taiKhoan.getMatKhau().equals(dangNhapDTO.getMatKhau())){
        //     return new ResponseEntity<>("Mật khẩu không đúng", HttpStatus.BAD_REQUEST);
        // }
        // TaiKhoanDTO taiKhoanDTO = taiKhoanService.mapToTaiKhoanDTO(taiKhoan);
        try{
            if(authenticateService.introspect(verifyDTO.getToken())){
                return ApiResponse.<String>builder().code(200)
                .result("Đúng").build();
            }
            else{
                return ApiResponse.<String>builder().code(400)
                .message("Sai").build();
            }
        }   
        catch(Exception e){
            System.out.println(e.getMessage());
            return ApiResponse.<String>builder().code(400)
                .message(e.getMessage()).build();
        }
    }

    @PutMapping("/doimatkhau")
    public ApiResponse<String> doiMatKhau(@RequestBody DoiMatKhauDTO doiMatKhauDTO) {
        if(doiMatKhauDTO.getTenDangNhap()==null||doiMatKhauDTO.getTenDangNhap().isEmpty()){
            return ApiResponse.<String>builder().code(400)
                .message("Vui lòng nhập tên đăng nhập").build();
        }
        if(doiMatKhauDTO.getMatKhauCu()==null||doiMatKhauDTO.getMatKhauCu().isEmpty()){
            return ApiResponse.<String>builder().code(400)
                .message("Vui lòng nhập mật khẩu cũ").build();
        }
        if(doiMatKhauDTO.getMatKhauMoi()==null||doiMatKhauDTO.getMatKhauMoi().isEmpty()){
            return ApiResponse.<String>builder().code(400)
                .message("Vui lòng nhập mật khẩu mới").build();
        }
        try{
            TaiKhoan taiKhoan = taiKhoanService.findById(doiMatKhauDTO.getTenDangNhap());
            if(taiKhoan==null){
                return ApiResponse.<String>builder().code(404)
                    .message("Không tìm thấy tài khoản").build();
            }
            taiKhoan = taiKhoanService.changePassword(doiMatKhauDTO.getMatKhauCu(),
            doiMatKhauDTO.getMatKhauMoi(), taiKhoan);
            if(taiKhoan==null){
                return ApiResponse.<String>builder().code(400)
                    .message("Mật khẩu cũ không đúng").build();
            }
            return ApiResponse.<String>builder().code(200)
                    .result("Đổi mật khẩu thành công").build();
        }
        catch(Exception e){
            return ApiResponse.<String>builder().code(400)
                    .message(e.getMessage()).build();
        }
    }

    @GetMapping("/maxacnhan/{id}")
    public ApiResponse<String> getMaXacNhan(@PathVariable String id){
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        TaiKhoan taiKhoan = taiKhoanService.findById(id);
        if(taiKhoan==null){
            return ApiResponse.<String>builder().code(400)
                .message("Không tìm thấy tài khoản").build();
        }
        String email=null;
        if(banQuanLyService.isExistsById(id)){
            email = banQuanLyService.findById(id).getEmail();
        }
        else if(khachHangService.isExistsById(id)){
            email = khachHangService.findById(id).getEmail();
        }
        if(email==null){
            return ApiResponse.<String>builder().code(400)
                .message("Không thể lấy thông tin email").build();
        }
        try{
            emailService.sendSimpleEmail(email, "Mã xác thực website chung cư", uuid);
            return ApiResponse.<String>builder().code(200)
                .result(uuid).build();
        }
        catch(Exception e){
            return ApiResponse.<String>builder().code(400)
                .message(e.getMessage()).build();
        }
    }

    @PutMapping("/quenmatkhau")
    public ApiResponse<String> quenMatKhau(@RequestBody DoiMatKhauDTO doiMatKhauDTO) {
        if(doiMatKhauDTO.getTenDangNhap()==null||doiMatKhauDTO.getTenDangNhap().isEmpty()){
            return ApiResponse.<String>builder().code(400)
                .message("Vui lòng nhập tên đăng nhập").build();
        }
        if(doiMatKhauDTO.getMatKhauMoi()==null||doiMatKhauDTO.getMatKhauMoi().isEmpty()){
            return ApiResponse.<String>builder().code(400)
                .message("Vui lòng nhập mật khẩu mới").build();
        }
        try{
            TaiKhoan taiKhoan = taiKhoanService.findById(doiMatKhauDTO.getTenDangNhap());
            if(taiKhoan==null){
                return ApiResponse.<String>builder().code(404)
                    .message("Không tìm thấy tài khoản").build();
            }
            taiKhoan = taiKhoanService.changePassword(null,
            doiMatKhauDTO.getMatKhauMoi(), taiKhoan);
            if(taiKhoan==null){
                return ApiResponse.<String>builder().code(400)
                    .message("Lỗi hệ thống").build();
            }
            return ApiResponse.<String>builder().code(200)
                    .result("Đổi mật khẩu thành công").build();
        }
        catch(Exception e){
            return ApiResponse.<String>builder().code(400)
                    .message(e.getMessage()).build();
        }
    }
    

    @GetMapping("/khachhang/{id}")
    public ApiResponse<KhachHangDTO> getKhachHang(@PathVariable("id") String id){
        KhachHang khachHang = khachHangService.findById(id);
        if(khachHang==null){
            return ApiResponse.<KhachHangDTO>builder().code(404)
            .message("Không tìm thấy thông tin").build();
        }
        KhachHangDTO khachHangDTO = khachHangService.mapToKhachHangDTO(khachHang);
        return ApiResponse.<KhachHangDTO>builder().code(200)
            .result(khachHangDTO).build();
    }

    @PutMapping("/khachhang")
    public ApiResponse<KhachHangDTO> updateKhachHang(@RequestBody KhachHangDTO khachHangDTO){
        try{
            if(!khachHangService.isExistsById(khachHangDTO.getMaKhachHang())){
                return ApiResponse.<KhachHangDTO>builder().code(404)
                .message("Không tìm thấy khách hàng").build();
            }
            KhachHang khachHang = khachHangService.mapToKhachHang(khachHangDTO);
            khachHang = khachHangService.save(khachHang);
            KhachHangDTO khachHangDTO2 = khachHangService.mapToKhachHangDTO(khachHang);
            return ApiResponse.<KhachHangDTO>builder().code(200)
            .result(khachHangDTO2).build();
        }
        catch(Exception e){
            return ApiResponse.<KhachHangDTO>builder().code(400)
            .message(e.getMessage()).build();
        }
    }

    @GetMapping("/banquanly/{id}")
    public ApiResponse<BanQuanLyDTO> getBanQuanLy(@PathVariable("id") String id){
        BanQuanLy banQuanLy = banQuanLyService.findById(id); 
        if(banQuanLy==null){
            return ApiResponse.<BanQuanLyDTO>builder().code(404)
            .message("Không tìm thấy thông tin").build();
        }
        BanQuanLyDTO banQuanLyDTO = banQuanLyService.mapToBanQuanLyDTO(banQuanLy);
        return ApiResponse.<BanQuanLyDTO>builder().code(200)
            .result(banQuanLyDTO).build();
    }

    @GetMapping("/thongtin/{id}")
    public ApiResponse<TaiKhoanDTO> getTaiKhoan(@PathVariable("id") String id) {
        TaiKhoan taiKhoan = taiKhoanService.findById(id);
        if(taiKhoan==null){
            return ApiResponse.<TaiKhoanDTO>builder().code(404)
            .message("Không tìm thấy tài khoản").build();
        }
        TaiKhoanDTO taiKhoanDTO = taiKhoanService.mapToTaiKhoanDTO(taiKhoan, true);
        return ApiResponse.<TaiKhoanDTO>builder().code(200)
        .result(taiKhoanDTO).build();
    }
    
    @PostMapping("/doihinhanh")
    public ApiResponse<String> changeHinhAnh(@RequestParam("images")MultipartFile[] images, 
    @RequestParam("tenDangNhap") String tenDangNhap) {
        try{
            TaiKhoan taiKhoan = taiKhoanService.findById(tenDangNhap);
            String result = hinhAnhService.deleteAllAvatar(taiKhoan);
            if(!result.contains("thành công")){
                System.out.println(result);
            }
            for(MultipartFile image: images){
                hinhAnhService.downloadHinhAnh(image, taiKhoan, null);
            }
            return ApiResponse.<String>builder().code(200)
            .result("Đổi thành công").build();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return ApiResponse.<String>builder().code(400)
            .message(e.getMessage()).build();
        }
    }

}
