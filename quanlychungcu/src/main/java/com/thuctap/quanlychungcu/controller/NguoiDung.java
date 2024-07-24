package com.thuctap.quanlychungcu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thuctap.quanlychungcu.dto.BanQuanLyDTO;
import com.thuctap.quanlychungcu.dto.CanHoDTO;
import com.thuctap.quanlychungcu.dto.DangNhapDTO;
import com.thuctap.quanlychungcu.dto.DoiMatKhauDTO;
import com.thuctap.quanlychungcu.dto.KhachHangDTO;
import com.thuctap.quanlychungcu.dto.TaiKhoanDTO;
import com.thuctap.quanlychungcu.model.BanQuanLy;
import com.thuctap.quanlychungcu.model.CanHo;
import com.thuctap.quanlychungcu.model.KhachHang;
import com.thuctap.quanlychungcu.model.TaiKhoan;
import com.thuctap.quanlychungcu.service.BanQuanLyService;
import com.thuctap.quanlychungcu.service.KhachHangService;
import com.thuctap.quanlychungcu.service.TaiKhoanService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


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

    @PostMapping("/dangnhap")
    public ResponseEntity<?> postMethodName(@RequestBody DangNhapDTO dangNhapDTO) {
        if(dangNhapDTO.getTenDangNhap()==null||dangNhapDTO.getTenDangNhap().isEmpty()){
            return new ResponseEntity<>("Vui lòng nhập tên đăng nhập", HttpStatus.BAD_REQUEST);
        }
        if(dangNhapDTO.getMatKhau()==null||dangNhapDTO.getMatKhau().isEmpty()){
            return new ResponseEntity<>("Vui lòng nhập mật khẩu", HttpStatus.BAD_REQUEST);
        }
        TaiKhoan taiKhoan = taiKhoanService.findById(dangNhapDTO.getTenDangNhap());
        if(taiKhoan.getMatKhau().equals(dangNhapDTO.getMatKhau())){
            return new ResponseEntity<>("Mật khẩu không đúng", HttpStatus.BAD_REQUEST);
        }
        TaiKhoanDTO taiKhoanDTO = taiKhoanService.mapToTaiKhoanDTO(taiKhoan);
        
        return new ResponseEntity<>(taiKhoanDTO, HttpStatus.OK);
    }

    @PostMapping("/doimatkhau")
    public ResponseEntity<?> postMethodName(@RequestBody DoiMatKhauDTO doiMatKhauDTO) {
        if(doiMatKhauDTO.getTenDangNhap()==null||doiMatKhauDTO.getTenDangNhap().isEmpty()){
            return new ResponseEntity<>("Vui lòng nhập tên đăng nhập", HttpStatus.BAD_REQUEST);
        }
        if(doiMatKhauDTO.getMatKhauCu()==null||doiMatKhauDTO.getMatKhauCu().isEmpty()){
            return new ResponseEntity<>("Vui lòng nhập mật khẩu cũ", HttpStatus.BAD_REQUEST);
        }
        if(doiMatKhauDTO.getMatKhauMoi()==null||doiMatKhauDTO.getMatKhauMoi().isEmpty()){
            return new ResponseEntity<>("Vui lòng nhập mật khẩu mới", HttpStatus.BAD_REQUEST);
        }
        TaiKhoan taiKhoan = taiKhoanService.findById(doiMatKhauDTO.getTenDangNhap());
        if(taiKhoan.getMatKhau().equals(doiMatKhauDTO.getMatKhauCu())){
            return new ResponseEntity<>("Mật khẩu không đúng", HttpStatus.BAD_REQUEST);
        }
        try{
            taiKhoan.setMatKhau(doiMatKhauDTO.getMatKhauMoi());
            taiKhoanService.save(taiKhoan);
            return new ResponseEntity<>("Đổi mật khẩu thành công", HttpStatus.OK);
        }
        catch(Exception e){
            System.out.println("Lỗi đổi mật khẩu: "+e.getMessage());
            return new ResponseEntity<>("Đổi mật khẩu thất bại", HttpStatus.BAD_REQUEST);
        }
    }
    

    @GetMapping("/khachhang/{id}")
    public ResponseEntity<?> getKhachHang(@PathVariable("id") String id){
        KhachHang khachHang = khachHangService.findById(id);
        if(khachHang==null){
            return new ResponseEntity<>("Không tìm thấy", HttpStatus.NOT_FOUND);
        }
        KhachHangDTO khachHangDTO = khachHangService.mapToKhachHangDTO(khachHang);
        return new ResponseEntity<>(khachHangDTO,HttpStatus.OK);
    }

    @GetMapping("/banquanly/{id}")
    public ResponseEntity<?> getBanQuanLy(@PathVariable("id") String id){
        BanQuanLy banQuanLy = banQuanLyService.findById(id);
        if(banQuanLy==null){
            return new ResponseEntity<>("Không tìm thấy", HttpStatus.NOT_FOUND);
        }
        BanQuanLyDTO banQuanLyDTO = banQuanLyService.mapToBanQuanLyDTO(banQuanLy);
        return new ResponseEntity<>(banQuanLyDTO,HttpStatus.OK);
    }
}
