package com.thuctap.quanlychungcu.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thuctap.quanlychungcu.dto.ApiResponse;
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
    public ApiResponse<List<HopDongKhachHangDTO>> getAllHopDong(){
        List<HopDong> hopDongList = hopDongService.findAll();
        List<HopDongKhachHangDTO> hopDongDTOList = hopDongList.stream()
        .map(hopDong -> hopDongService.mapToHopDongKhachHangDTO(hopDong)).toList();
        return ApiResponse.<List<HopDongKhachHangDTO>>builder().code(200)
                .result(hopDongDTOList).build();
    }

    @GetMapping("/khachhang/{id}")
    public ApiResponse<HopDongKhachHangDTO> getHopDongKhachHang(@PathVariable("id") long id){
        if(!hopDongService.isExistsById(id)){
            return ApiResponse.<HopDongKhachHangDTO>builder().code(400)
                .message("Không tồn tại hợp đồng").build();
        }
        try{
            HopDong hopDong = hopDongService.findById(id);
            HopDongKhachHangDTO hopDongDTO = hopDongService.mapToHopDongKhachHangDTO(hopDong);
            List<DieuKhoanDTO> dieuKhoanList = new ArrayList<>();
            for(CTHopDong x: hopDong.getChiTietHopDongList()){
                dieuKhoanList.add(dieuKhoanService.mapToDieuKhoanDTO(x.getDieuKhoan()));
            }
            hopDongDTO.setDieuKhoanList(dieuKhoanList);
            return ApiResponse.<HopDongKhachHangDTO>builder().code(200)
                .result(hopDongDTO).build();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return ApiResponse.<HopDongKhachHangDTO>builder().code(400)
                .message(e.getMessage()).build();
        }
    }

    @GetMapping("/khachhangall/{id}")
    public ApiResponse<List<HopDongKhachHangDTO>> getAllHopDongKhachHang(@PathVariable("id") String id){
        if(!khachHangService.isExistsById(id)){
            return ApiResponse.<List<HopDongKhachHangDTO>>builder().code(400)
                .message("Không tồn tại khách hàng").build();
        }
        try{
            List<HopDong> hopDongList = hopDongService.findAllByMaKhachHang(id);
            List<HopDongKhachHangDTO> hopDongDTOList = hopDongList.stream()
            .map(hopDong -> hopDongService.mapToHopDongKhachHangDTO(hopDong)).toList();
            return ApiResponse.<List<HopDongKhachHangDTO>>builder().code(200)
                .result(hopDongDTOList).build();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return ApiResponse.<List<HopDongKhachHangDTO>>builder().code(400)
                .message(e.getMessage()).build();
        }
    }

    @GetMapping("/{id}")
    public ApiResponse<HopDongDTO> getHopDong(@PathVariable("id") long id){
        HopDong hopDong = hopDongService.findById(id);
        if(hopDong==null){
            return ApiResponse.<HopDongDTO>builder().code(404)
                .message("Không tìm thấy").build();
        }
        HopDongDTO hopDongDTO = hopDongService.mapToHopDongDTO(hopDong);
        return ApiResponse.<HopDongDTO>builder().code(200)
                .result(hopDongDTO).build();
    }

    @PostMapping
    public ApiResponse<String> insertHopDong(@RequestBody HopDongDTO hopDongDTO) {
        
        try{
            HopDong hopDong = hopDongService.mapToHopDong(hopDongDTO);
            // hopDong.setNgayLap(getNow());
            // hopDong.setNgayLap(convertToUTC(hopDong.getNgayLap()));
            hopDong.setNgayBatDau(convertToUTC(hopDong.getNgayBatDau()));
            hopDong.setThoiHan(convertToUTC(hopDong.getThoiHan()));
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
                HoaDon hoaDon = hoaDonService.findById(hopDongDTO.getSoHoaDon());
                if(hoaDon==null){
                    return ApiResponse.<String>builder().code(404)
                    .message("Không tìm thấy hóa đơn").build();
                }
                hoaDon.setHopDong(hopDong);
                hoaDon.setThoiGianDong(getNow());
                hoaDon = hoaDonService.save(hoaDon);
                if(hoaDonService.isExistsById(hoaDon.getSoHoaDon())){
                    return ApiResponse.<String>builder().code(200)
                    .result("Đăng ký thành công").build();
                }
                else{
                    return ApiResponse.<String>builder().code(200)
                    .result("Đăng ký thành công, thanh toán thất bại").build();
                }
            }
            else{
                return ApiResponse.<String>builder().code(400)
                    .message("Đăng ký thất bại").build();
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return ApiResponse.<String>builder().code(400)
                    .message(e.getMessage()).build();
        }
    }

    // @PutMapping
    // public ApiResponse<?> updateHopDong(@RequestBody HopDongDTO hopDongDTO) {
    //     if (!hopDongService.isExistsById(hopDongDTO.getIdHopDong())) {
    //         return new ApiResponse<>("Hợp đồng không tồn tại", HttpStatus.BAD_REQUEST);
    //     }
    //     try{
    //         HopDong hopDong = hopDongService.mapToHopDong(hopDongDTO);
    //         hopDong = hopDongService.save(hopDong);
    //         HopDongDTO hopDongDTO2 = hopDongService.mapToHopDongDTO(hopDong);
    //         return new ApiResponse<>(hopDongDTO2, HttpStatus.OK);
    //     }
    //     catch(Exception e){
    //         return new ApiResponse<>(e.getMessage(),HttpStatus.BAD_REQUEST);
    //     }
    // }

    //Gia hạn
    @PutMapping("/giahan/{id}/{soHoaDon}")
    public ApiResponse<String> giaHanHopDong(@PathVariable(value="id") long id,
     @PathVariable(value = "soHoaDon")long soHoaDon) {
        if (!hopDongService.isExistsById(id)) {
            return ApiResponse.<String>builder().code(400)
                    .message("Hợp đồng không tồn tại").build();
        }
        try{
            HopDong hopDong = hopDongService.findById(id);
            hopDong.setThoiHan(plusDay(hopDong.getThoiHan(), hopDong.getChuKy()));
            hopDong=hopDongService.save(hopDong);

            HoaDon hoaDon = hoaDonService.findById(soHoaDon);
            if(hoaDon==null){
                return ApiResponse.<String>builder().code(404)
                    .message("Không tìm thấy hóa đơn").build();
            }
            hoaDon.setHopDong(hopDong);
            hoaDon.setThoiGianDong(getNow());
            hoaDon = hoaDonService.save(hoaDon);
            if (hoaDonService.isExistsById(hoaDon.getSoHoaDon())) {
                return ApiResponse.<String>builder().code(200)
                    .result("Gia hạn thành công").build();
            } else {
                return ApiResponse.<String>builder().code(400)
                    .message("Gia hạn thất bại").build();
            }
        }
        catch(Exception e){
            return ApiResponse.<String>builder().code(400)
                    .message(e.getMessage()).build();
        }
    }

    //Hủy hợp đồng
    @PutMapping("/huy/{id}")
    public ApiResponse<String> huyHopDong(@PathVariable long id) {
        if (!hopDongService.isExistsById(id)) {
            return ApiResponse.<String>builder().code(400)
                    .message("Hợp đồng không tồn tại").build();
        }
        try{
            HopDong hopDong = hopDongService.findById(id);
            hopDong.setTrangThai(true);
            hopDong=hopDongService.save(hopDong);
            return ApiResponse.<String>builder().code(200)
                    .result("Hủy thành công, bạn vẫn có thể sử dụng căn hộ trước thời hạn").build();
        }
        catch(Exception e){
            return ApiResponse.<String>builder().code(400)
                    .message(e.getMessage()).build();
        }
    }

    @GetMapping("/dichvu")
    public ApiResponse<List<HopDongDichVuKhachHangDTO>> getAllHopDongDichVu(){
        List<YeuCauDichVu> yeuCauDichVuList = yeuCauDichVuService.findAll();
        List<HopDongDichVuKhachHangDTO> hopDongDTOList = yeuCauDichVuList.stream()
        .map(yeuCauDichVu -> hopDongService.mapToHopDongDichVuKhachHangDTO(yeuCauDichVu)).toList();
        return ApiResponse.<List<HopDongDichVuKhachHangDTO>>builder().code(200)
                    .result(hopDongDTOList).build();
    }

    //Hợp đồng dịch vụ
    @GetMapping("/dichvu/khachhang/{id}")
    public ApiResponse<HopDongDichVuKhachHangDTO> getHopDongDichVuKhachHang(@PathVariable("id") long id){
        if(!yeuCauDichVuService.isExistsById(id)){
            return ApiResponse.<HopDongDichVuKhachHangDTO>builder().code(400)
                    .message("Không tồn tại hợp đồng").build();
        }
        try{
            YeuCauDichVu yeuCauDichVu = yeuCauDichVuService.findById(id);
            HopDongDichVuKhachHangDTO hopDongDTO = hopDongService.mapToHopDongDichVuKhachHangDTO(yeuCauDichVu);
            List<DieuKhoanDTO> dieuKhoanList = new ArrayList<>();
            for(CTYeuCauDichVu x: yeuCauDichVu.getChiTietYeuCauDichVuList()){
                dieuKhoanList.add(dieuKhoanService.mapToDieuKhoanDTO(x.getDieuKhoan()));
            }
            hopDongDTO.setDieuKhoanList(dieuKhoanList);
            return ApiResponse.<HopDongDichVuKhachHangDTO>builder().code(200)
                    .result(hopDongDTO).build();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return ApiResponse.<HopDongDichVuKhachHangDTO>builder().code(400)
                    .message(e.getMessage()).build();
        }
    }

    @GetMapping("/dichvu/khachhangall/{id}")
    public ApiResponse<List<HopDongDichVuKhachHangDTO>> getAllHopDongDichVuKhachHang(@PathVariable("id") String id){
        if(!khachHangService.isExistsById(id)){
            return ApiResponse.<List<HopDongDichVuKhachHangDTO>>builder().code(400)
                    .message("Không tồn tại khách hàng").build();
        }
        try{
            List<YeuCauDichVu> yeuCauDichVuList = hopDongService.findAllDichVuByMaKhachHang(id);
            List<HopDongDichVuKhachHangDTO> hopDongDTOList = yeuCauDichVuList.stream()
            .map(yeuCauDichVu -> hopDongService.mapToHopDongDichVuKhachHangDTO(yeuCauDichVu)).toList();
            return ApiResponse.<List<HopDongDichVuKhachHangDTO>>builder().code(200)
                    .result(hopDongDTOList).build();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return ApiResponse.<List<HopDongDichVuKhachHangDTO>>builder().code(400)
                    .message(e.getMessage()).build();
        }
    }

    @PostMapping("/dichvu/checkyeucau")
    public ApiResponse<String> checkHopDongDichVu(@RequestBody YeuCauDichVuDTO yeuCauDichVuDTO){
        try{
            YeuCauDichVu yeuCauDichVu = hopDongService.mapToYeuCauDichVu(yeuCauDichVuDTO);
            yeuCauDichVu.setChuKy(yeuCauDichVu.getDichVu().getChuKy());
            if(yeuCauDichVu.getChuKy()!=0){
                if(hopDongService.isExistsByHopDongDichVu(yeuCauDichVu.getHopDong(), yeuCauDichVu.getDichVu())){
                    return ApiResponse.<String>builder().code(400)
                    .message("Có hợp đồng dịch vụ đang hoạt động").build();
                }
            }
            return ApiResponse.<String>builder().code(200)
            .result("Hợp lệ").build();
        }
        catch(Exception e){
            return ApiResponse.<String>builder().code(400)
            .message(e.getMessage()).build();
        }
    }
    

    @PostMapping("/dichvu")
    public ApiResponse<String> insertHopDongDichVu(@RequestBody YeuCauDichVuDTO yeuCauDichVuDTO) {
        try{
            YeuCauDichVu yeuCauDichVu = hopDongService.mapToYeuCauDichVu(yeuCauDichVuDTO);
            yeuCauDichVu.setNgayYeuCau(convertToUTC(yeuCauDichVu.getNgayYeuCau()));
            yeuCauDichVu.setThoiHan(convertToUTC(yeuCauDichVu.getThoiHan()));
            yeuCauDichVu.setTrangThai(false);
            yeuCauDichVu.setChuKy(yeuCauDichVu.getDichVu().getChuKy());
            if(yeuCauDichVu.getChuKy()!=0){
                if(hopDongService.isExistsByHopDongDichVu(yeuCauDichVu.getHopDong(), yeuCauDichVu.getDichVu())){
                    System.out.println(yeuCauDichVu.getHopDong().getIdHopDong());
                    System.out.println(yeuCauDichVu.getDichVu().getIdDichVu());
                    return ApiResponse.<String>builder().code(400)
                    .message("Có hợp đồng dịch vụ đang hoạt động").build();
                }
            }
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
                HoaDon hoaDon = hoaDonService.findById(yeuCauDichVuDTO.getSoHoaDon());
                if(hoaDon==null){
                    return ApiResponse.<String>builder().code(404)
                    .message("Không tìm thấy hóa đơn").build();
                }
                hoaDon.setYeuCauDichVu(yeuCauDichVu);
                hoaDon.setThoiGianDong(getNow());
                hoaDon = hoaDonService.save(hoaDon);
                if(hoaDonService.isExistsById(hoaDon.getSoHoaDon())){
                    return ApiResponse.<String>builder().code(200)
                    .result("Đăng ký thành công").build();
                }
                else{
                    return ApiResponse.<String>builder().code(200)
                    .result("Đăng ký thành công, thanh toán thất bại").build();
                }
            }
            else{
                return ApiResponse.<String>builder().code(200)
                    .result("Đăng ký thất bại").build();
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return ApiResponse.<String>builder().code(200)
                    .result(e.getMessage()).build();
        }
    }

    //Gia hạn
    @PutMapping("/dichvu/giahan/{id}/{soHoaDon}")
    public ApiResponse<String> giaHanHopDongDichVu(@PathVariable(value = "id") long id,
    @PathVariable(value="soHoaDon")long soHoaDon) {
        if (!hopDongService.isExistsDichVuById(id)) {
            return ApiResponse.<String>builder().code(400)
                    .message("Hợp đồng dịch vụ không tồn tại").build();
        }
        try{
            YeuCauDichVu yeuCauDichVu = hopDongService.findDichVuById(id);
            yeuCauDichVu.setThoiHan(plusDay(yeuCauDichVu.getThoiHan(), yeuCauDichVu.getChuKy()));
            yeuCauDichVu=hopDongService.saveDichVu(yeuCauDichVu);
            HoaDon hoaDon = hoaDonService.findById(soHoaDon);
            if(hoaDon==null){
                return ApiResponse.<String>builder().code(404)
                    .message("Không tìm thấy hóa đơn").build();
            }
            hoaDon.setYeuCauDichVu(yeuCauDichVu);
            hoaDon.setThoiGianDong(getNow());
            hoaDon = hoaDonService.save(hoaDon);
            if (hoaDonService.isExistsById(hoaDon.getSoHoaDon())) {
                return ApiResponse.<String>builder().code(200)
                    .result("Gia hạn thành công").build();
            } else {
                return ApiResponse.<String>builder().code(400)
                    .message("Gia hạn thất bại").build();
            }
        }
        catch(Exception e){
            return ApiResponse.<String>builder().code(400)
                    .message(e.getMessage()).build();
        }
    }

    //Hủy hợp đồng
    @PutMapping("dichvu/huy/{id}")
    public ApiResponse<String> huyHopDongDichVu(@PathVariable long id) {
        if (!hopDongService.isExistsDichVuById(id)) {
            return ApiResponse.<String>builder().code(400)
                    .message("Hợp đồng dịch vụ không tồn tại").build();
        }
        try{
            YeuCauDichVu yeuCauDichVu = hopDongService.findDichVuById(id);
            yeuCauDichVu.setTrangThai(true);
            yeuCauDichVu=hopDongService.saveDichVu(yeuCauDichVu);
            return ApiResponse.<String>builder().code(200)
                    .result("Hủy thành công, bạn vẫn có thể sử dụng dịch vụ trước thời hạn").build();
        }
        catch(Exception e){
            return ApiResponse.<String>builder().code(400)
                    .message(e.getMessage()).build();
        }
    }

}
