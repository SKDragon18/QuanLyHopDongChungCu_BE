package com.thuctap.quanlychungcu.controller;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thuctap.quanlychungcu.dto.CanHoBayBanDTO;
import com.thuctap.quanlychungcu.dto.DichVuBayBanDTO;
import com.thuctap.quanlychungcu.dto.DieuKhoanDTO;
import com.thuctap.quanlychungcu.dto.LoaiPhongDTO;
import com.thuctap.quanlychungcu.model.CTDKCanHo;
import com.thuctap.quanlychungcu.model.CanHo;
import com.thuctap.quanlychungcu.model.HinhAnh;
import com.thuctap.quanlychungcu.service.BangGiaService;
import com.thuctap.quanlychungcu.service.CanHoService;
import com.thuctap.quanlychungcu.service.DieuKhoanService;
import com.thuctap.quanlychungcu.service.HinhAnhService;

//Hiển thị trang home
@RestController
@RequestMapping("/hienthi")
public class HienThiDichVuController {
    @Autowired
    BangGiaService bangGiaService;

    
    @GetMapping("/canhochothue")
    public ResponseEntity<?> getAllCanHo(){
        List<CanHoBayBanDTO> list = bangGiaService.getCanHoHienThiList();
        return new ResponseEntity<>(list,HttpStatus.OK);
    }

    @GetMapping("/canhochothue/{id}")
    public ResponseEntity<?> getCanHo(@PathVariable("id") int id){
        
        List<CanHoBayBanDTO> list = bangGiaService.getCanHoHienThiList();
        for(CanHoBayBanDTO x: list){
            if(x.getIdCanHo()==id){
                return new ResponseEntity<>(x,HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Không tìm thấy",HttpStatus.NOT_FOUND);
    }

    @GetMapping("/dichvu")
    public ResponseEntity<?> getAllDichVu(){
        List<DichVuBayBanDTO> list = bangGiaService.getDichVuHienThiList();
        return new ResponseEntity<>(list,HttpStatus.OK);
    }

    @GetMapping("/dichvu/{id}")
    public ResponseEntity<?> getDichVu(@PathVariable("id") int id){
        
        List<DichVuBayBanDTO> list = bangGiaService.getDichVuHienThiList();
        for(DichVuBayBanDTO x: list){
            if(x.getIdDichVu()==id){
                return new ResponseEntity<>(x,HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Không tìm thấy",HttpStatus.NOT_FOUND);
    }
}
