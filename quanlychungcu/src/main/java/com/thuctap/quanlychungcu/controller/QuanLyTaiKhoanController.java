package com.thuctap.quanlychungcu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thuctap.quanlychungcu.dto.ApiResponse;
import com.thuctap.quanlychungcu.dto.BanQuanLyDTO;
import com.thuctap.quanlychungcu.dto.DangKyNVDTO;
import com.thuctap.quanlychungcu.dto.QuyenDTO;
import com.thuctap.quanlychungcu.dto.TaiKhoanDTO;
import com.thuctap.quanlychungcu.model.BanQuanLy;
import com.thuctap.quanlychungcu.model.Quyen;
import com.thuctap.quanlychungcu.model.TaiKhoan;
import com.thuctap.quanlychungcu.service.BanQuanLyService;
import com.thuctap.quanlychungcu.service.QuyenService;
import com.thuctap.quanlychungcu.service.TaiKhoanService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/taikhoan")

public class QuanLyTaiKhoanController {
    @Autowired
    TaiKhoanService taiKhoanService;

    @Autowired
    BanQuanLyService banQuanLyService;

    @Autowired
    QuyenService quyenService;

    @GetMapping
    public ApiResponse<List<TaiKhoanDTO>> getAllTaiKhoan(){
        List<TaiKhoan> taiKhoanList = taiKhoanService.findAll();
        List<TaiKhoanDTO> taiKhoanDTOList = taiKhoanList.stream()
        .map(taiKhoan -> taiKhoanService.mapToTaiKhoanDTO(taiKhoan)).toList();
        return ApiResponse.<List<TaiKhoanDTO>>builder()
        .result(taiKhoanDTOList).code(200).build();
    }

    // @GetMapping("/{id}")
    // public ResponseEntity<?> getTaiKhoan(@PathVariable("id") String id){
    //     TaiKhoan taiKhoan = taiKhoanService.findById(id);
    //     if(taiKhoan==null){
    //         return new ResponseEntity<>("Không tìm thấy", HttpStatus.NOT_FOUND);
    //     }
    //     TaiKhoanDTO taiKhoanDTO = taiKhoanService.mapToTaiKhoanDTO(taiKhoan);
    //     return new ResponseEntity<>(taiKhoanDTO,HttpStatus.OK);
    // }

