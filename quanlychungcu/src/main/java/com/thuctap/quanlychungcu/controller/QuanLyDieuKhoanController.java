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
import com.thuctap.quanlychungcu.dto.DieuKhoanDTO;
import com.thuctap.quanlychungcu.model.DieuKhoan;
import com.thuctap.quanlychungcu.service.DieuKhoanService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/dieukhoan")
public class QuanLyDieuKhoanController {
    @Autowired
    DieuKhoanService dieuKhoanService;

    @GetMapping
    public ApiResponse<List<DieuKhoanDTO>> getAllDieuKhoan(){
        List<DieuKhoan> dieuKhoanList = dieuKhoanService.findAll();
        List<DieuKhoanDTO> dieuKhoanDTOList = dieuKhoanList.stream()
        .map(dieuKhoan -> dieuKhoanService.mapToDieuKhoanDTO(dieuKhoan)).toList();
        return ApiResponse.<List<DieuKhoanDTO>> builder().code(200)
        .result(dieuKhoanDTOList).build();
    }

    @GetMapping("/{id}")
    public ApiResponse<DieuKhoanDTO> getDieuKhoan(@PathVariable("id") String id){
        DieuKhoan dieuKhoan = dieuKhoanService.findById(id);
        if(dieuKhoan==null){
            return ApiResponse.<DieuKhoanDTO> builder().code(404)
            .message("Không tìm thấy").build();
        }
        DieuKhoanDTO dieuKhoanDTO = dieuKhoanService.mapToDieuKhoanDTO(dieuKhoan);
        return ApiResponse.<DieuKhoanDTO> builder().code(200)
        .result(dieuKhoanDTO).build();
    }

    @PostMapping
    public ApiResponse<DieuKhoanDTO> insertDieuKhoan(@RequestBody DieuKhoanDTO dieuKhoanDTO) {
        DieuKhoan dieuKhoan = dieuKhoanService.mapToDieuKhoan(dieuKhoanDTO);
        try{
            dieuKhoan = dieuKhoanService.save(dieuKhoan);
            if(dieuKhoanService.isExistsById(dieuKhoan.getMa())){
                DieuKhoanDTO dieuKhoanDTO2 = dieuKhoanService.mapToDieuKhoanDTO(dieuKhoan);
                return ApiResponse.<DieuKhoanDTO> builder().code(200)
                .result(dieuKhoanDTO2).build();
            }
            else{
                return ApiResponse.<DieuKhoanDTO> builder().code(400)
                .message("Lưu thất bại").build();
            }
        }
        catch(Exception e){
            return ApiResponse.<DieuKhoanDTO> builder().code(400)
                .message(e.getMessage()).build();
        }
    }

    @PutMapping
    public ApiResponse<DieuKhoanDTO> updateDieuKhoan(@RequestBody DieuKhoanDTO dieuKhoanDTO) {
        if (!dieuKhoanService.isExistsById(dieuKhoanDTO.getMa())) {
            return ApiResponse.<DieuKhoanDTO> builder().code(400)
                .message("Điều khoản không tồn tại").build();
        }
        try{
            DieuKhoan dieuKhoan = dieuKhoanService.mapToDieuKhoan(dieuKhoanDTO);
            dieuKhoan = dieuKhoanService.save(dieuKhoan);
            DieuKhoanDTO dieuKhoanDTO2 = dieuKhoanService.mapToDieuKhoanDTO(dieuKhoan);
            return ApiResponse.<DieuKhoanDTO> builder().code(200)
                .result(dieuKhoanDTO2).build();
        }
        catch(Exception e){
            return ApiResponse.<DieuKhoanDTO> builder().code(400)
                .message(e.getMessage()).build();
        }
    }


    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteDieuKhoan(@PathVariable String id) {
        if (!dieuKhoanService.isExistsById(id)) {
            return ApiResponse.<String> builder().code(400)
                .message("Điều khoản không tồn tại").build();
        }
        try{
            dieuKhoanService.deleteById(id);
            if (!dieuKhoanService.isExistsById(id)) {
                return ApiResponse.<String> builder().code(200)
                .result("Xóa thành công").build();
            } else {
                return ApiResponse.<String> builder().code(400)
                .message("Xóa thất bại").build();
            }
        }
        catch(Exception e){
            return ApiResponse.<String> builder().code(400)
                .message(e.getMessage()).build();
        }
    }

}
