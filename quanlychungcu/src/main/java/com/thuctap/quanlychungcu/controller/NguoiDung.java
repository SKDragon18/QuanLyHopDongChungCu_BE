package com.thuctap.quanlychungcu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thuctap.quanlychungcu.dto.CanHoDTO;
import com.thuctap.quanlychungcu.dto.KhachHangDTO;
import com.thuctap.quanlychungcu.model.CanHo;
import com.thuctap.quanlychungcu.model.KhachHang;
import com.thuctap.quanlychungcu.service.KhachHangService;

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
    KhachHangService khachHangService;

    @GetMapping("/khachhang/{id}")
    public ResponseEntity<?> getKhachHang(@PathVariable("id") String id){
        KhachHang khachHang = khachHangService.findById(id);
        if(khachHang==null){
            return new ResponseEntity<>("Không tìm thấy", HttpStatus.NOT_FOUND);
        }
        KhachHangDTO khachHangDTO = khachHangService.mapToKhachHangDTO(khachHang);
        return new ResponseEntity<>(khachHangDTO,HttpStatus.OK);
    }
}
