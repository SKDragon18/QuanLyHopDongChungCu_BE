package com.thuctap.quanlychungcu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<List<HoaDonDTO>> getAllHoaDon(){
        List<HoaDon> hoaDonList = hoaDonService.findAll();
        List<HoaDonDTO> hoaDonDTOList = hoaDonList.stream()
        .map(hoaDon -> hoaDonService.mapToHoaDonDTO(hoaDon)).toList();
        return new ResponseEntity<>(hoaDonDTOList,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getHoaDon(@PathVariable("id") long id){
        HoaDon hoaDon = hoaDonService.findById(id);
        if(hoaDon==null){
            return new ResponseEntity<>("Không tìm thấy", HttpStatus.NOT_FOUND);
        }
        HoaDonDTO hoaDonDTO = hoaDonService.mapToHoaDonDTO(hoaDon);
        return new ResponseEntity<>(hoaDonDTO,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> insertHoaDon(@RequestBody HoaDonDTO hoaDonDTO) {
        HoaDon hoaDon = hoaDonService.mapToHoaDon(hoaDonDTO);
        try{
            hoaDon = hoaDonService.save(hoaDon);
            if(hoaDonService.isExistsById(hoaDon.getSoHoaDon())){
                HoaDonDTO hoaDonDTO2 = hoaDonService.mapToHoaDonDTO(hoaDon);
                return new ResponseEntity<>(hoaDonDTO2,HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("data",HttpStatus.BAD_REQUEST);
            }
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


}
