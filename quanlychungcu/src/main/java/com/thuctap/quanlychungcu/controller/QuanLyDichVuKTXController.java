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

import com.thuctap.quanlychungcu.dto.CPKTXDTO;
import com.thuctap.quanlychungcu.model.CPKTX;
import com.thuctap.quanlychungcu.service.CPKTXService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/cpktx")
public class QuanLyDichVuKTXController {
    @Autowired
    CPKTXService chiPhiKTXService;

    //Dich vu thuong
    @GetMapping
    public ResponseEntity<List<CPKTXDTO>> getAllCPKTX(){
        List<CPKTX> chiPhiKTXList = chiPhiKTXService.findAll();
        List<CPKTXDTO> chiPhiKTXDTOList = chiPhiKTXList.stream()
        .map(chiPhiKTX -> chiPhiKTXService.mapToCPKTXDTO(chiPhiKTX)).toList();
        return new ResponseEntity<>(chiPhiKTXDTOList,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCPKTX(@PathVariable("id") int id){
        CPKTX chiPhiKTX = chiPhiKTXService.findById(id);
        if(chiPhiKTX==null){
            return new ResponseEntity<>("Không tìm thấy", HttpStatus.NOT_FOUND);
        }
        CPKTXDTO chiPhiKTXDTO = chiPhiKTXService.mapToCPKTXDTO(chiPhiKTX);
        return new ResponseEntity<>(chiPhiKTXDTO,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> insertCPKTX(@RequestBody CPKTXDTO chiPhiKTXDTO) {
        CPKTX chiPhiKTX = chiPhiKTXService.mapToCPKTX(chiPhiKTXDTO);
        try{
            chiPhiKTX = chiPhiKTXService.save(chiPhiKTX);
            if(chiPhiKTXService.isExistsById(chiPhiKTX.getIdCPKTX())){
                CPKTXDTO chiPhiKTXDTO2 = chiPhiKTXService.mapToCPKTXDTO(chiPhiKTX);
                return new ResponseEntity<>(chiPhiKTXDTO2,HttpStatus.OK);
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
    public ResponseEntity<?> updateCPKTX(@RequestBody CPKTXDTO chiPhiKTXDTO) {
        if (!chiPhiKTXService.isExistsById(chiPhiKTXDTO.getIdCPKTX())) {
            return new ResponseEntity<>("Dịch vụ không tồn tại", HttpStatus.BAD_REQUEST);
        }
        try{
            CPKTX chiPhiKTX = chiPhiKTXService.mapToCPKTX(chiPhiKTXDTO);
            chiPhiKTX = chiPhiKTXService.save(chiPhiKTX);
            CPKTXDTO chiPhiKTXDTO2 = chiPhiKTXService.mapToCPKTXDTO(chiPhiKTX);
            return new ResponseEntity<>(chiPhiKTXDTO2, HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCPKTX(@PathVariable int id) {
        if (!chiPhiKTXService.isExistsById(id)) {
            return new ResponseEntity<>("Dịch vụ không tồn tại", HttpStatus.BAD_REQUEST);
        }
        try{
            chiPhiKTXService.deleteById(id);
            if (!chiPhiKTXService.isExistsById(id)) {
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
