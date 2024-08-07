package com.thuctap.quanlychungcu.controller;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.nimbusds.jose.JOSEException;
import com.thuctap.quanlychungcu.dto.ApiResponse;
import com.thuctap.quanlychungcu.dto.DieuKhoanDTO;
import com.thuctap.quanlychungcu.dto.HoaDonDTO;
import com.thuctap.quanlychungcu.dto.HopDongDTO;
import com.thuctap.quanlychungcu.dto.HopDongDichVuKhachHangDTO;
import com.thuctap.quanlychungcu.dto.HopDongKhachHangDTO;
import com.thuctap.quanlychungcu.dto.ThanhToanDTO;
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
import com.thuctap.quanlychungcu.service.AuthenticateService;
import com.thuctap.quanlychungcu.service.BanQuanLyService;
import com.thuctap.quanlychungcu.service.DieuKhoanService;
import com.thuctap.quanlychungcu.service.HoaDonService;
import com.thuctap.quanlychungcu.service.HopDongService;
import com.thuctap.quanlychungcu.service.KhachHangService;
import com.thuctap.quanlychungcu.service.ThanhToanService;
import com.thuctap.quanlychungcu.service.YeuCauDichVuService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;



@RestController
@RequestMapping("/hopdong")
public class QuanLyHopDongController {
    @Autowired
    HopDongService hopDongService;

    @Autowired
    BanQuanLyService banQuanLyService;

    @Autowired
    YeuCauDichVuService yeuCauDichVuService;

    @Autowired
    HoaDonService hoaDonService;

    @Autowired
    KhachHangService khachHangService;

    @Autowired
    DieuKhoanService dieuKhoanService;

    @Autowired
    ThanhToanService thanhToanService;

