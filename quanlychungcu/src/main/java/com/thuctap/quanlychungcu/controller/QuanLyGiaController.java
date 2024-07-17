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

import com.thuctap.quanlychungcu.dto.BangGiaDTO;
import com.thuctap.quanlychungcu.model.BangGia;
import com.thuctap.quanlychungcu.service.BangGiaService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/banggia")
public class QuanLyGiaController {
    @Autowired
    BangGiaService bangGiaService;

    @GetMapping
    public ResponseEntity<List<BangGiaDTO>> getAllBangGia(){
        List<BangGia> bangGiaList = bangGiaService.findAll();
        List<BangGiaDTO> bangGiaDTOList = bangGiaList.stream()
        .map(bangGia -> bangGiaService.mapToBangGiaDTO(bangGia)).toList();
        return new ResponseEntity<>(bangGiaDTOList,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBangGia(@PathVariable("id") long id){
        BangGia bangGia = bangGiaService.findById(id);
        if(bangGia==null){
            return new ResponseEntity<>("Không tìm thấy", HttpStatus.NOT_FOUND);
        }
        BangGiaDTO bangGiaDTO = bangGiaService.mapToBangGiaDTO(bangGia);
        return new ResponseEntity<>(bangGiaDTO,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> insertBangGia(@RequestBody BangGiaDTO bangGiaDTO) {
        BangGia bangGia = bangGiaService.mapToBangGia(bangGiaDTO);
        try{
            bangGia = bangGiaService.save(bangGia);
            if(bangGiaService.isExistsById(bangGia.getIdBangGia())){
                BangGiaDTO bangGiaDTO2 = bangGiaService.mapToBangGiaDTO(bangGia);
                return new ResponseEntity<>(bangGiaDTO2,HttpStatus.OK);
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
    public ResponseEntity<?> updateBangGia(@RequestBody BangGiaDTO bangGiaDTO) {
        if (!bangGiaService.isExistsById(bangGiaDTO.getIdBangGia())) {
            return new ResponseEntity<>("Bảng giá không tồn tại", HttpStatus.BAD_REQUEST);
        }
        try{
            BangGia bangGia = bangGiaService.mapToBangGia(bangGiaDTO);
            bangGia = bangGiaService.save(bangGia);
            BangGiaDTO bangGiaDTO2 = bangGiaService.mapToBangGiaDTO(bangGia);
            return new ResponseEntity<>(bangGiaDTO2, HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBangGia(@PathVariable long id) {
        if (!bangGiaService.isExistsById(id)) {
            return new ResponseEntity<>("Bảng giá không tồn tại", HttpStatus.BAD_REQUEST);
        }
        try{
            bangGiaService.deleteById(id);
            if (!bangGiaService.isExistsById(id)) {
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
