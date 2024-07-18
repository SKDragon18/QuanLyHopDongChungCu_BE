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
    public ResponseEntity<List<DichVuDTO>> getAllDichVu(){
        List<DichVu> dichVuList = dichVuService.findAll();
        List<DichVuDTO> dichVuDTOList = dichVuList.stream()
        .map(dichVu -> dichVuService.mapToDichVuDTO(dichVu)).toList();
        return new ResponseEntity<>(dichVuDTOList,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDichVu(@PathVariable("id") int id){
        DichVu dichVu = dichVuService.findById(id);
        if(dichVu==null){
            return new ResponseEntity<>("Không tìm thấy", HttpStatus.NOT_FOUND);
        }
        DichVuDTO dichVuDTO = dichVuService.mapToDichVuDTO(dichVu);
        return new ResponseEntity<>(dichVuDTO,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> insertDichVu(@RequestBody DichVuDTO dichVuDTO) {
        DichVu dichVu = dichVuService.mapToDichVu(dichVuDTO);
        try{
            dichVu.setTrangThai(false);
            dichVu = dichVuService.save(dichVu);
            if(dichVuService.isExistsById(dichVu.getIdDichVu())){
                DichVuDTO dichVuDTO2 = dichVuService.mapToDichVuDTO(dichVu);
                return new ResponseEntity<>(dichVuDTO2,HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("Thêm thất bại",HttpStatus.BAD_REQUEST);
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateDichVu(@RequestBody DichVuDTO dichVuDTO) {
        if (!dichVuService.isExistsById(dichVuDTO.getIdDichVu())) {
            return new ResponseEntity<>("Dịch vụ không tồn tại", HttpStatus.BAD_REQUEST);
        }
        try{
            DichVu dichVu = dichVuService.mapToDichVu(dichVuDTO);
            dichVu=dichVuService.save(dichVu);
            if(!dichVuService.updateDieuKhoan(dichVuDTO, dichVu)){
                return new ResponseEntity<>("Lỗi update điều khoản",HttpStatus.BAD_REQUEST);
            }
            dichVu=dichVuService.findById(dichVuDTO.getIdDichVu());
            DichVuDTO dichVuDTO2 = dichVuService.mapToDichVuDTO(dichVu);
            return new ResponseEntity<>(dichVuDTO2, HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> changePublicDichVu(@PathVariable int id) {
        if (!dichVuService.isExistsById(id)) {
            return new ResponseEntity<>("Dịch vụ không tồn tại", HttpStatus.BAD_REQUEST);
        }
        try{
            DichVu dichVu = dichVuService.findById(id);
            dichVu.setTrangThai(!dichVu.getTrangThai());
            dichVu = dichVuService.save(dichVu);
            DichVuDTO dichVuDTO2 = dichVuService.mapToDichVuDTO(dichVu);
            return new ResponseEntity<>(dichVuDTO2, HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDichVu(@PathVariable int id) {
        if (!dichVuService.isExistsById(id)) {
            return new ResponseEntity<>("Dịch vụ không tồn tại", HttpStatus.BAD_REQUEST);
        }
        try{
            dichVuService.deleteById(id);
            if (!dichVuService.isExistsById(id)) {
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
