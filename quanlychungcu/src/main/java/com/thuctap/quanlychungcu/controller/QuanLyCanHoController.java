package com.thuctap.quanlychungcu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.thuctap.quanlychungcu.dto.CanHoDTO;
import com.thuctap.quanlychungcu.dto.LoaiPhongDTO;
import com.thuctap.quanlychungcu.model.CanHo;
import com.thuctap.quanlychungcu.model.LoaiPhong;
import com.thuctap.quanlychungcu.service.CanHoService;
import com.thuctap.quanlychungcu.service.HinhAnhService;
import com.thuctap.quanlychungcu.service.LoaiPhongService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/canho")
public class QuanLyCanHoController {
    @Autowired
    CanHoService canHoService;

    @Autowired
    LoaiPhongService loaiPhongService;

    @Autowired
    HinhAnhService hinhAnhService;

    @GetMapping
    public ResponseEntity<List<CanHoDTO>> getAllCanHo(){
        List<CanHo> canHoList = canHoService.findAll();
        List<CanHoDTO> canHoDTOList = canHoList.stream()
        .map(canHo -> canHoService.mapToCanHoDTO(canHo,true)).toList();
        return new ResponseEntity<>(canHoDTOList,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCanHo(@PathVariable("id") int id){
        CanHo canHo = canHoService.findById(id);
        if(canHo==null){
            return new ResponseEntity<>("Không tìm thấy", HttpStatus.NOT_FOUND);
        }
        CanHoDTO canHoDTO = canHoService.mapToCanHoDTO(canHo,true);
        if(canHoDTO==null){
            return new ResponseEntity<>("Không tìm thấy", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(canHoDTO,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> insertCanHo(@RequestBody CanHoDTO canHoDTO) {
        CanHo canHo = canHoService.mapToCanHo(canHoDTO);
        try{
            canHo = canHoService.save(canHo);
            if(canHoService.isExistsById(canHo.getIdCanHo())){
                CanHoDTO canHoDTO2 = canHoService.mapToCanHoDTO(canHo,false);
                return new ResponseEntity<>(canHoDTO2,HttpStatus.OK);
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

    @PostMapping("/hinhanh")
    public ResponseEntity<?> insertHinhAnhCanHo(@RequestParam("images")MultipartFile[] images, 
    @RequestParam("idCanHo") int idCanHo) {
        try{
            CanHo canHo = canHoService.findById(idCanHo);
            for(MultipartFile image: images){
                hinhAnhService.downloadHinhAnh(image, null, canHo);
            }
            return new ResponseEntity<>("Thêm thành công",HttpStatus.OK);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/doihinhanh")
    public ResponseEntity<?> changeHinhAnhCanHo(@RequestParam("images")MultipartFile[] images, 
    @RequestParam("idCanHo") int idCanHo) {
        try{
            CanHo canHo = canHoService.findById(idCanHo);
            String result = hinhAnhService.deleteAllHinhAnhCanHo(canHo);
            if(!result.contains("thành công")){
                System.out.println(result);
            }
            for(MultipartFile image: images){
                hinhAnhService.downloadHinhAnh(image, null, canHo);
            }
            return new ResponseEntity<>("Đổi thành công",HttpStatus.OK);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateCanHo(@RequestBody CanHoDTO canHoDTO) {
        if (!canHoService.isExistsById(canHoDTO.getIdCanHo())) {
            return new ResponseEntity<>("Căn hộ không tồn tại", HttpStatus.BAD_REQUEST);
        }
        try{
            CanHo canHo = canHoService.mapToCanHo(canHoDTO);
            canHo = canHoService.save(canHo);
            CanHoDTO canHoDTO2 = canHoService.mapToCanHoDTO(canHo,false);
            return new ResponseEntity<>(canHoDTO2, HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCanHo(@PathVariable int id) {
        if (!canHoService.isExistsById(id)) {
            return new ResponseEntity<>("Căn hộ không tồn tại", HttpStatus.BAD_REQUEST);
        }
        try{
            CanHo canHo = canHoService.findById(id);
            String result = hinhAnhService.deleteAllHinhAnhCanHo(canHo);
            if(!result.contains("thành công")&&!result.contains("Không có")){
                System.out.println(result);
                return new ResponseEntity<>("Xóa hình ảnh thất bại", HttpStatus.BAD_REQUEST);
            }
            canHoService.deleteById(id);
            if (!canHoService.isExistsById(id)) {
                return new ResponseEntity<>("Xóa thành công", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Xóa thất bại", HttpStatus.BAD_REQUEST);
            }
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    //Loại phòng

    @GetMapping("/loaiphong")
    public ResponseEntity<List<LoaiPhongDTO>> getAllLoaiPhong(){
        List<LoaiPhong> loaiPhongList = loaiPhongService.findAll();
        List<LoaiPhongDTO> loaiPhongDTOList = loaiPhongList.stream()
        .map(loaiPhong -> loaiPhongService.mapToLoaiPhongDTO(loaiPhong)).toList();
        return new ResponseEntity<>(loaiPhongDTOList,HttpStatus.OK);
    }

    @GetMapping("/loaiphong/{id}")
    public ResponseEntity<?> getLoaiPhong(@PathVariable("id") int id){
        LoaiPhong loaiPhong = loaiPhongService.findById(id);
        if(loaiPhong==null){
            return new ResponseEntity<>("Không tìm thấy", HttpStatus.NOT_FOUND);
        }
        LoaiPhongDTO loaiPhongDTO = loaiPhongService.mapToLoaiPhongDTO(loaiPhong);
        return new ResponseEntity<>(loaiPhongDTO,HttpStatus.OK);
    }

    @PostMapping("/loaiphong")
    public ResponseEntity<?> insertLoaiPhong(@RequestBody LoaiPhongDTO loaiPhongDTO) {
        LoaiPhong loaiPhong = loaiPhongService.mapToLoaiPhong(loaiPhongDTO);
        try{
            loaiPhong = loaiPhongService.save(loaiPhong);
            if(loaiPhongService.isExistsById(loaiPhong.getIdLoaiPhong())){
                LoaiPhongDTO loaiPhongDTO2 = loaiPhongService.mapToLoaiPhongDTO(loaiPhong);
                return new ResponseEntity<>(loaiPhongDTO2,HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("data",HttpStatus.BAD_REQUEST);
            }
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/loaiphong")
    public ResponseEntity<?> updateLoaiPhong(@RequestBody LoaiPhongDTO loaiPhongDTO) {
        if (!loaiPhongService.isExistsById(loaiPhongDTO.getIdLoaiPhong())) {
            return new ResponseEntity<>("Loại phòng không tồn tại", HttpStatus.BAD_REQUEST);
        }
        try{
            LoaiPhong loaiPhong = loaiPhongService.mapToLoaiPhong(loaiPhongDTO);
            loaiPhong = loaiPhongService.save(loaiPhong);
            LoaiPhongDTO loaiPhongDTO2 = loaiPhongService.mapToLoaiPhongDTO(loaiPhong);
            return new ResponseEntity<>(loaiPhongDTO2, HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/loaiphong/{id}")
    public ResponseEntity<?> deleteLoaiPhong(@PathVariable int id) {
        if (!loaiPhongService.isExistsById(id)) {
            return new ResponseEntity<>("Loại phòng không tồn tại", HttpStatus.BAD_REQUEST);
        }
        try{
            loaiPhongService.deleteById(id);
            if (!loaiPhongService.isExistsById(id)) {
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
