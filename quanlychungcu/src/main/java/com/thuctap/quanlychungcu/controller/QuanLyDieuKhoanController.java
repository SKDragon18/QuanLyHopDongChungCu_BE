package com.thuctap.quanlychungcu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<List<DieuKhoanDTO>> getAllDieuKhoan(){
        List<DieuKhoan> dieuKhoanList = dieuKhoanService.findAll();
        List<DieuKhoanDTO> dieuKhoanDTOList = dieuKhoanList.stream()
        .map(dieuKhoan -> dieuKhoanService.mapToDieuKhoanDTO(dieuKhoan)).toList();
        return new ResponseEntity<>(dieuKhoanDTOList,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDieuKhoan(@PathVariable("id") String id){
        DieuKhoan dieuKhoan = dieuKhoanService.findById(id);
        if(dieuKhoan==null){
            return new ResponseEntity<>("Không tìm thấy", HttpStatus.NOT_FOUND);
        }
        DieuKhoanDTO dieuKhoanDTO = dieuKhoanService.mapToDieuKhoanDTO(dieuKhoan);
        return new ResponseEntity<>(dieuKhoanDTO,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> insertDieuKhoan(@RequestBody DieuKhoanDTO dieuKhoanDTO) {
        DieuKhoan dieuKhoan = dieuKhoanService.mapToDieuKhoan(dieuKhoanDTO);
        try{
            dieuKhoan = dieuKhoanService.save(dieuKhoan);
            if(dieuKhoanService.isExistsById(dieuKhoan.getMa())){
                DieuKhoanDTO dieuKhoanDTO2 = dieuKhoanService.mapToDieuKhoanDTO(dieuKhoan);
                return new ResponseEntity<>(dieuKhoanDTO2,HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("data",HttpStatus.BAD_REQUEST);
            }
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateDieuKhoan(@RequestBody DieuKhoanDTO dieuKhoanDTO) {
        if (!dieuKhoanService.isExistsById(dieuKhoanDTO.getMa())) {
            return new ResponseEntity<>("Điều khoản không tồn tại", HttpStatus.BAD_REQUEST);
        }
        try{
            DieuKhoan dieuKhoan = dieuKhoanService.mapToDieuKhoan(dieuKhoanDTO);
            dieuKhoan = dieuKhoanService.save(dieuKhoan);
            DieuKhoanDTO dieuKhoanDTO2 = dieuKhoanService.mapToDieuKhoanDTO(dieuKhoan);
            return new ResponseEntity<>(dieuKhoanDTO2, HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDieuKhoan(@PathVariable String id) {
        if (!dieuKhoanService.isExistsById(id)) {
            return new ResponseEntity<>("Điều khoản không tồn tại", HttpStatus.BAD_REQUEST);
        }
        try{
            dieuKhoanService.deleteById(id);
            if (!dieuKhoanService.isExistsById(id)) {
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
