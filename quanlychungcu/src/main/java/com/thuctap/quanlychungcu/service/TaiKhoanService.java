package com.thuctap.quanlychungcu.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thuctap.quanlychungcu.dto.QuyenDTO;
import com.thuctap.quanlychungcu.dto.TaiKhoanDTO;
import com.thuctap.quanlychungcu.model.Quyen;
import com.thuctap.quanlychungcu.model.TaiKhoan;
import com.thuctap.quanlychungcu.repository.TaiKhoanRepository;
@Service
public class TaiKhoanService {
    @Autowired
    TaiKhoanRepository taiKhoanRepository;

    @Autowired
    QuyenService quyenService;

    public TaiKhoanDTO mapToTaiKhoanDTO(TaiKhoan taiKhoan){
        if(taiKhoan==null)return null;
        QuyenDTO quyenDTO = quyenService.mapToQuyenDTO(taiKhoan.getQuyen());
        return TaiKhoanDTO.builder()
            .tenDangNhap(taiKhoan.getTenDangNhap())
            .matKhau(taiKhoan.getMatKhau())
            .quyen(quyenDTO)
            .khoa(taiKhoan.getKhoa())
            .build();
    }

    public TaiKhoan mapToTaiKhoan(TaiKhoanDTO taiKhoanDTO){
        if(taiKhoanDTO==null)return null;
        Quyen quyen = null;
        if(taiKhoanDTO.getQuyen()!=null){
            quyen = quyenService.findById(taiKhoanDTO.getQuyen().getIdQuyen());
        }
        
        return TaiKhoan.builder()
            .tenDangNhap(taiKhoanDTO.getTenDangNhap())
            .matKhau(taiKhoanDTO.getMatKhau())
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

    public void deleteById(String id){
        taiKhoanRepository.deleteById(id);;
    }

    
}
