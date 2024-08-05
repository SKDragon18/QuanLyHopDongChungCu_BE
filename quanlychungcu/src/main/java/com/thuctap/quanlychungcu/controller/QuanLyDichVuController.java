package com.thuctap.quanlychungcu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thuctap.quanlychungcu.dto.ApiResponse;
import com.thuctap.quanlychungcu.dto.DichVuDTO;
import com.thuctap.quanlychungcu.model.DichVu;
import com.thuctap.quanlychungcu.service.DichVuService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/dichvu")
public class QuanLyDichVuController {
    @Autowired
    DichVuService dichVuService;

    //Dich vu thuong
    @GetMapping
    public ApiResponse<List<DichVuDTO>> getAllDichVu(){
        List<DichVu> dichVuList = dichVuService.findAll();
        List<DichVuDTO> dichVuDTOList = dichVuList.stream()
        .map(dichVu -> dichVuService.mapToDichVuDTO(dichVu)).toList();
        return ApiResponse.<List<DichVuDTO>>builder().code(200)
        .result(dichVuDTOList).build();
    }

    @GetMapping("/{id}")
    public ApiResponse<DichVuDTO> getDichVu(@PathVariable("id") int id){
        DichVu dichVu = dichVuService.findById(id);
        if(dichVu==null){
            return ApiResponse.<DichVuDTO>builder().code(404)
            .message("Không tìm thấy").build();
        }
        DichVuDTO dichVuDTO = dichVuService.mapToDichVuDTO(dichVu);
        return ApiResponse.<DichVuDTO>builder().code(200)
            .result(dichVuDTO).build();
    }

    @PostMapping
    public ApiResponse<DichVuDTO> insertDichVu(@RequestBody DichVuDTO dichVuDTO) {
        DichVu dichVu = dichVuService.mapToDichVu(dichVuDTO);
        try{
            dichVu.setTrangThai(true);
            dichVu = dichVuService.save(dichVu);
            if(dichVuService.isExistsById(dichVu.getIdDichVu())){
                DichVuDTO dichVuDTO2 = dichVuService.mapToDichVuDTO(dichVu);
                return ApiResponse.<DichVuDTO>builder().code(200)
                .result(dichVuDTO2).build();
            }
            else{
                return ApiResponse.<DichVuDTO>builder().code(400)
                .message("Thêm thất bại").build();
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return ApiResponse.<DichVuDTO>builder().code(400)
                .message(e.getMessage()).build();
        }
    }

    @PutMapping
    public ApiResponse<DichVuDTO> updateDichVu(@RequestBody DichVuDTO dichVuDTO) {
        if (!dichVuService.isExistsById(dichVuDTO.getIdDichVu())) {
            return ApiResponse.<DichVuDTO>builder().code(400)
                .message("Dịch vụ không tồn tại").build();
        }
        try{
            DichVu dichVu = dichVuService.mapToDichVu(dichVuDTO);
            dichVu=dichVuService.save(dichVu);
            if(!dichVuService.updateDieuKhoan(dichVuDTO, dichVu)){
                return ApiResponse.<DichVuDTO>builder().code(400)
                .message("Lỗi update điều khoản").build();
            }
            dichVu=dichVuService.findById(dichVuDTO.getIdDichVu());
            DichVuDTO dichVuDTO2 = dichVuService.mapToDichVuDTO(dichVu);
            return ApiResponse.<DichVuDTO>builder().code(200)
                .result(dichVuDTO2).build();
        }
        catch(Exception e){
            return ApiResponse.<DichVuDTO>builder().code(400)
                .message(e.getMessage()).build();
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<DichVuDTO> changePublicDichVu(@PathVariable int id) {
        if (!dichVuService.isExistsById(id)) {
            return ApiResponse.<DichVuDTO>builder().code(400)
                .message("Dịch vụ không tồn tại").build();
        }
        try{
            DichVu dichVu = dichVuService.findById(id);
            dichVu.setTrangThai(!dichVu.getTrangThai());
            dichVu = dichVuService.save(dichVu);
            DichVuDTO dichVuDTO2 = dichVuService.mapToDichVuDTO(dichVu);
            return ApiResponse.<DichVuDTO>builder().code(200)
                .result(dichVuDTO2).build();
        }
        catch(Exception e){
            return ApiResponse.<DichVuDTO>builder().code(400)
                .message(e.getMessage()).build();
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteDichVu(@PathVariable int id) {
        if (!dichVuService.isExistsById(id)) {
            return ApiResponse.<String>builder().code(400)
                .message("Dịch vụ không tồn tại").build();
        }
        try{
            dichVuService.deleteById(id);
            if (!dichVuService.isExistsById(id)) {
                return ApiResponse.<String>builder().code(200)
                .result("Xóa thành công").build();
            } else {
                return ApiResponse.<String>builder().code(400)
                .message("Xóa thất bại").build();
            }
        }
        catch(Exception e){
            return ApiResponse.<String>builder().code(400)
                .message(e.getMessage()).build();
        }
    }
}
