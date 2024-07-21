package com.thuctap.quanlychungcu.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    public Timestamp convertToUTC(Timestamp time){
        LocalDateTime localDateTime = time.toLocalDateTime();
        localDateTime = localDateTime.minusHours(7);//trừ 7 tiếng
        return Timestamp.valueOf(localDateTime);
    }

    @GetMapping
    public ResponseEntity<List<BangGiaDTO>> getAllBangGia(){
        List<BangGia> bangGiaList = bangGiaService.findAll();
        List<BangGiaDTO> bangGiaDTOList = bangGiaList.stream()
        .map(bangGia -> bangGiaService.mapToBangGiaDTO(bangGia)).toList();
        return new ResponseEntity<>(bangGiaDTOList,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBangGia(@PathVariable("id") long id){
        if (!bangGiaService.isExistsById(id)) {
            return new ResponseEntity<>("Bảng giá không tồn tại", HttpStatus.BAD_REQUEST);
        }
        BangGia bangGia = bangGiaService.findById(id);
        List<CanHoBangGiaDTO> canHoList = bangGiaService.findCanHoListById(id);
        List<DichVuBangGiaDTO> dichVuList = bangGiaService.findDichVuListById(id);
        BangGiaCTDTO bangGiaCTDTO = new BangGiaCTDTO();
        bangGiaCTDTO.setIdBangGia(bangGia.getIdBangGia());
        bangGiaCTDTO.setNoiDung(bangGia.getNoiDung());
        bangGiaCTDTO.setCanHoList(canHoList);
        bangGiaCTDTO.setDichVuList(dichVuList);
        return new ResponseEntity<>(bangGiaCTDTO,HttpStatus.OK);
    }

    // @GetMapping("/canho/{id}")
    // public ResponseEntity<?> getCanHoList(@PathVariable("id") long id){
    //     if (!bangGiaService.isExistsById(id)) {
    //         return new ResponseEntity<>("Bảng giá không tồn tại", HttpStatus.BAD_REQUEST);
    //     }
    //     List<CanHoBangGiaDTO> canHoList = bangGiaService.findCanHoListById(id);
    //     return new ResponseEntity<>(canHoList,HttpStatus.OK);
    // }

    // @GetMapping("/dichvu/{id}")
    // public ResponseEntity<?> getDichVuList(@PathVariable("id") long id){
    //     if (!bangGiaService.isExistsById(id)) {
    //         return new ResponseEntity<>("Bảng giá không tồn tại", HttpStatus.BAD_REQUEST);
    //     }
    //     List<DichVuBangGiaDTO> dichVuList = bangGiaService.findDichVuListById(id);
    //     return new ResponseEntity<>(dichVuList,HttpStatus.OK);
    // }

    @PostMapping
    public ResponseEntity<?> insertBangGia(@RequestBody BangGiaDTO bangGiaDTO) {
        BangGia bangGia = bangGiaService.mapToBangGia(bangGiaDTO);
        try{
            bangGia.setApDung(false);
            bangGia.setThoiGianBatDau(convertToUTC(bangGia.getThoiGianBatDau()));
            bangGia.setThoiGianKetThuc(convertToUTC(bangGia.getThoiGianKetThuc()));
            bangGia = bangGiaService.save(bangGia);
            if(bangGiaService.isExistsById(bangGia.getIdBangGia())){
                BangGiaDTO bangGiaDTO2 = bangGiaService.mapToBangGiaDTO(bangGia);
                return new ResponseEntity<>(bangGiaDTO2,HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("Lỗi thêm bảng giá",HttpStatus.BAD_REQUEST);
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    
    @PutMapping
    @Transactional
    public ResponseEntity<?> updateBangGia(@RequestBody BangGiaCTDTO bangGiaCTDTO) {
        long idBangGia = bangGiaCTDTO.getIdBangGia();
        if (!bangGiaService.isExistsById(idBangGia)) {
            return new ResponseEntity<>("Bảng giá không tồn tại", HttpStatus.BAD_REQUEST);
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
            return new ResponseEntity<>("Thành công", HttpStatus.OK);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> applyBangGia(@PathVariable long id) {
        if (!bangGiaService.isExistsById(id)) {
            return new ResponseEntity<>("Bảng giá không tồn tại", HttpStatus.BAD_REQUEST);
        }
        try{
            BangGia bangGia = bangGiaService.findById(id);
            bangGia.setApDung(!bangGia.getApDung());
            bangGia = bangGiaService.save(bangGia);
            BangGiaDTO bangGiaDTO = bangGiaService.mapToBangGiaDTO(bangGia);
            return new ResponseEntity<>(bangGiaDTO, HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/uploadcanho/{id}")
    public ResponseEntity<?> uploadCanHoBangGia(@PathVariable long id) {
        if (!bangGiaService.isExistsById(id)) {
            return new ResponseEntity<>("Bảng giá không tồn tại", HttpStatus.BAD_REQUEST);
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
            return new ResponseEntity<>("Upload thành công", HttpStatus.OK);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/uploaddichvu/{id}")
    public ResponseEntity<?> uploadDichVuBangGia(@PathVariable long id) {
        if (!bangGiaService.isExistsById(id)) {
            return new ResponseEntity<>("Bảng giá không tồn tại", HttpStatus.BAD_REQUEST);
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
            return new ResponseEntity<>("Upload thành công", HttpStatus.OK);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
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
