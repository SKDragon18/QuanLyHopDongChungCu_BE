package com.thuctap.quanlychungcu.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.thuctap.quanlychungcu.dto.BanQuanLyDTO;
import com.thuctap.quanlychungcu.dto.DangKyDTO;
import com.thuctap.quanlychungcu.dto.DangKyNVDTO;
import com.thuctap.quanlychungcu.dto.KhachHangDTO;
import com.thuctap.quanlychungcu.dto.QuyenDTO;
import com.thuctap.quanlychungcu.dto.TaiKhoanDTO;
import com.thuctap.quanlychungcu.model.BanQuanLy;
import com.thuctap.quanlychungcu.model.HinhAnh;
import com.thuctap.quanlychungcu.model.KhachHang;
import com.thuctap.quanlychungcu.model.Quyen;
import com.thuctap.quanlychungcu.model.TaiKhoan;
import com.thuctap.quanlychungcu.repository.TaiKhoanRepository;

import jakarta.transaction.Transactional;
@Service
public class TaiKhoanService {
    @Autowired
    TaiKhoanRepository taiKhoanRepository;

    @Autowired
    KhachHangService khachHangService;

    @Autowired
    BanQuanLyService banQuanLyService;

    @Autowired
    QuyenService quyenService;

    @Autowired
    HinhAnhService hinhAnhService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public TaiKhoanDTO mapToTaiKhoanDTO(TaiKhoan taiKhoan, Boolean hasImage){
        if(taiKhoan==null)return null;
        QuyenDTO quyenDTO = quyenService.mapToQuyenDTO(taiKhoan.getQuyen());
        
        KhachHangDTO khachHangDTO = khachHangService
        .mapToKhachHangDTO(khachHangService.findById(
            taiKhoan.getTenDangNhap()));
        
            BanQuanLyDTO banQuanLyDTO = banQuanLyService
        .mapToBanQuanLyDTO(banQuanLyService.findById(
            taiKhoan.getTenDangNhap()));
        
        TaiKhoanDTO taiKhoanDTO = TaiKhoanDTO.builder()
            .tenDangNhap(taiKhoan.getTenDangNhap())
            .quyen(quyenDTO)
            .khoa(taiKhoan.getKhoa())
            .banQuanLy(banQuanLyDTO)
            .khachHang(khachHangDTO)
            .build();
        if(hasImage){
            try{
                List<HinhAnh> hinhAnhList = taiKhoan.getHinhAnhList();
                List<byte[]> hinhAnhByteList = new ArrayList<>();
                if(hinhAnhList!=null && hinhAnhList.size()>0){
                    for(HinhAnh hinhAnh: hinhAnhList){
                        hinhAnhByteList.add(hinhAnhService.getHinhAnh(hinhAnh));
                    }
                }
                taiKhoanDTO.setHinhAnhList(hinhAnhByteList);
            }
            catch(IOException e){
                System.out.println("Error: "+e.getMessage());
            }
        }
        return taiKhoanDTO;
    }

    public TaiKhoan mapToTaiKhoan(TaiKhoanDTO taiKhoanDTO){
        if(taiKhoanDTO==null)return null;
        Quyen quyen = null;
        if(taiKhoanDTO.getQuyen()!=null){
            quyen = quyenService.findById(taiKhoanDTO.getQuyen().getIdQuyen());
        }
        
        return TaiKhoan.builder()
            .tenDangNhap(taiKhoanDTO.getTenDangNhap())
            .quyen(quyen)
            .khoa(taiKhoanDTO.getKhoa())
            .build();
    }

    public List<TaiKhoan> findAll(){
        return taiKhoanRepository.findAll();
    }

    public TaiKhoan findById(String id){
        Optional<TaiKhoan> taiKhoanOptional = taiKhoanRepository.findById(id);
        TaiKhoan taiKhoan = taiKhoanOptional.orElse(null);
        if(taiKhoan==null){
            return null;
        }
        return taiKhoan;
    }

    public boolean isExistsById(String tenDangNhap){
        return taiKhoanRepository.existsById(tenDangNhap);
    }

    public TaiKhoan save(TaiKhoan taiKhoan){
        return taiKhoanRepository.save(taiKhoan);
    }

    public TaiKhoan changePassword(String oldPass, String newPass, TaiKhoan taiKhoan){
        if(oldPass!=null&&!passwordEncoder.matches(oldPass, taiKhoan.getMatKhau())){
            return null;
        }
        taiKhoan.setMatKhau(passwordEncoder.encode(newPass));
        taiKhoan = save(taiKhoan);
        return taiKhoan;
    }

    public void deleteById(String id){
        taiKhoanRepository.deleteById(id);;
    }

    @Transactional
    public TaiKhoan register(DangKyDTO dangKyDTO){
        TaiKhoan taiKhoan = TaiKhoan.builder()
        .tenDangNhap(dangKyDTO.getTenDangNhap())
        .matKhau(passwordEncoder.encode(dangKyDTO.getMatKhau()))
        .quyen(quyenService.findById(3))
        .khoa(false).build();

        taiKhoan=save(taiKhoan);

        KhachHang khachHang = KhachHang.builder()
        .maKhachHang(dangKyDTO.getTenDangNhap())
        .ho(dangKyDTO.getHo())
        .ten(dangKyDTO.getTen())
        .ngaySinh(dangKyDTO.getNgaySinh())
        .email(dangKyDTO.getEmail())
        .sdt(dangKyDTO.getSdt())
        .build();
        khachHangService.save(khachHang);
        return taiKhoan;
    }

    public String generateMaMoi(){
        return taiKhoanRepository.generateMaMoi();
    }

    @Transactional
    public TaiKhoan registerNV(DangKyNVDTO dangKyDTO){
        String maMoi = generateMaMoi();
        TaiKhoan taiKhoan = TaiKhoan.builder()
        .tenDangNhap(maMoi)
        .matKhau(passwordEncoder.encode(dangKyDTO.getMatKhau()))
        .quyen(quyenService.findById(dangKyDTO.getIdQuyen()))
        .khoa(false).build();

        taiKhoan=save(taiKhoan);
        BanQuanLy banQuanLy = BanQuanLy.builder()
        .ma(maMoi)
        .ho(dangKyDTO.getHo())
        .ten(dangKyDTO.getTen())
        .email(dangKyDTO.getEmail())
        .sdt(dangKyDTO.getSdt())
        .diaChi(dangKyDTO.getDiaChi())
        .nghi(false)
        .build();
        
        banQuanLyService.save(banQuanLy);
        return taiKhoan;
    }
    
}
