package com.thuctap.quanlychungcu.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thuctap.quanlychungcu.dto.YeuCauDichVuDTO;
import com.thuctap.quanlychungcu.dto.DichVuDTO;
import com.thuctap.quanlychungcu.dto.HopDongDTO;
import com.thuctap.quanlychungcu.model.YeuCauDichVu;
import com.thuctap.quanlychungcu.model.DichVu;
import com.thuctap.quanlychungcu.model.HopDong;
import com.thuctap.quanlychungcu.repository.YeuCauDichVuRepository;
@Service
public class YeuCauDichVuService {

    @Autowired
    YeuCauDichVuRepository yeuCauDichVuRepository;

    @Autowired
    DichVuService dichVuService;

    @Autowired
    HopDongService hopDongService;

    public YeuCauDichVuDTO mapToYeuCauDichVuDTO(YeuCauDichVu yeuCauDichVu){
        if(yeuCauDichVu==null)return null;

        DichVuDTO dichVuDTO = dichVuService.mapToDichVuDTO(yeuCauDichVu.getDichVu());
        HopDongDTO hopDongDTO = hopDongService.mapToHopDongDTO(yeuCauDichVu.getHopDong());
        return YeuCauDichVuDTO.builder()
            .idYeuCauDichVu(yeuCauDichVu.getIdYeuCauDichVu())
            .dichVu(dichVuDTO)
            .hopDong(hopDongDTO)
            .giaTra(yeuCauDichVu.getGiaTra())
            .ngayYeuCau(yeuCauDichVu.getNgayYeuCau())
            .thoiHan(yeuCauDichVu.getThoiHan())
            .chuKy(yeuCauDichVu.getChuKy())
            .trangThai(yeuCauDichVu.getTrangThai())
            .build();
    }

    public YeuCauDichVu mapToYeuCauDichVu(YeuCauDichVuDTO yeuCauDichVuDTO){
        if(yeuCauDichVuDTO==null)return null;

        DichVu dichVu = null;
        if(yeuCauDichVuDTO.getDichVu()!=null){
            dichVu = dichVuService.findById(yeuCauDichVuDTO.getDichVu().getIdDichVu());
        }
        
        HopDong hopDong = null;
        if(yeuCauDichVuDTO.getHopDong()!=null){
            hopDong = hopDongService.findById(yeuCauDichVuDTO.getHopDong().getIdHopDong());
        }
        
        return YeuCauDichVu.builder()
            .idYeuCauDichVu(yeuCauDichVuDTO.getIdYeuCauDichVu())
            .dichVu(dichVu)
            .hopDong(hopDong)
            .giaTra(yeuCauDichVuDTO.getGiaTra())
            .ngayYeuCau(yeuCauDichVuDTO.getNgayYeuCau())
            .thoiHan(yeuCauDichVuDTO.getThoiHan())
            .chuKy(yeuCauDichVuDTO.getChuKy())
            .trangThai(yeuCauDichVuDTO.getTrangThai())
            .build();
    }

    public List<YeuCauDichVu> findAll(){
        return yeuCauDichVuRepository.findAll();
    }

    public YeuCauDichVu findById(long id){
        Optional<YeuCauDichVu> yeuCauDichVuOptional = yeuCauDichVuRepository.findById(id);
        YeuCauDichVu yeuCauDichVu = yeuCauDichVuOptional.orElse(null);
        if(yeuCauDichVu==null){
            return null;
        }
        return yeuCauDichVu;
    }

    public boolean isExistsById(long id){
        return yeuCauDichVuRepository.existsById(id);
    }

    public YeuCauDichVu save(YeuCauDichVu yeuCauDichVu){
        return yeuCauDichVuRepository.save(yeuCauDichVu);
    }

    public void deleteById(long id){
        yeuCauDichVuRepository.deleteById(id);;
    }
}
