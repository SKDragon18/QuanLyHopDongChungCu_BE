package com.thuctap.quanlychungcu.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thuctap.quanlychungcu.dto.KhachHangDTO;
import com.thuctap.quanlychungcu.model.KhachHang;
import com.thuctap.quanlychungcu.repository.KhachHangRepository;
@Service
public class KhachHangService {

    @Autowired
    KhachHangRepository khachHangRepository;

    public KhachHangDTO mapToKhachHangDTO(KhachHang khachHang){
        if(khachHang==null)return null;
        return KhachHangDTO.builder()
            .maKhachHang(khachHang.getMaKhachHang())
            .ho(khachHang.getHo())
            .ten(khachHang.getTen())
            .sdt(khachHang.getSdt())
            .email(khachHang.getEmail())
            .cmnd(khachHang.getCmnd())
            .build();
    }

    public KhachHang mapToKhachHang(KhachHangDTO khachHangDTO){
        if(khachHangDTO==null)return null;
        return KhachHang.builder()
            .maKhachHang(khachHangDTO.getMaKhachHang())
            .ho(khachHangDTO.getHo())
            .ten(khachHangDTO.getTen())
            .sdt(khachHangDTO.getSdt())
            .email(khachHangDTO.getEmail())
            .cmnd(khachHangDTO.getCmnd())
            .build();
    }

    public List<KhachHang> findAll(){
        return khachHangRepository.findAll();
    }

    public KhachHang findById(String id){
        Optional<KhachHang> khachHangOptional = khachHangRepository.findById(id);
        KhachHang khachHang = khachHangOptional.orElse(null);
        if(khachHang==null){
            return null;
        }
        return khachHang;
    }

    public boolean isExistsById(String id){
        return khachHangRepository.existsById(id);
    }

    public KhachHang save(KhachHang khachHang){
        return khachHangRepository.save(khachHang);
    }

    public void deleteById(String id){
        khachHangRepository.deleteById(id);;
    }
}
