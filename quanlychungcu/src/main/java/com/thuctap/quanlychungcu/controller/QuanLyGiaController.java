package com.thuctap.quanlychungcu.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thuctap.quanlychungcu.dto.ApiResponse;
import com.thuctap.quanlychungcu.dto.BanQuanLyDTO;
import com.thuctap.quanlychungcu.dto.BangGiaCTDTO;
import com.thuctap.quanlychungcu.dto.BangGiaDTO;
import com.thuctap.quanlychungcu.dto.CanHoBangGiaDTO;
import com.thuctap.quanlychungcu.dto.DichVuBangGiaDTO;
import com.thuctap.quanlychungcu.model.BangGia;
import com.thuctap.quanlychungcu.model.CanHo;
import com.thuctap.quanlychungcu.model.DichVu;
import com.thuctap.quanlychungcu.model.GiaCanHo;
import com.thuctap.quanlychungcu.model.GiaCanHoPK;
import com.thuctap.quanlychungcu.model.GiaDichVu;
import com.thuctap.quanlychungcu.model.GiaDichVuPK;
import com.thuctap.quanlychungcu.service.BanQuanLyService;
import com.thuctap.quanlychungcu.service.BangGiaService;
import com.thuctap.quanlychungcu.service.CanHoService;
import com.thuctap.quanlychungcu.service.DichVuService;

import jakarta.transaction.Transactional;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/banggia")
public class QuanLyGiaController {
    @Autowired
    BangGiaService bangGiaService;

    @Autowired
    CanHoService canHoService;

    @Autowired
    DichVuService dichVuService;

    @Autowired
    BanQuanLyService banQuanLyService;

    @GetMapping
    public ApiResponse<List<BangGiaDTO>> getAllBangGia(){
        List<BangGia> bangGiaList = bangGiaService.findAll();
        List<BangGiaDTO> bangGiaDTOList = bangGiaList.stream()
        .map(bangGia -> bangGiaService.mapToBangGiaDTO(bangGia)).toList();
        return ApiResponse.<List<BangGiaDTO>>builder().code(200)
        .result(bangGiaDTOList).build();
    }

    @GetMapping("/{id}")
    public ApiResponse<BangGiaCTDTO> getBangGia(@PathVariable("id") long id){
        if (!bangGiaService.isExistsById(id)) {
           return ApiResponse.<BangGiaCTDTO>builder().code(400)
            .message("Bảng giá không tồn tại").build();
        }
        BangGia bangGia = bangGiaService.findById(id);
        List<CanHoBangGiaDTO> canHoList = bangGiaService.findCanHoListById(id);
        List<DichVuBangGiaDTO> dichVuList = bangGiaService.findDichVuListById(id);
        BangGiaCTDTO bangGiaCTDTO = new BangGiaCTDTO();
        bangGiaCTDTO.setIdBangGia(bangGia.getIdBangGia());
        bangGiaCTDTO.setNoiDung(bangGia.getNoiDung());
        bangGiaCTDTO.setCanHoList(canHoList);
        bangGiaCTDTO.setDichVuList(dichVuList);
        if(bangGia.getBanQuanLy()!=null){
            BanQuanLyDTO banQuanLy = banQuanLyService.mapToBanQuanLyDTO(bangGia.getBanQuanLy());
            bangGiaCTDTO.setBanQuanLy(banQuanLy);
        }
        return ApiResponse.<BangGiaCTDTO>builder().code(200)
        .result(bangGiaCTDTO).build();
    }

    // @GetMapping("/canho/{id}")
    // public ApiResponse<?> getCanHoList(@PathVariable("id") long id){
    //     if (!bangGiaService.isExistsById(id)) {
    //         return new ApiResponse<>("Bảng giá không tồn tại", HttpStatus.BAD_REQUEST);
    //     }
    //     List<CanHoBangGiaDTO> canHoList = bangGiaService.findCanHoListById(id);
    //     return new ApiResponse<>(canHoList,HttpStatus.OK);
    // }