    @PostMapping
    public ApiResponse<String> insertTaiKhoan(@RequestBody DangKyNVDTO dangKyNVDTO){
        if(!dangKyNVDTO.getMatKhau().equals(dangKyNVDTO.getMatKhauNhapLai())){
            return ApiResponse.<String>builder().code(400)
                .message("Mật khẩu nhập lại không đúng").build();
        }
        try{
            TaiKhoan taiKhoan = taiKhoanService.registerNV(dangKyNVDTO);
            if(!taiKhoanService.isExistsById(taiKhoan.getTenDangNhap())){
                return ApiResponse.<String>builder().code(400)
                .message("Đăng ký thất bại").build();
            }
            else{
                return ApiResponse.<String>builder().code(200)
                .result("Đăng ký thành công").build();
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return ApiResponse.<String>builder().code(400)
                .message("Lỗi hệ thống: "+ e.getMessage()).build();
        }
    }

    @PutMapping("/updatekhoa/{id}")
    public ApiResponse<String> updateTrangThaiTaiKhoan(@PathVariable String id) {
        TaiKhoan taiKhoan = taiKhoanService.findById(id);
        if(taiKhoan==null){
            return ApiResponse.<String>builder().code(404)
            .message("Không tìm thấy tài khoản").build();
        }
        try{
            taiKhoan.setKhoa(!taiKhoan.getKhoa());
            taiKhoan=taiKhoanService.save(taiKhoan);
            return ApiResponse.<String>builder().code(200)
                .result("Cập nhật trạng thái thành công").build();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return ApiResponse.<String>builder().code(400)
            .message(e.getMessage()).build();
        }
        
    }

    @PutMapping("/updatequyen/{id}")
    public ApiResponse<String> updateQuyenTaiKhoan(@PathVariable String id) {
        TaiKhoan taiKhoan = taiKhoanService.findById(id);
        if(taiKhoan==null){
            return ApiResponse.<String>builder().code(404)
            .message("Không tìm thấy tài khoản").build();
        }
        try{
            Quyen quyen =null;
            if(taiKhoan.getQuyen().getTenQuyen().equals("admin")){
                quyen = quyenService.findByTenQuyen("quanly");
            }
            else if(taiKhoan.getQuyen().getTenQuyen().equals("quanly")){
                quyen = quyenService.findByTenQuyen("admin");
            }
            if(quyen==null){
                return ApiResponse.<String>builder().code(400)
                .message("Lỗi khác quyền chỉ định").build();
            }
            taiKhoan.setQuyen(quyen);
            taiKhoan=taiKhoanService.save(taiKhoan);
            return ApiResponse.<String>builder().code(200)
                .result("Cập nhật quyền mới thành công").build();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return ApiResponse.<String>builder().code(400)
            .message(e.getMessage()).build();
        }
        
    }

    

    @GetMapping("/quyen")
    public ApiResponse<List<QuyenDTO>> getAllQuyen(){
        List<Quyen> quyenList = quyenService.findAll();
        List<QuyenDTO> quyenDTOList = quyenList.stream()
        .map(quyen -> quyenService.mapToQuyenDTO(quyen)).toList();
        return ApiResponse.<List<QuyenDTO>>builder().code(200)
        .result(quyenDTOList).build();
    }

    // @GetMapping("/quyen/{id}")
    // public ResponseEntity<?> getQuyen(@PathVariable int id) {
    //     if (!quyenService.isExistsById(id)) {
    //         return new ResponseEntity<>("Quyền không tồn tại", HttpStatus.BAD_REQUEST);
    //     }
    //     Quyen quyen = quyenService.findById(id);
    //     QuyenDTO quyenDTO = quyenService.mapToQuyenDTO(quyen);
    //     return new ResponseEntity<>(quyenDTO, HttpStatus.OK);
    // }

    @PostMapping("/quyen")
    public ApiResponse<QuyenDTO> insertQuyen(@RequestBody QuyenDTO quyenDTO) {
        Quyen quyen = quyenService.mapToQuyen(quyenDTO);
        try{
            quyen = quyenService.save(quyen);
            if(quyenService.isExistsById(quyen.getIdQuyen())){
                QuyenDTO quyenDTO2 = quyenService.mapToQuyenDTO(quyen);
                return ApiResponse.<QuyenDTO>builder().code(200)
                .result(quyenDTO2).build();
            }
            else{
                return ApiResponse.<QuyenDTO>builder().code(400)
                .message("Thêm quyền thất bại").build();
            }
        }
        catch(Exception e){
            return ApiResponse.<QuyenDTO>builder().code(400)
                .message(e.getMessage()).build();
        }
        
    }
     
    @PutMapping("/quyen")
    public ResponseEntity<?> updateQuyen(@RequestBody QuyenDTO quyenDTO) {
        if (!quyenService.isExistsById(quyenDTO.getIdQuyen())) {
            return new ResponseEntity<>("Quyền không tồn tại", HttpStatus.BAD_REQUEST);
        }
        try{
            Quyen quyen = quyenService.mapToQuyen(quyenDTO);
            quyen = quyenService.save(quyen);
            QuyenDTO quyenDTO2 = quyenService.mapToQuyenDTO(quyen);
            return new ResponseEntity<>(quyenDTO2, HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/quyen/{id}")
    public ResponseEntity<?> deleteQuyen(@PathVariable int id) {
        if (!quyenService.isExistsById(id)) {
            return new ResponseEntity<>("Quyền không tồn tại", HttpStatus.BAD_REQUEST);
        }
        try{
            quyenService.deleteById(id);
            if (!quyenService.isExistsById(id)) {
                return new ResponseEntity<>("Xóa thành công", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Xóa thất bại", HttpStatus.BAD_REQUEST);
            }
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/banquanly/{id}")
    public ApiResponse<BanQuanLyDTO> getBanQuanLy(@PathVariable("id") String id){
        BanQuanLy banQuanLy = banQuanLyService.findById(id); 
        if(banQuanLy==null){
            return ApiResponse.<BanQuanLyDTO>builder().code(404)
            .message("Không tìm thấy thông tin").build();
        }
        BanQuanLyDTO banQuanLyDTO = banQuanLyService.mapToBanQuanLyDTO(banQuanLy);
        return ApiResponse.<BanQuanLyDTO>builder().code(200)
            .result(banQuanLyDTO).build();
    }

    @PutMapping("/banquanly")
    public ApiResponse<BanQuanLyDTO> updateBanQuanLy(@RequestBody BanQuanLyDTO banQuanLyDTO){
        if(!banQuanLyService.isExistsById(banQuanLyDTO.getMa())){
            return ApiResponse.<BanQuanLyDTO>builder().code(404)
            .message("Ban quản lý không tồn tại").build();
        }
        try{
            BanQuanLy banQuanLy = banQuanLyService.mapToBanQuanLy(banQuanLyDTO);
            banQuanLy=banQuanLyService.save(banQuanLy);
            BanQuanLyDTO banQuanLyDTO2 =banQuanLyService.mapToBanQuanLyDTO(banQuanLy);
            return ApiResponse.<BanQuanLyDTO>builder().code(200)
            .result(banQuanLyDTO2).build();
        }
        catch(Exception e){
            return ApiResponse.<BanQuanLyDTO>builder().code(400)
            .message(e.getMessage()).build();
        }

        

    }
}
