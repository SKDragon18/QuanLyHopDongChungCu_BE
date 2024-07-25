package com.thuctap.quanlychungcu.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thuctap.quanlychungcu.dto.ApiResponse;
import com.thuctap.quanlychungcu.dto.HoaDonDTO;
import com.thuctap.quanlychungcu.model.HoaDon;
import com.thuctap.quanlychungcu.service.HoaDonService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/hoadon")
public class QuanLyHoaDonController {
    @Autowired
    HoaDonService hoaDonService;

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


}
