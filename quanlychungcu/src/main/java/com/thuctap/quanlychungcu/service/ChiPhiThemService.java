package com.thuctap.quanlychungcu.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thuctap.quanlychungcu.dto.CPKTXDTO;
import com.thuctap.quanlychungcu.dto.ChiPhiThemDTO;
import com.thuctap.quanlychungcu.dto.HopDongDTO;
import com.thuctap.quanlychungcu.model.CPKTX;
import com.thuctap.quanlychungcu.model.ChiPhiThem;
import com.thuctap.quanlychungcu.model.HopDong;
import com.thuctap.quanlychungcu.repository.ChiPhiThemRepository;
@Service
public class ChiPhiThemService {

    @Autowired
    ChiPhiThemRepository chiPhiThemRepository;

    @Autowired
    CPKTXService chiPhiKTXService;

    @Autowired
    HopDongService hopDongService;

    public ChiPhiThemDTO mapToChiPhiThemDTO(ChiPhiThem chiPhiThem){
        if(chiPhiThem==null)return null;

        CPKTXDTO chiPhiKTXDTO = chiPhiKTXService.mapToCPKTXDTO(chiPhiThem.getChiPhiKTX());
        HopDongDTO hopDongDTO = hopDongService.mapToHopDongDTO(chiPhiThem.getHopDong());
        return ChiPhiThemDTO.builder()
            .idCPThem(chiPhiThem.getIdCPThem())
            .chiPhiKTX(chiPhiKTXDTO)
            .hopDong(hopDongDTO)
            .giaTra(chiPhiThem.getGiaTra())
            .ngayYeuCau(chiPhiThem.getNgayYeuCau())
            .thoiHan(chiPhiThem.getThoiHan())
            .build();
    }

    public ChiPhiThem mapToChiPhiThem(ChiPhiThemDTO chiPhiThemDTO){
        if(chiPhiThemDTO==null)return null;

        CPKTX chiPhiKTX = null;
        if(chiPhiThemDTO.getChiPhiKTX()!=null){
            chiPhiKTX = chiPhiKTXService.findById(chiPhiThemDTO.getChiPhiKTX().getIdCPKTX());
        }
        
        HopDong hopDong = null;
        if(chiPhiThemDTO.getHopDong()!=null){
            hopDong = hopDongService.findById(chiPhiThemDTO.getHopDong().getIdHopDong());
        }
        
        return ChiPhiThem.builder()
            .idCPThem(chiPhiThemDTO.getIdCPThem())
            .chiPhiKTX(chiPhiKTX)
            .hopDong(hopDong)
            .giaTra(chiPhiThemDTO.getGiaTra())
            .ngayYeuCau(chiPhiThemDTO.getNgayYeuCau())
            .thoiHan(chiPhiThemDTO.getThoiHan())
            .build();
    }

    public List<ChiPhiThem> findAll(){
        return chiPhiThemRepository.findAll();
    }

    public ChiPhiThem findById(long id){
        Optional<ChiPhiThem> chiPhiThemOptional = chiPhiThemRepository.findById(id);
        ChiPhiThem chiPhiThem = chiPhiThemOptional.orElse(null);
        if(chiPhiThem==null){
            return null;
        }
        return chiPhiThem;
    }

    public boolean isExistsById(long id){
        return chiPhiThemRepository.existsById(id);
    }

    public ChiPhiThem save(ChiPhiThem chiPhiThem){
        return chiPhiThemRepository.save(chiPhiThem);
    }

    public void deleteById(long id){
        chiPhiThemRepository.deleteById(id);;
    }
}
