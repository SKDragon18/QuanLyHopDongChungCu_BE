package com.thuctap.quanlychungcu.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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
import com.thuctap.quanlychungcu.dto.HoaDonDTO;
import com.thuctap.quanlychungcu.dto.HopDongDTO;
import com.thuctap.quanlychungcu.dto.HopDongDichVuKhachHangDTO;
import com.thuctap.quanlychungcu.dto.HopDongKhachHangDTO;
import com.thuctap.quanlychungcu.dto.YeuCauDichVuDTO;
import com.thuctap.quanlychungcu.model.CTDKCanHo;
import com.thuctap.quanlychungcu.model.CTDKDichVu;
import com.thuctap.quanlychungcu.model.CTHopDong;
import com.thuctap.quanlychungcu.model.CTYeuCauDichVu;
import com.thuctap.quanlychungcu.model.CanHo;
import com.thuctap.quanlychungcu.model.DichVu;
import com.thuctap.quanlychungcu.model.HoaDon;
import com.thuctap.quanlychungcu.model.HopDong;
import com.thuctap.quanlychungcu.model.KhachHang;
import com.thuctap.quanlychungcu.model.YeuCauDichVu;
import com.thuctap.quanlychungcu.service.DieuKhoanService;
import com.thuctap.quanlychungcu.service.HoaDonService;
import com.thuctap.quanlychungcu.service.HopDongService;
import com.thuctap.quanlychungcu.service.KhachHangService;
import com.thuctap.quanlychungcu.service.YeuCauDichVuService;

import jakarta.transaction.Transactional;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/hopdong")
public class QuanLyHopDongController {
    @Autowired
    HopDongService hopDongService;

    @Autowired
    YeuCauDichVuService yeuCauDichVuService;

    @Autowired
    HoaDonService hoaDonService;

    @Autowired
    KhachHangService khachHangService;

    @Autowired
    DieuKhoanService dieuKhoanService;

    public Timestamp getNow(){
        Date date = new Date();
        return new Timestamp(date.getTime());
    }

    public Timestamp convertToUTC(Timestamp time){
        LocalDateTime localDateTime = time.toLocalDateTime();
        localDateTime = localDateTime.minusHours(7);//trừ 7 tiếng
        return Timestamp.valueOf(localDateTime);
    }

    public Timestamp plusDay(Timestamp time, int chuKy){
        LocalDateTime localDateTime = time.toLocalDateTime();
        localDateTime = localDateTime.plusDays(chuKy);
        return Timestamp.valueOf(localDateTime);
    }

    @GetMapping
    public ResponseEntity<List<HopDongKhachHangDTO>> getAllHopDong(){
        List<HopDong> hopDongList = hopDongService.findAll();
        List<HopDongKhachHangDTO> hopDongDTOList = hopDongList.stream()
        .map(hopDong -> hopDongService.mapToHopDongKhachHangDTO(hopDong)).toList();
        return new ResponseEntity<>(hopDongDTOList,HttpStatus.OK);
    }

