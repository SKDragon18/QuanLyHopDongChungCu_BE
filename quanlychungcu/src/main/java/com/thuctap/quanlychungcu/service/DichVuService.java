package com.thuctap.quanlychungcu.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thuctap.quanlychungcu.dto.DichVuDTO;
import com.thuctap.quanlychungcu.model.DichVu;
import com.thuctap.quanlychungcu.repository.DichVuRepository;
@Service
public class DichVuService {

    @Autowired
    DichVuRepository dichVuRepository;

    public DichVuDTO mapToDichVuDTO(DichVu dichVu){
        if(dichVu==null)return null;
        return DichVuDTO.builder()
            .idDichVu(dichVu.getIdDichVu())
            .tenDichVu(dichVu.getTenDichVu())
            .ghiChu(dichVu.getGhiChu())
            .giaHienTai(dichVu.getGiaHienTai())
            .build();
    }

    public DichVu mapToDichVu(DichVuDTO dichVuDTO){
        if(dichVuDTO==null)return null;
        return DichVu.builder()
            .idDichVu(dichVuDTO.getIdDichVu())
            .tenDichVu(dichVuDTO.getTenDichVu())
            .ghiChu(dichVuDTO.getGhiChu())
            .giaHienTai(dichVuDTO.getGiaHienTai())
            .build();
    }

    public List<DichVu> findAll(){
        return dichVuRepository.findAll();
    }

    public DichVu findById(int id){
        Optional<DichVu> dichVuOptional = dichVuRepository.findById(id);
        DichVu dichVu = dichVuOptional.orElse(null);
        if(dichVu==null){
            return null;
        }
        return dichVu;
    }

    public boolean isExistsById(int id){
        return dichVuRepository.existsById(id);
    }

    public DichVu save(DichVu dichVu){
        return dichVuRepository.save(dichVu);
    }

    public void deleteById(int id){
        dichVuRepository.deleteById(id);;
    }
}
