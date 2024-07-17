package com.thuctap.quanlychungcu.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thuctap.quanlychungcu.dto.BanQuanLyDTO;
import com.thuctap.quanlychungcu.dto.BangGiaDTO;
import com.thuctap.quanlychungcu.model.BanQuanLy;
import com.thuctap.quanlychungcu.model.BangGia;
import com.thuctap.quanlychungcu.repository.BangGiaRepository;
@Service
public class BangGiaService {

    @Autowired
    BangGiaRepository bangGiaRepository;

    @Autowired
    BanQuanLyService banQuanLyService;

    public BangGiaDTO mapToBangGiaDTO(BangGia bangGia){
        if(bangGia==null)return null;

        BanQuanLyDTO banQuanLyDTO = banQuanLyService.mapToBanQuanLyDTO(bangGia.getBanQuanLy());

        return BangGiaDTO.builder()
            .idBangGia(bangGia.getIdBangGia())
            .noiDung(bangGia.getNoiDung())
            .apDung(bangGia.getApDung())
            .thoiGianBatDau(bangGia.getThoiGianBatDau())
            .thoiGianKetThuc(bangGia.getThoiGianKetThuc())
            .banQuanLy(banQuanLyDTO)
            .build();
    }

    public BangGia mapToBangGia(BangGiaDTO bangGiaDTO){
        if(bangGiaDTO==null)return null;
        BanQuanLy banQuanLy = banQuanLyService.findById(bangGiaDTO.getBanQuanLy().getMa());
        return BangGia.builder()
            .idBangGia(bangGiaDTO.getIdBangGia())
            .noiDung(bangGiaDTO.getNoiDung())
            .apDung(bangGiaDTO.getApDung())
            .thoiGianBatDau(bangGiaDTO.getThoiGianBatDau())
            .thoiGianKetThuc(bangGiaDTO.getThoiGianKetThuc())
            .banQuanLy(banQuanLy)
            .build();
    }

    public List<BangGia> findAll(){
        return bangGiaRepository.findAll();
    }

    public BangGia findById(long id){
        Optional<BangGia> bangGiaOptional = bangGiaRepository.findById(id);
        BangGia bangGia = bangGiaOptional.orElse(null);
        if(bangGia==null){
            return null;
        }
        return bangGia;
    }

    public boolean isExistsById(long id){
        return bangGiaRepository.existsById(id);
    }

    public BangGia save(BangGia bangGia){
        return bangGiaRepository.save(bangGia);
    }

    public void deleteById(long id){
        bangGiaRepository.deleteById(id);;
    }
}
