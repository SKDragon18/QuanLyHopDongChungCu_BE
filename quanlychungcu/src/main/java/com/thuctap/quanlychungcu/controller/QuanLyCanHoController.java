package com.thuctap.quanlychungcu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.thuctap.quanlychungcu.dto.ApiResponse;
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
    public ApiResponse<List<CanHoDTO>> getAllCanHo(){
        List<CanHo> canHoList = canHoService.findAll();
        List<CanHoDTO> canHoDTOList = canHoList.stream()
        .map(canHo -> canHoService.mapToCanHoDTO(canHo,true)).toList();
        return ApiResponse.<List<CanHoDTO>>builder().code(200)
        .result(canHoDTOList).build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CanHoDTO> getCanHo(@PathVariable("id") int id){
        CanHo canHo = canHoService.findById(id);
        if(canHo==null){
            return ApiResponse.<CanHoDTO>builder().code(404)
            .message("Không tìm thấy").build();
        }
        CanHoDTO canHoDTO = canHoService.mapToCanHoDTO(canHo,true);
        if(canHoDTO==null){
            return ApiResponse.<CanHoDTO>builder().code(404)
            .message("Không tìm thấy").build();
        }
        return ApiResponse.<CanHoDTO>builder().code(200)
        .result(canHoDTO).build();
    }

    @PostMapping
    public ApiResponse<CanHoDTO> insertCanHo(@RequestBody CanHoDTO canHoDTO) {
        CanHo canHo = canHoService.mapToCanHo(canHoDTO);
        canHo.setTrangThai(true);
        try{
            canHo = canHoService.save(canHo);
            if(canHoService.isExistsById(canHo.getIdCanHo())){
                CanHoDTO canHoDTO2 = canHoService.mapToCanHoDTO(canHo,false);
                return ApiResponse.<CanHoDTO>builder().code(200)
                .result(canHoDTO2).build();
            }
            else{
                return ApiResponse.<CanHoDTO>builder().code(400)
                .message("Thêm thất bại").build();
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return ApiResponse.<CanHoDTO>builder().code(400)
            .message(e.getMessage()).build();
        }
    }

    @PostMapping("/hinhanh")
    public ApiResponse<String> insertHinhAnhCanHo(@RequestParam("images")MultipartFile[] images, 
    @RequestParam("idCanHo") int idCanHo) {
        try{
            CanHo canHo = canHoService.findById(idCanHo);
            for(MultipartFile image: images){
                hinhAnhService.downloadHinhAnh(image, null, canHo);
            }
            return ApiResponse.<String>builder().code(200)
            .result("Thêm thành công").build();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return ApiResponse.<String>builder().code(400)
            .message(e.getMessage()).build();
        }
    }

    @PostMapping("/doihinhanh")
    public ApiResponse<String> changeHinhAnhCanHo(@RequestParam("images")MultipartFile[] images, 
    @RequestParam("idCanHo") int idCanHo) {
        try{
            CanHo canHo = canHoService.findById(idCanHo);
            String result = hinhAnhService.deleteAllHinhAnhCanHo(canHo);
            System.out.println(result);
            for(MultipartFile image: images){
                result=hinhAnhService.downloadHinhAnh(image, null, canHo);
                System.out.println(result);
            }
            return ApiResponse.<String>builder().code(200)
            .result("Đổi thành công").build();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return ApiResponse.<String>builder().code(400)
            .message(e.getMessage()).build();
        }
    }

    @PutMapping
    public ApiResponse<CanHoDTO> updateCanHo(@RequestBody CanHoDTO canHoDTO) {
        if (!canHoService.isExistsById(canHoDTO.getIdCanHo())) {
            return ApiResponse.<CanHoDTO>builder().code(400)
                .message("Căn hộ không tồn tại").build();
        }
        try{
            
            CanHo canHo = canHoService.mapToCanHo(canHoDTO);
            canHo=canHoService.save(canHo);
            if(!canHoService.updateDieuKhoan(canHoDTO, canHo)){
                return ApiResponse.<CanHoDTO>builder().code(400)
                .message("Lỗi update điều khoản").build();
            }
            canHo=canHoService.findById(canHoDTO.getIdCanHo());
            CanHoDTO canHoDTO2 = canHoService.mapToCanHoDTO(canHo,false);
            return ApiResponse.<CanHoDTO>builder().code(200)
            .result(canHoDTO2).build();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return ApiResponse.<CanHoDTO>builder().code(400)
                .message(e.getMessage()).build();
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<CanHoDTO> changePublicCanHo(@PathVariable int id) {
        if (!canHoService.isExistsById(id)) {
            return ApiResponse.<CanHoDTO>builder().code(400)
            .message("Căn hộ không tồn tại").build();
        }
        try{
            CanHo canHo = canHoService.findById(id);
            canHo.setTrangThai(!canHo.getTrangThai());
            canHo = canHoService.save(canHo);
            CanHoDTO canHoDTO2 = canHoService.mapToCanHoDTO(canHo,false);
            return ApiResponse.<CanHoDTO>builder().code(200)
            .result(canHoDTO2).build();
        }
        catch(Exception e){
            return ApiResponse.<CanHoDTO>builder().code(400)
            .message(e.getMessage()).build();
        }
    }


    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteCanHo(@PathVariable int id) {
        if (!canHoService.isExistsById(id)) {
            return ApiResponse.<String>builder().code(400)
            .message("Căn hộ không tồn tại").build();
        }
        try{
            CanHo canHo = canHoService.findById(id);
            String result = hinhAnhService.deleteAllHinhAnhCanHo(canHo);
            if(!result.contains("thành công")&&!result.contains("Không có")){
                System.out.println(result);
                return ApiResponse.<String>builder().code(400)
                .message("Xóa hình ảnh thất bại").build();
            }
            canHoService.deleteById(id);
            if (!canHoService.isExistsById(id)) {
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

    //Loại phòng

    @GetMapping("/loaiphong")
    public ApiResponse<List<LoaiPhongDTO>> getAllLoaiPhong(){
        List<LoaiPhong> loaiPhongList = loaiPhongService.findAll();
        List<LoaiPhongDTO> loaiPhongDTOList = loaiPhongList.stream()
        .map(loaiPhong -> loaiPhongService.mapToLoaiPhongDTO(loaiPhong)).toList();
        return ApiResponse.<List<LoaiPhongDTO>>builder().code(200)
                .result(loaiPhongDTOList).build();
    }

    @GetMapping("/loaiphong/{id}")
    public ApiResponse<LoaiPhongDTO> getLoaiPhong(@PathVariable("id") int id){
        LoaiPhong loaiPhong = loaiPhongService.findById(id);
        if(loaiPhong==null){
            return ApiResponse.<LoaiPhongDTO>builder().code(404)
                .message("Không tìm thấy").build();
        }
        LoaiPhongDTO loaiPhongDTO = loaiPhongService.mapToLoaiPhongDTO(loaiPhong);
        return ApiResponse.<LoaiPhongDTO>builder().code(200)
                .result(loaiPhongDTO).build();
    }

    @PostMapping("/loaiphong")
    public ApiResponse<LoaiPhongDTO> insertLoaiPhong(@RequestBody LoaiPhongDTO loaiPhongDTO) {
        LoaiPhong loaiPhong = loaiPhongService.mapToLoaiPhong(loaiPhongDTO);
        try{
            loaiPhong = loaiPhongService.save(loaiPhong);
            if(loaiPhongService.isExistsById(loaiPhong.getIdLoaiPhong())){
                LoaiPhongDTO loaiPhongDTO2 = loaiPhongService.mapToLoaiPhongDTO(loaiPhong);
                return ApiResponse.<LoaiPhongDTO>builder().code(200)
                .result(loaiPhongDTO2).build();
            }
            else{
                return ApiResponse.<LoaiPhongDTO>builder().code(400)
                .message("Lỗi lưu loại phòng").build();
            }
        }
        catch(Exception e){
            return ApiResponse.<LoaiPhongDTO>builder().code(400)
                .message(e.getMessage()).build();
        }
    }

    @PutMapping("/loaiphong")
    public ApiResponse<LoaiPhongDTO> updateLoaiPhong(@RequestBody LoaiPhongDTO loaiPhongDTO) {
        if (!loaiPhongService.isExistsById(loaiPhongDTO.getIdLoaiPhong())) {
            return ApiResponse.<LoaiPhongDTO>builder().code(400)
                .message("Loại phòng không tồn tại").build();
        }
        try{
            LoaiPhong loaiPhong = loaiPhongService.mapToLoaiPhong(loaiPhongDTO);
            loaiPhong = loaiPhongService.save(loaiPhong);
            LoaiPhongDTO loaiPhongDTO2 = loaiPhongService.mapToLoaiPhongDTO(loaiPhong);
            return ApiResponse.<LoaiPhongDTO>builder().code(400)
                .result(loaiPhongDTO2).build();
        }
        catch(Exception e){
            return ApiResponse.<LoaiPhongDTO>builder().code(400)
                .message(e.getMessage()).build();
        }
    }


    @DeleteMapping("/loaiphong/{id}")
    public ApiResponse<String> deleteLoaiPhong(@PathVariable int id) {
        if (!loaiPhongService.isExistsById(id)) {
            return ApiResponse.<String>builder().code(400)
                .message("Loại phòng không tồn tại").build();
        }
        try{
            loaiPhongService.deleteById(id);
            if (!loaiPhongService.isExistsById(id)) {
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
