package com.thuctap.quanlychungcu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thuctap.quanlychungcu.dto.ApiResponse;
import com.thuctap.quanlychungcu.dto.QuyenDTO;
import com.thuctap.quanlychungcu.dto.TaiKhoanDTO;
import com.thuctap.quanlychungcu.model.Quyen;
import com.thuctap.quanlychungcu.model.TaiKhoan;
import com.thuctap.quanlychungcu.service.QuyenService;
import com.thuctap.quanlychungcu.service.TaiKhoanService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/taikhoan")

public class QuanLyTaiKhoanController {
    @Autowired
    TaiKhoanService taiKhoanService;

    @Autowired
    QuyenService quyenService;

    @GetMapping
    public ApiResponse<List<TaiKhoanDTO>> getAllTaiKhoan(){

        List<TaiKhoan> taiKhoanList = taiKhoanService.findAll();
        List<TaiKhoanDTO> taiKhoanDTOList = taiKhoanList.stream()
        .map(taiKhoan -> taiKhoanService.mapToTaiKhoanDTO(taiKhoan)).toList();
        return ApiResponse.<List<TaiKhoanDTO>>builder()
        .result(taiKhoanDTOList).code(200).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTaiKhoan(@PathVariable("id") String id){
        TaiKhoan taiKhoan = taiKhoanService.findById(id);
        if(taiKhoan==null){
            return new ResponseEntity<>("Không tìm thấy", HttpStatus.NOT_FOUND);
        }
        TaiKhoanDTO taiKhoanDTO = taiKhoanService.mapToTaiKhoanDTO(taiKhoan);
        return new ResponseEntity<>(taiKhoanDTO,HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateTaiKhoan(@RequestBody TaiKhoanDTO taiKhoanDTO) {
        if (!taiKhoanService.isExistsById(taiKhoanDTO.getTenDangNhap())) {
            return new ResponseEntity<>("Tài khoản không tồn tại", HttpStatus.BAD_REQUEST);
        }
        try{
            TaiKhoan taiKhoan = taiKhoanService.mapToTaiKhoan(taiKhoanDTO);
            taiKhoan = taiKhoanService.save(taiKhoan);
            TaiKhoanDTO taiKhoanDTO2 = taiKhoanService.mapToTaiKhoanDTO(taiKhoan);
            return new ResponseEntity<>(taiKhoanDTO2, HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    

    @GetMapping("/quyen")
    public ResponseEntity<List<QuyenDTO>> getAllQuyen(){
        List<Quyen> quyenList = quyenService.findAll();
        List<QuyenDTO> quyenDTOList = quyenList.stream()
        .map(quyen -> quyenService.mapToQuyenDTO(quyen)).toList();
        return new ResponseEntity<>(quyenDTOList,HttpStatus.OK);
    }

    @GetMapping("/quyen/{id}")
    public ResponseEntity<?> getQuyen(@PathVariable int id) {
        if (!quyenService.isExistsById(id)) {
            return new ResponseEntity<>("Quyền không tồn tại", HttpStatus.BAD_REQUEST);
        }
        Quyen quyen = quyenService.findById(id);
        QuyenDTO quyenDTO = quyenService.mapToQuyenDTO(quyen);
        return new ResponseEntity<>(quyenDTO, HttpStatus.OK);
    }

    @PostMapping("/quyen")
    public ResponseEntity<?> insertQuyen(@RequestBody QuyenDTO quyenDTO) {
        Quyen quyen = quyenService.mapToQuyen(quyenDTO);
        try{
            quyen = quyenService.save(quyen);
            if(quyenService.isExistsById(quyen.getIdQuyen())){
                QuyenDTO quyenDTO2 = quyenService.mapToQuyenDTO(quyen);
                return new ResponseEntity<>(quyenDTO2,HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("data",HttpStatus.BAD_REQUEST);
            }
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        
    }
     
    @PutMapping("/quyen")
    public ResponseEntity<?> updateQuyen(@RequestBody QuyenDTO quyenDTO) {
        if (!quyenService.isExistsById(quyenDTO.getIdQuyen())) {
            return new ResponseEntity<>("Quyền không tồn tại", HttpStatus.BAD_REQUEST);
        }
        try{
            Quyen quyen = quyenService.mapToQuyen(quyenDTO);
            quyen = quyenService.save(quyen);
            QuyenDTO quyenDTO2 = quyenService.mapToQuyenDTO(quyen);
            return new ResponseEntity<>(quyenDTO2, HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/quyen/{id}")
    public ResponseEntity<?> deleteQuyen(@PathVariable int id) {
        if (!quyenService.isExistsById(id)) {
            return new ResponseEntity<>("Quyền không tồn tại", HttpStatus.BAD_REQUEST);
        }
        try{
            quyenService.deleteById(id);
            if (!quyenService.isExistsById(id)) {
                return new ResponseEntity<>("Xóa thành công", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Xóa thất bại", HttpStatus.BAD_REQUEST);
            }
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

}
