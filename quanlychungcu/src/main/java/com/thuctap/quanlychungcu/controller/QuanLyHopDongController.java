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

import com.thuctap.quanlychungcu.dto.HopDongDTO;
import com.thuctap.quanlychungcu.model.HopDong;
import com.thuctap.quanlychungcu.service.HopDongService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/hopdong")
public class QuanLyHopDongController {
    @Autowired
    HopDongService hopDongService;

    @GetMapping
    public ResponseEntity<List<HopDongDTO>> getAllHopDong(){
        List<HopDong> hopDongList = hopDongService.findAll();
        List<HopDongDTO> hopDongDTOList = hopDongList.stream()
        .map(hopDong -> hopDongService.mapToHopDongDTO(hopDong)).toList();
        return new ResponseEntity<>(hopDongDTOList,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getHopDong(@PathVariable("id") long id){
        HopDong hopDong = hopDongService.findById(id);
        if(hopDong==null){
            return new ResponseEntity<>("Không tìm thấy", HttpStatus.NOT_FOUND);
        }
        HopDongDTO hopDongDTO = hopDongService.mapToHopDongDTO(hopDong);
        return new ResponseEntity<>(hopDongDTO,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> insertHopDong(@RequestBody HopDongDTO hopDongDTO) {
        HopDong hopDong = hopDongService.mapToHopDong(hopDongDTO);
        try{
            hopDong = hopDongService.save(hopDong);
            if(hopDongService.isExistsById(hopDong.getIdHopDong())){
                HopDongDTO hopDongDTO2 = hopDongService.mapToHopDongDTO(hopDong);
                return new ResponseEntity<>(hopDongDTO2,HttpStatus.OK);
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
    public ResponseEntity<?> updateHopDong(@RequestBody HopDongDTO hopDongDTO) {
        if (!hopDongService.isExistsById(hopDongDTO.getIdHopDong())) {
            return new ResponseEntity<>("Hợp đồng không tồn tại", HttpStatus.BAD_REQUEST);
        }
        try{
            HopDong hopDong = hopDongService.mapToHopDong(hopDongDTO);
            hopDong = hopDongService.save(hopDong);
            HopDongDTO hopDongDTO2 = hopDongService.mapToHopDongDTO(hopDong);
            return new ResponseEntity<>(hopDongDTO2, HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHopDong(@PathVariable long id) {
        if (!hopDongService.isExistsById(id)) {
            return new ResponseEntity<>("Hợp đồng không tồn tại", HttpStatus.BAD_REQUEST);
        }
        try{
            hopDongService.deleteById(id);
            if (!hopDongService.isExistsById(id)) {
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
