package com.thuctap.quanlychungcu.controller;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thuctap.quanlychungcu.dto.ApiResponse;
import com.thuctap.quanlychungcu.dto.CanHoBayBanDTO;
import com.thuctap.quanlychungcu.dto.DichVuBayBanDTO;
import com.thuctap.quanlychungcu.service.BangGiaService;

//Hiển thị trang home
@RestController
@RequestMapping("/hienthi")
public class HienThiDichVuController {
    @Autowired
    BangGiaService bangGiaService;

    
    @GetMapping("/canhochothue")
    public ApiResponse<List<CanHoBayBanDTO>> getAllCanHo(){
        List<CanHoBayBanDTO> list = bangGiaService.getCanHoHienThiList();
        return ApiResponse.<List<CanHoBayBanDTO>>builder().code(200).result(list).build();
    }

    @GetMapping("/canhochothue/{id}")
    public ApiResponse<CanHoBayBanDTO> getCanHo(@PathVariable("id") int id){
        
        List<CanHoBayBanDTO> list = bangGiaService.getCanHoHienThiList();
        for(CanHoBayBanDTO x: list){
            if(x.getIdCanHo()==id){
                return ApiResponse.<CanHoBayBanDTO>builder().result(x).code(200).build();
            }
        }
        return ApiResponse.<CanHoBayBanDTO>builder().message("Không tìm thấy").code(404).build();
    }

    @GetMapping("/dichvu")
    public ApiResponse<List<DichVuBayBanDTO>> getAllDichVu(){
        List<DichVuBayBanDTO> list = bangGiaService.getDichVuHienThiList();
        return ApiResponse.<List<DichVuBayBanDTO>>builder().code(200).result(list).build();
    }

    @GetMapping("/dichvu/{id}")
    public ApiResponse<DichVuBayBanDTO> getDichVu(@PathVariable("id") int id){
        
        List<DichVuBayBanDTO> list = bangGiaService.getDichVuHienThiList();
        for(DichVuBayBanDTO x: list){
            if(x.getIdDichVu()==id){
                return ApiResponse.<DichVuBayBanDTO>builder().code(200).result(x).build();
            }
        }
        return ApiResponse.<DichVuBayBanDTO>builder().code(404).message("Không tìm thấy").build();
    }
}
