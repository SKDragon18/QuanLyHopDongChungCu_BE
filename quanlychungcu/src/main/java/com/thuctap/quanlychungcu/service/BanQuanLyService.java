package com.thuctap.quanlychungcu.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thuctap.quanlychungcu.dto.BanQuanLyDTO;
import com.thuctap.quanlychungcu.model.BanQuanLy;
import com.thuctap.quanlychungcu.repository.BanQuanLyRepository;
@Service
public class BanQuanLyService {

    @Autowired
    BanQuanLyRepository banQuanLyRepository;

    public BanQuanLyDTO mapToBanQuanLyDTO(BanQuanLy banQuanLy){
        if(banQuanLy==null)return null;
        return BanQuanLyDTO.builder()
            .ma(banQuanLy.getMa())
            .ho(banQuanLy.getHo())
            .ten(banQuanLy.getTen())
            .sdt(banQuanLy.getSdt())
            .email(banQuanLy.getEmail())
            .diaChi(banQuanLy.getDiaChi())
            .cmnd(banQuanLy.getCmnd())
            .nghi(banQuanLy.getNghi())
            .build();
    }

    public BanQuanLy mapToBanQuanLy(BanQuanLyDTO banQuanLyDTO){
        if(banQuanLyDTO==null)return null;
        return BanQuanLy.builder()
            .ma(banQuanLyDTO.getMa())
            .ho(banQuanLyDTO.getHo())
            .ten(banQuanLyDTO.getTen())
            .sdt(banQuanLyDTO.getSdt())
            .email(banQuanLyDTO.getEmail())
            .diaChi(banQuanLyDTO.getDiaChi())
            .cmnd(banQuanLyDTO.getCmnd())
            .nghi(banQuanLyDTO.getNghi())
            .build();
    }

    public List<BanQuanLy> findAll(){
        return banQuanLyRepository.findAll();
    }

    public BanQuanLy findById(String id){
        Optional<BanQuanLy> banQuanLyOptional = banQuanLyRepository.findById(id);
        BanQuanLy banQuanLy = banQuanLyOptional.orElse(null);
        if(banQuanLy==null){
            return null;
        }
        return banQuanLy;
    }

    public boolean isExistsById(String id){
        return banQuanLyRepository.existsById(id);
    }

    public BanQuanLy save(BanQuanLy banQuanLy){
        return banQuanLyRepository.save(banQuanLy);
    }

    public void deleteById(String id){
        banQuanLyRepository.deleteById(id);;
    }
}