    // @GetMapping("/dichvu/{id}")
    // public ApiResponse<?> getDichVuList(@PathVariable("id") long id){
    //     if (!bangGiaService.isExistsById(id)) {
    //         return new ApiResponse<>("Bảng giá không tồn tại", HttpStatus.BAD_REQUEST);
    //     }
    //     List<DichVuBangGiaDTO> dichVuList = bangGiaService.findDichVuListById(id);
    //     return new ApiResponse<>(dichVuList,HttpStatus.OK);
    // }

    @PostMapping
    public ApiResponse<BangGiaDTO> insertBangGia(@RequestBody BangGiaDTO bangGiaDTO) {
        BangGia bangGia = bangGiaService.mapToBangGia(bangGiaDTO);
        try{
            bangGia.setApDung(false);
            bangGia = bangGiaService.save(bangGia);
            if(bangGiaService.isExistsById(bangGia.getIdBangGia())){
                BangGiaDTO bangGiaDTO2 = bangGiaService.mapToBangGiaDTO(bangGia);
                return ApiResponse.<BangGiaDTO>builder().code(200)
                .result(bangGiaDTO2).build();
            }
            else{
                return ApiResponse.<BangGiaDTO>builder().code(400)
                .message("Lỗi thêm bảng giá").build();
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return ApiResponse.<BangGiaDTO>builder().code(400)
                .message(e.getMessage()).build();
        }
    }

    
    @PutMapping
    @Transactional
    public ApiResponse<String> updateBangGia(@RequestBody BangGiaCTDTO bangGiaCTDTO) {
        long idBangGia = bangGiaCTDTO.getIdBangGia();
        if (!bangGiaService.isExistsById(idBangGia)) {
            return ApiResponse.<String>builder().code(400)
                .message("Bảng giá không tồn tại").build();
        }
        try{
            BangGia bangGia = bangGiaService.findById(idBangGia);
            bangGia.setNoiDung(bangGiaCTDTO.getNoiDung());
            bangGiaService.save(bangGia);
            List<CanHoBangGiaDTO> canHoList = bangGiaCTDTO.getCanHoList();
            List<DichVuBangGiaDTO> dichVuList = bangGiaCTDTO.getDichVuList();
            if(canHoList!=null){
                List<CanHoBangGiaDTO> canHoListOld = bangGiaService.findCanHoListById(idBangGia);
                for(CanHoBangGiaDTO x: canHoListOld){
                    if(!canHoList.contains(x)){
                        GiaCanHoPK canHoPK = new GiaCanHoPK();
                        canHoPK.setIdBangGia(idBangGia);
                        canHoPK.setIdCanHo(x.getIdCanHo());
                        bangGiaService.deleteGiaCanHoById(canHoPK);
                    }
                }
                for(CanHoBangGiaDTO x: canHoList){
                    CanHo canHo = canHoService.findById(x.getIdCanHo());
                    GiaCanHoPK canHoPK = new GiaCanHoPK();
                    canHoPK.setIdBangGia(idBangGia);
                    canHoPK.setIdCanHo(x.getIdCanHo());
                    GiaCanHo giaCanHo = new GiaCanHo();
                    giaCanHo.setGiaCanHoPK(canHoPK);
                    giaCanHo.setBangGia(bangGia);
                    giaCanHo.setCanHo(canHo);
                    giaCanHo.setGia(x.getGiaKhuyenMai());
                    bangGiaService.saveGiaCanHo(giaCanHo);
                }
            }
            if(dichVuList!=null){
                List<DichVuBangGiaDTO> dichVuListOld = bangGiaService.findDichVuListById(idBangGia);
                for(DichVuBangGiaDTO x: dichVuListOld){
                    if(!dichVuList.contains(x)){
                        GiaDichVuPK giaDichVuPK = new GiaDichVuPK();
                        giaDichVuPK.setIdBangGia(idBangGia);
                        giaDichVuPK.setIdDichVu(x.getIdDichVu());
                        bangGiaService.deleteGiaDichVuById(giaDichVuPK);
                    }
                }
                for(DichVuBangGiaDTO x: dichVuList){
                    DichVu dichVu = dichVuService.findById(x.getIdDichVu());
                    GiaDichVuPK giaDichVuPK = new GiaDichVuPK();
                    giaDichVuPK.setIdBangGia(idBangGia);
                    giaDichVuPK.setIdDichVu(x.getIdDichVu());
                    GiaDichVu giaDichVu = new GiaDichVu();
                    giaDichVu.setGiaDichVuPK(giaDichVuPK);
                    giaDichVu.setBangGia(bangGia);
                    giaDichVu.setDichVu(dichVu);
                    giaDichVu.setGia(x.getGiaKhuyenMai());
                    
                    bangGiaService.saveGiaDichVu(giaDichVu);
                }
            }
            return ApiResponse.<String>builder().code(200)
                .message("Thành công").build();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return ApiResponse.<String>builder().code(400)
                .message(e.getMessage()).build();
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<BangGiaDTO> applyBangGia(@PathVariable long id) {
        if (!bangGiaService.isExistsById(id)) {
            return ApiResponse.<BangGiaDTO>builder().code(400)
                .message("Bảng giá không tồn tại").build();
        }
        try{
            BangGia bangGia = bangGiaService.findById(id);
            bangGia.setApDung(!bangGia.getApDung());
            bangGia = bangGiaService.save(bangGia);
            BangGiaDTO bangGiaDTO = bangGiaService.mapToBangGiaDTO(bangGia);
            return ApiResponse.<BangGiaDTO>builder().code(200)
                .result(bangGiaDTO).build();
        }
        catch(Exception e){
            return ApiResponse.<BangGiaDTO>builder().code(400)
                .message(e.getMessage()).build();
        }
    }

    @PutMapping("/uploadcanho/{id}")
    public ApiResponse<String> uploadCanHoBangGia(@PathVariable long id) {
        if (!bangGiaService.isExistsById(id)) {
            return ApiResponse.<String>builder().code(400)
                .message("Bảng giá không tồn tại").build();
        }
        try{
            BangGia bangGia = bangGiaService.findById(id);
            List<CanHo> canHoList = canHoService.findAll();
            for(CanHo x: canHoList){
                GiaCanHoPK canHoPK = new GiaCanHoPK();
                canHoPK.setIdBangGia(id);
                canHoPK.setIdCanHo(x.getIdCanHo());
                GiaCanHo giaCanHo = new GiaCanHo();
                giaCanHo.setGiaCanHoPK(canHoPK);
                giaCanHo.setBangGia(bangGia);
                giaCanHo.setCanHo(x);
                giaCanHo.setGia(x.getGiaThue());
                bangGiaService.saveGiaCanHo(giaCanHo);
            }
            return ApiResponse.<String>builder().code(200)
                .result("Upload thành công").build();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return ApiResponse.<String>builder().code(400)
                .message(e.getMessage()).build();
        }
    }

    @PutMapping("/uploaddichvu/{id}")
    public ApiResponse<String> uploadDichVuBangGia(@PathVariable long id) {
        if (!bangGiaService.isExistsById(id)) {
            return ApiResponse.<String>builder().code(400)
                .message("Bảng giá không tồn tại").build();
        }
        try{
            BangGia bangGia = bangGiaService.findById(id);
            List<DichVu> dichVuList = dichVuService.findAll();
            for(DichVu x: dichVuList){
                GiaDichVuPK giaDichVuPK = new GiaDichVuPK();
                giaDichVuPK.setIdBangGia(id);
                giaDichVuPK.setIdDichVu(x.getIdDichVu());
                GiaDichVu giaDichVu = new GiaDichVu();
                giaDichVu.setGiaDichVuPK(giaDichVuPK);
                giaDichVu.setBangGia(bangGia);
                giaDichVu.setDichVu(x);
                giaDichVu.setGia(x.getGiaHienTai());
                
                bangGiaService.saveGiaDichVu(giaDichVu);
            }
            return ApiResponse.<String>builder().code(200)
                .result("Upload thành công").build();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return ApiResponse.<String>builder().code(400)
                .message(e.getMessage()).build();
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteBangGia(@PathVariable long id) {
        if (!bangGiaService.isExistsById(id)) {
            return ApiResponse.<String>builder().code(400)
                .message("Bảng giá không tồn tại").build();
        }
        try{
            bangGiaService.deleteById(id);
            if (!bangGiaService.isExistsById(id)) {
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