    @GetMapping("/khachhang/{id}")
    public ResponseEntity<?> getHopDongKhachHang(@PathVariable("id") long id){
        if(!hopDongService.isExistsById(id)){
            return new ResponseEntity<>("Không tồn tại hợp đồng",HttpStatus.BAD_REQUEST);
        }
        try{
            HopDong hopDong = hopDongService.findById(id);
            HopDongKhachHangDTO hopDongDTO = hopDongService.mapToHopDongKhachHangDTO(hopDong);
            List<DieuKhoanDTO> dieuKhoanList = new ArrayList<>();
            for(CTHopDong x: hopDong.getChiTietHopDongList()){
                dieuKhoanList.add(dieuKhoanService.mapToDieuKhoanDTO(x.getDieuKhoan()));
            }
            hopDongDTO.setDieuKhoanList(dieuKhoanList);
            return new ResponseEntity<>(hopDongDTO,HttpStatus.OK);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/khachhangall/{id}")
    public ResponseEntity<?> getAllHopDongKhachHang(@PathVariable("id") String id){
        if(!khachHangService.isExistsById(id)){
            return new ResponseEntity<>("Không tồn tại khách hàng",HttpStatus.BAD_REQUEST);
        }
        try{
            List<HopDong> hopDongList = hopDongService.findAllByMaKhachHang(id);
            List<HopDongKhachHangDTO> hopDongDTOList = hopDongList.stream()
            .map(hopDong -> hopDongService.mapToHopDongKhachHangDTO(hopDong)).toList();
            return new ResponseEntity<>(hopDongDTOList,HttpStatus.OK);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
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
        
        try{
            HopDong hopDong = hopDongService.mapToHopDong(hopDongDTO);
            hopDong.setNgayLap(getNow());
            hopDong.setTrangThai(false);
            hopDong.setChuKy(hopDong.getCanHo().getChuKy());
            hopDong = hopDongService.save(hopDong);
            CanHo canHo = hopDong.getCanHo();
            List<CTDKCanHo> chiTietDieuKhoanCanHoList = canHo.getChiTietDieuKhoanCanHoList();
            for(CTDKCanHo x: chiTietDieuKhoanCanHoList){
                CTHopDong chiTietHopDong = new CTHopDong();
                chiTietHopDong.setDieuKhoan(x.getDieuKhoan());
                chiTietHopDong.setHopDong(hopDong);
                hopDongService.saveCTHopDong(chiTietHopDong);
            } 
            if(hopDongService.isExistsById(hopDong.getIdHopDong())){
                HoaDon hoaDon = new HoaDon();
                hoaDon.setHopDong(hopDong);
                hoaDon.setThoiGianDong(getNow());
                hoaDon.setTongHoaDon(hopDong.getGiaTri());
                hoaDon = hoaDonService.save(hoaDon);
                if(hoaDonService.isExistsById(hoaDon.getSoHoaDon())){
                    return new ResponseEntity<>("Đăng ký thành công",HttpStatus.OK);
                }
                else{
                    return new ResponseEntity<>("Đăng ký thành công, thanh toán thất bại",HttpStatus.OK);
                }
            }
            else{
                return new ResponseEntity<>("Đăng ký thất bại",HttpStatus.BAD_REQUEST);
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    // @PutMapping
    // public ResponseEntity<?> updateHopDong(@RequestBody HopDongDTO hopDongDTO) {
    //     if (!hopDongService.isExistsById(hopDongDTO.getIdHopDong())) {
    //         return new ResponseEntity<>("Hợp đồng không tồn tại", HttpStatus.BAD_REQUEST);
    //     }
    //     try{
    //         HopDong hopDong = hopDongService.mapToHopDong(hopDongDTO);
    //         hopDong = hopDongService.save(hopDong);
    //         HopDongDTO hopDongDTO2 = hopDongService.mapToHopDongDTO(hopDong);
    //         return new ResponseEntity<>(hopDongDTO2, HttpStatus.OK);
    //     }
    //     catch(Exception e){
    //         return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
    //     }
    // }

    //Gia hạn
    @PutMapping("/giahan/{id}")
    public ResponseEntity<?> giaHanHopDong(@PathVariable long id) {
        if (!hopDongService.isExistsById(id)) {
            return new ResponseEntity<>("Hợp đồng không tồn tại", HttpStatus.BAD_REQUEST);
        }
        try{
            HopDong hopDong = hopDongService.findById(id);
            hopDong.setThoiHan(plusDay(hopDong.getThoiHan(), hopDong.getChuKy()));
            hopDong=hopDongService.save(hopDong);

            HoaDon hoaDon = new HoaDon();
            hoaDon.setHopDong(hopDong);
            hoaDon.setThoiGianDong(getNow());
            hoaDon.setTongHoaDon(hopDong.getGiaTri());
            hoaDon = hoaDonService.save(hoaDon);
            if (hoaDonService.isExistsById(hoaDon.getSoHoaDon())) {
                return new ResponseEntity<>("Gia hạn thành công", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Gia hạn thất bại", HttpStatus.BAD_REQUEST);
            }
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    //Hủy hợp đồng
    @PutMapping("/huy/{id}")
    public ResponseEntity<?> huyHopDong(@PathVariable long id) {
        if (!hopDongService.isExistsById(id)) {
            return new ResponseEntity<>("Hợp đồng không tồn tại", HttpStatus.BAD_REQUEST);
        }
        try{
            HopDong hopDong = hopDongService.findById(id);
            hopDong.setTrangThai(true);
            hopDong=hopDongService.save(hopDong);
            return new ResponseEntity<>("Hủy thành công, bạn vẫn có thể sử dụng căn hộ trước thời hạn", HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/dichvu")
    public ResponseEntity<List<HopDongDichVuKhachHangDTO>> getAllHopDongDichVu(){
        List<YeuCauDichVu> yeuCauDichVuList = yeuCauDichVuService.findAll();
        List<HopDongDichVuKhachHangDTO> hopDongDTOList = yeuCauDichVuList.stream()
        .map(yeuCauDichVu -> hopDongService.mapToHopDongDichVuKhachHangDTO(yeuCauDichVu)).toList();
        return new ResponseEntity<>(hopDongDTOList,HttpStatus.OK);
    }

    //Hợp đồng dịch vụ
    @GetMapping("/dichvu/khachhang/{id}")
    public ResponseEntity<?> getHopDongDichVuKhachHang(@PathVariable("id") long id){
        if(!yeuCauDichVuService.isExistsById(id)){
            return new ResponseEntity<>("Không tồn tại hợp đồng",HttpStatus.BAD_REQUEST);
        }
        try{
            YeuCauDichVu yeuCauDichVu = yeuCauDichVuService.findById(id);
            HopDongDichVuKhachHangDTO hopDongDTO = hopDongService.mapToHopDongDichVuKhachHangDTO(yeuCauDichVu);
            List<DieuKhoanDTO> dieuKhoanList = new ArrayList<>();
            for(CTYeuCauDichVu x: yeuCauDichVu.getChiTietYeuCauDichVuList()){
                dieuKhoanList.add(dieuKhoanService.mapToDieuKhoanDTO(x.getDieuKhoan()));
            }
            hopDongDTO.setDieuKhoanList(dieuKhoanList);
            return new ResponseEntity<>(hopDongDTO,HttpStatus.OK);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/dichvu/khachhangall/{id}")
    public ResponseEntity<?> getAllHopDongDichVuKhachHang(@PathVariable("id") String id){
        if(!khachHangService.isExistsById(id)){
            return new ResponseEntity<>("Không tồn tại khách hàng",HttpStatus.BAD_REQUEST);
        }
        try{
            List<YeuCauDichVu> yeuCauDichVuList = hopDongService.findAllDichVuByMaKhachHang(id);
            List<HopDongDichVuKhachHangDTO> hopDongDTOList = yeuCauDichVuList.stream()
            .map(yeuCauDichVu -> hopDongService.mapToHopDongDichVuKhachHangDTO(yeuCauDichVu)).toList();
            return new ResponseEntity<>(hopDongDTOList,HttpStatus.OK);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/dichvu")
    public ResponseEntity<?> insertHopDongDichVu(@RequestBody YeuCauDichVuDTO yeuCauDichVuDTO) {
        try{
            YeuCauDichVu yeuCauDichVu = hopDongService.mapToYeuCauDichVu(yeuCauDichVuDTO);
            yeuCauDichVu.setNgayYeuCau(getNow());
            yeuCauDichVu.setTrangThai(false);
            yeuCauDichVu.setChuKy(yeuCauDichVu.getDichVu().getChuKy());
            yeuCauDichVu = hopDongService.saveDichVu(yeuCauDichVu);
            DichVu dichVu = yeuCauDichVu.getDichVu();
            List<CTDKDichVu> chiTietDieuKhoanDichVuList = dichVu.getChiTietDieuKhoanDichVuList();
            for(CTDKDichVu x: chiTietDieuKhoanDichVuList){
                CTYeuCauDichVu chiTietYeuCauDichVu = new CTYeuCauDichVu();
                chiTietYeuCauDichVu.setDieuKhoan(x.getDieuKhoan());
                chiTietYeuCauDichVu.setYeuCauDichVu(yeuCauDichVu);
                hopDongService.saveCTYeuCauDichVu(chiTietYeuCauDichVu);
            } 
            if(hopDongService.isExistsDichVuById(yeuCauDichVu.getIdYeuCauDichVu())){
                HoaDon hoaDon = new HoaDon();
                hoaDon.setYeuCauDichVu(yeuCauDichVu);
                hoaDon.setThoiGianDong(getNow());
                hoaDon.setTongHoaDon(yeuCauDichVu.getGiaTra());
                hoaDon = hoaDonService.save(hoaDon);
                if(hoaDonService.isExistsById(hoaDon.getSoHoaDon())){
                    return new ResponseEntity<>("Đăng ký thành công",HttpStatus.OK);
                }
                else{
                    return new ResponseEntity<>("Đăng ký thành công, thanh toán thất bại",HttpStatus.OK);
                }
            }
            else{
                return new ResponseEntity<>("Đăng ký thất bại",HttpStatus.BAD_REQUEST);
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    //Gia hạn
    @PutMapping("/dichvu/giahan/{id}")
    public ResponseEntity<?> giaHanHopDongDichVu(@PathVariable long id) {
        if (!hopDongService.isExistsDichVuById(id)) {
            return new ResponseEntity<>("Hợp đồng dịch vụ không tồn tại", HttpStatus.BAD_REQUEST);
        }
        try{
            YeuCauDichVu yeuCauDichVu = hopDongService.findDichVuById(id);
            yeuCauDichVu.setThoiHan(plusDay(yeuCauDichVu.getThoiHan(), yeuCauDichVu.getChuKy()));
            yeuCauDichVu=hopDongService.saveDichVu(yeuCauDichVu);
            HoaDon hoaDon = new HoaDon();
            hoaDon.setYeuCauDichVu(yeuCauDichVu);
            hoaDon.setThoiGianDong(getNow());
            hoaDon.setTongHoaDon(yeuCauDichVu.getGiaTra());
            hoaDon = hoaDonService.save(hoaDon);
            if (hoaDonService.isExistsById(hoaDon.getSoHoaDon())) {
                return new ResponseEntity<>("Gia hạn thành công", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Gia hạn thất bại", HttpStatus.BAD_REQUEST);
            }
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    //Hủy hợp đồng
    @PutMapping("dichvu/huy/{id}")
    public ResponseEntity<?> huyHopDongDichVu(@PathVariable long id) {
        if (!hopDongService.isExistsDichVuById(id)) {
            return new ResponseEntity<>("Hợp đồng dịch vụ không tồn tại", HttpStatus.BAD_REQUEST);
        }
        try{
            YeuCauDichVu yeuCauDichVu = hopDongService.findDichVuById(id);
            yeuCauDichVu.setTrangThai(true);
            yeuCauDichVu=hopDongService.saveDichVu(yeuCauDichVu);
            return new ResponseEntity<>("Hủy thành công, bạn vẫn có thể sử dụng dịch vụ trước thời hạn", HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

}