    @Autowired
    AuthenticateService authenticateService;

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
            System.out.println("/////////////////");
            System.out.println(hopDongDTO.toString());
            System.out.println("/////////////////");
            System.out.println(hopDong.toString());
            System.out.println("/////////////////");
            hopDong.setNgayLap(convertToUTC(hopDong.getNgayLap()));
            hopDong.setNgayBatDau(convertToUTC(hopDong.getNgayBatDau()));
            hopDong.setThoiHan(convertToUTC(hopDong.getThoiHan()));
            hopDong.setThoiGianDong(convertToUTC(hopDong.getThoiGianDong()));
            hopDong.setTrangThai(true);
            hopDong.setChuKy(hopDong.getCanHo().getChuKy());
            hopDong.setChuKyDong(hopDong.getCanHo().getChuKyDong());
            hopDong.setYeuCau(0);
            hopDong.setDuyet(0);
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
                return ApiResponse.<String>builder().code(200)
                    .result("Đăng ký thành công").build();
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
    @PutMapping("/giahan/{id}")
    public ApiResponse<String> giaHanHopDong(@PathVariable long id) {
        if (!hopDongService.isExistsById(id)) {
            return ApiResponse.<String>builder().code(400)
                    .message("Hợp đồng không tồn tại").build();
        }
        try{
            HopDong hopDong = hopDongService.findById(id);
            hopDong.setYeuCau(1);
            hopDong.setDuyet(0);
            hopDong=hopDongService.save(hopDong);
            return ApiResponse.<String>builder().code(200)
            .result("Đã gửi yêu cầu gia hạn").build();
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
            hopDong.setYeuCau(2);
            hopDong.setDuyet(0);
            hopDong=hopDongService.save(hopDong);
            return ApiResponse.<String>builder().code(200)
                    .result("Đã gửi yêu cầu hủy").build();
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
            yeuCauDichVu.setTrangThai(true);
            yeuCauDichVu.setChuKy(yeuCauDichVu.getDichVu().getChuKy());
            yeuCauDichVu.setDuyet(0);
            yeuCauDichVu.setYeuCau(0);
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
                return ApiResponse.<String>builder().code(200)
                    .result("Đăng ký thành công").build();
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

    //Gia hạn hợp đồng
    // @PutMapping("/dichvu/giahan/{id}")
    // public ApiResponse<String> giaHanHopDongDichVu(@PathVariable long id) {
    //     if (!hopDongService.isExistsDichVuById(id)) {
    //         return ApiResponse.<String>builder().code(400)
    //                 .message("Hợp đồng dịch vụ không tồn tại").build();
    //     }
    //     try{
    //         YeuCauDichVu yeuCauDichVu = hopDongService.findDichVuById(id);
    //         yeuCauDichVu.setThoiHan(plusDay(yeuCauDichVu.getThoiHan(), yeuCauDichVu.getChuKy()));
    //         yeuCauDichVu=hopDongService.saveDichVu(yeuCauDichVu);
    //         return ApiResponse.<String>builder().code(200)
    //                 .result("Yêu cầu gia hạn thành công").build();
    //     }
    //     catch(Exception e){
    //         return ApiResponse.<String>builder().code(400)
    //                 .message(e.getMessage()).build();
    //     }
    // }

    //Hủy hợp đồng
    @PutMapping("/dichvu/huy/{id}")
    public ApiResponse<String> huyHopDongDichVu(@PathVariable long id) {
        if (!hopDongService.isExistsDichVuById(id)) {
            return ApiResponse.<String>builder().code(400)
                    .message("Hợp đồng dịch vụ không tồn tại").build();
        }
        try{
            YeuCauDichVu yeuCauDichVu = hopDongService.findDichVuById(id);
            yeuCauDichVu.setYeuCau(1);
            yeuCauDichVu.setDuyet(0);
            yeuCauDichVu=hopDongService.saveDichVu(yeuCauDichVu);
            return ApiResponse.<String>builder().code(200)
                    .result("Đã gửi yêu cầu hủy dịch vụ").build();
        }
        catch(Exception e){
            return ApiResponse.<String>builder().code(400)
                    .message(e.getMessage()).build();
        }
    }

    @PutMapping("/duyet/{id}/{duyet}")
    public ApiResponse<String> duyetHopDongCanHo(@PathVariable("id")Long id,
    @PathVariable("duyet")int duyet,@RequestHeader("Authorization") String authHeader){
        if(duyet==0){
            return ApiResponse.<String>builder().code(400)
            .message("Bạn không thể cung cấp tình trạng duyệt là 0").build();
        }
        HopDong hopDong = hopDongService.findById(id);
        if(hopDong==null){
            return ApiResponse.<String>builder().code(404)
            .message("Không tìm thấy hợp đồng").build();
        }
        
        try{
            //Lấy thông tin ban quản lý duyệt
            String token = authHeader.replace("Bearer", "");
            try {
                String tenDangNhap = authenticateService.getTenDangNhap(token);
                hopDong.setBanQuanLy(banQuanLyService.findById(tenDangNhap));
            } catch (ParseException e) {
                System.out.println(e.getMessage());
            } catch (JOSEException e) {
                System.out.println(e.getMessage());
            }
            hopDong.setDuyet(duyet);
            if(duyet==2){
                hopDongService.save(hopDong);
                return ApiResponse.<String>builder().code(200)
                .result("Từ chối thành công yêu cầu từ chủ hợp đồng").build();
            }
            if(hopDong.getYeuCau()==0){//Trường hợp đăng ký
                hopDong.setTrangThai(false);//Mở khóa hợp đồng
                HoaDon hoaDon = new HoaDon();
                hoaDon.setThoiGianTao(getNow());
                hoaDon.setHopDong(hopDong);
                hoaDon.setTongHoaDon(hopDong.getGiaTri());
                hoaDon.setTrangThai(false);
                hoaDon = hoaDonService.save(hoaDon);//Lưu hóa đơn lần đầu
                if(!hoaDonService.isExistsById(hoaDon.getSoHoaDon())){
                    hopDong.setTrangThai(true);
                    hopDong.setDuyet(0);
                    hopDongService.save(hopDong);
                    return ApiResponse.<String>builder().code(400)
                    .message("Lỗi: Không thể tạo hóa đơn").build();
                }
                hopDongService.save(hopDong);
                return ApiResponse.<String>builder().code(200)
                .result("Đồng ý thành công đăng ký hợp đồng mới").build();
            }
            else if(hopDong.getYeuCau()==1){//yêu cầu gia hạn
                hopDong.setThoiHan(plusDay(hopDong.getThoiHan(), hopDong.getChuKy()));
                hopDongService.save(hopDong);
                return ApiResponse.<String>builder().code(200)
                .result("Gia hạn thành công").build();
            }
            else if(hopDong.getYeuCau()==2){
                hopDong.setTrangThai(true);
                hopDongService.save(hopDong);
                return ApiResponse.<String>builder().code(200)
                .result("Hủy hợp đồng thành công").build();
            }
            return ApiResponse.<String>builder().code(400)
            .message("Lỗi không đúng theo định dạng quy định").build();
        }
        catch(Exception e){
            return ApiResponse.<String>builder().code(400)
            .message(e.getMessage()).build();
        }
    }

    @PutMapping("/dichvu/duyet/{id}/{duyet}")
    public ApiResponse<String> duyetHopDongDichVu(@PathVariable("id")Long id,
    @PathVariable("duyet")int duyet, @RequestHeader("Authorization") String authHeader){
        
        if(duyet==0){
            return ApiResponse.<String>builder().code(400)
            .message("Bạn không thể cung cấp tình trạng duyệt là 0").build();
        }
        YeuCauDichVu yeuCauDichVu = hopDongService.findDichVuById(id);
        if(yeuCauDichVu==null){
            return ApiResponse.<String>builder().code(404)
            .message("Không tìm thấy hợp đồng dịch vụ").build();
        }
        
        try{
            //Lấy thông tin ban quản lý duyệt
            String token = authHeader.replace("Bearer", "");
            try {
                String tenDangNhap = authenticateService.getTenDangNhap(token);
                yeuCauDichVu.setBanQuanLy(banQuanLyService.findById(tenDangNhap));
            } catch (ParseException e) {
                System.out.println(e.getMessage());
            } catch (JOSEException e) {
                System.out.println(e.getMessage());
            }
            yeuCauDichVu.setDuyet(duyet);
            
            if(duyet==2){
                hopDongService.saveDichVu(yeuCauDichVu);
                return ApiResponse.<String>builder().code(200)
                .result("Từ chối thành công yêu cầu từ chủ hợp đồng").build();
            }
            if(yeuCauDichVu.getYeuCau()==0){//Trường hợp đăng ký
                yeuCauDichVu.setTrangThai(false);//Mở khóa hợp đồng
                HoaDon hoaDon = new HoaDon();
                hoaDon.setYeuCauDichVu(yeuCauDichVu);
                hoaDon.setTongHoaDon(yeuCauDichVu.getGiaTra());
                hoaDon.setTrangThai(false);
                hoaDon.setThoiGianTao(getNow());
                hoaDon = hoaDonService.save(hoaDon);//Lưu hóa đơn lần đầu
                if(!hoaDonService.isExistsById(hoaDon.getSoHoaDon())){
                    yeuCauDichVu.setTrangThai(true);
                    yeuCauDichVu.setDuyet(0);
                    hopDongService.saveDichVu(yeuCauDichVu);
                    return ApiResponse.<String>builder().code(400)
                    .message("Lỗi: Không thể tạo hóa đơn").build();
                }
                hopDongService.saveDichVu(yeuCauDichVu);
                return ApiResponse.<String>builder().code(200)
                .result("Đồng ý thành công đăng ký hợp đồng dịch vụ mới").build();
            }
            else if(yeuCauDichVu.getYeuCau()==1){
                hopDongService.huyDichVu(yeuCauDichVu);
                return ApiResponse.<String>builder().code(200)
                .result("Hủy dịch vụ thành công").build();
            }
            return ApiResponse.<String>builder().code(400)
            .message("Lỗi không đúng theo định dạng quy định").build();
        }
        catch(Exception e){
            return ApiResponse.<String>builder().code(400)
            .message(e.getMessage()).build();
        }
    }

    @GetMapping("/notifications")
    public ApiResponse<List<String>> notifyBanQuanLy() {
        try{
            List<HopDong> hopDongList = hopDongService.findAll();
            List<HopDongKhachHangDTO> hopDongDTOList = hopDongList.stream()
            .map(hopDong -> hopDongService.mapToHopDongKhachHangDTO(hopDong)).toList();
            
            List<HoaDon> hoaDonList = hoaDonService.findAll();

            Timestamp now = getNow();

            List<String> notificationList= new ArrayList<>();
            if(hopDongDTOList.size()>0){
                for(HopDongKhachHangDTO x: hopDongDTOList){
                    if(x.getTrangThai())continue;
                    if(x.getGiaHan()){
                        String message = "HỢP ĐỒNG "+ String.valueOf(x.getIdHopDong())
                        +" của chủ hộ "+ x.getKhachHang().getTen() +"-"+x.getKhachHang().getMaKhachHang()
                        +" sắp TỚI HẠN";
                        notificationList.add(message);
                    }
                }
            }
            
            if(hoaDonList.size()>0){
                for(HoaDon x: hoaDonList){
                    if(now.compareTo(plusDay(x.getThoiGianTao(), 7))>=0){
                        String maKH = null;
                        HopDong hopDong =null;
                        
                        if(x.getHopDong()!=null){
                            hopDong = x.getHopDong();
                        }
                        else if(x.getYeuCauDichVu()!=null){
                            hopDong = x.getYeuCauDichVu().getHopDong();
                        }

                        if(hopDong!=null){
                            maKH = hopDong.getKhachHang().getTen()+"-"+hopDong.getKhachHang().getMaKhachHang();
                        }
                        else{
                            maKH="NaN";
                        }
                        String message = "HÓA ĐƠN số "+ String.valueOf(x.getSoHoaDon())
                        +" của chủ hộ "
                        + maKH
                        +" đã TỚI HẠN THANH TOÁN";
                        notificationList.add(message);
                    } 
                }
            }
            return ApiResponse.<List<String>>builder().code(200)
            .result(notificationList).build();
        }
        catch(Exception e){
            return ApiResponse.<List<String>>builder().code(400)
                .message(e.getMessage()).build();
        }    
    }

    @GetMapping("/notifications/{id}")
    public ApiResponse<List<String>> notify(@PathVariable String id) {
        if(!khachHangService.isExistsById(id)){
            return ApiResponse.<List<String>>builder().code(400)
                .message("Không tồn tại khách hàng").build();
        }
        try{
            List<HopDong> hopDongList = hopDongService.findAllByMaKhachHang(id);
            List<HopDongKhachHangDTO> hopDongDTOList = hopDongList.stream()
            .map(hopDong -> hopDongService.mapToHopDongKhachHangDTO(hopDong)).toList();
            List<HoaDon> hoaDonList = hoaDonService.findAll();

            List<String> notificationList= new ArrayList<>();
            if(hopDongDTOList.size()>0){
                for(HopDongKhachHangDTO x: hopDongDTOList){
                    if(x.getTrangThai())continue;
                    if(x.getGiaHan()){
                        String message = "HỢP ĐỒNG "+ String.valueOf(x.getIdHopDong())+" sắp HẾT HẠN";
                        notificationList.add(message);
                    }
                }
            }
            if(hoaDonList.size()>0){
                for(HoaDon x: hoaDonList){
                    if(x.getTrangThai())continue;//TH đã thanh toán
                    HopDong hopDong = null;
                    if(x.getHopDong()!=null){
                        hopDong = x.getHopDong();
                    }
                    if(x.getYeuCauDichVu()!=null){
                        hopDong = x.getYeuCauDichVu().getHopDong();
                    }
                    if(hopDong!=null){
                        if(hopDong.getKhachHang().getMaKhachHang().equals(id)){
                            String message = "HÓA ĐƠN "+ String.valueOf(x.getSoHoaDon())+" CHƯA THANH TOÁN";
                            notificationList.add(message);
                        }
                    }
                }
            }
            return ApiResponse.<List<String>>builder().code(200)
            .result(notificationList).build();
        }
        catch(Exception e){
            return ApiResponse.<List<String>>builder().code(400)
                .message(e.getMessage()).build();
        }    
    }

}
