package com.thuctap.quanlychungcu.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thuctap.quanlychungcu.dto.LoaiPhongDTO;
import com.thuctap.quanlychungcu.model.LoaiPhong;
import com.thuctap.quanlychungcu.repository.LoaiPhongRepository;
@Service
public class LoaiPhongService {

    @Autowired
    LoaiPhongRepository loaiPhongRepository;

    public LoaiPhongDTO mapToLoaiPhongDTO(LoaiPhong loaiPhong){
        if(loaiPhong==null)return null;
        return LoaiPhongDTO.builder()
            .idLoaiPhong(loaiPhong.getIdLoaiPhong())
            .tenLoaiPhong(loaiPhong.getTenLoaiPhong())
            .build();
    }

    public LoaiPhong mapToLoaiPhong(LoaiPhongDTO loaiPhongDTO){
        if(loaiPhongDTO==null)return null;
        return LoaiPhong.builder()
            .idLoaiPhong(loaiPhongDTO.getIdLoaiPhong())
            .tenLoaiPhong(loaiPhongDTO.getTenLoaiPhong())
            .build();
    }

    public List<LoaiPhong> findAll(){
        return loaiPhongRepository.findAll();
    }

    public LoaiPhong findById(int id){
        Optional<LoaiPhong> loaiPhongOptional = loaiPhongRepository.findById(id);
        LoaiPhong loaiPhong = loaiPhongOptional.orElse(null);
        if(loaiPhong==null){
            return null;
        }
        return loaiPhong;
    }

    public boolean isExistsById(int id){
        return loaiPhongRepository.existsById(id);
    }

    public LoaiPhong save(LoaiPhong loaiPhong){
        return loaiPhongRepository.save(loaiPhong);
    }

    public void deleteById(int id){
        loaiPhongRepository.deleteById(id);;
    }
}
