package com.thuctap.quanlychungcu.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thuctap.quanlychungcu.dto.ChiPhiThemDTO;
import com.thuctap.quanlychungcu.dto.HoaDonDTO;
import com.thuctap.quanlychungcu.dto.HopDongDTO;
import com.thuctap.quanlychungcu.model.ChiPhiThem;
import com.thuctap.quanlychungcu.model.HoaDon;
import com.thuctap.quanlychungcu.model.HopDong;
import com.thuctap.quanlychungcu.repository.HoaDonRepository;
@Service
public class HoaDonService {

    @Autowired
    HoaDonRepository hoaDonRepository;

    @Autowired
    HopDongService hopDongService;

    @Autowired
    ChiPhiThemService chiPhiThemService;

    public HoaDonDTO mapToHoaDonDTO(HoaDon hoaDon){
        if(hoaDon==null)return null;
        HopDongDTO hopDongDTO = hopDongService.mapToHopDongDTO(hoaDon.getHopDong());;
        ChiPhiThemDTO chiPhiThemDTO = chiPhiThemService.mapToChiPhiThemDTO(hoaDon.getChiPhiThem());

        return HoaDonDTO.builder()
            .soHoaDon(hoaDon.getSoHoaDon())
            .thoiGianDong(hoaDon.getThoiGianDong())
            .tongHoaDon(hoaDon.getTongHoaDon())
            .hopDong(hopDongDTO)
            .chiPhiThem(chiPhiThemDTO)
            .build();
    }

    public HoaDon mapToHoaDon(HoaDonDTO hoaDonDTO){
        if(hoaDonDTO==null)return null;

        HopDong hopDong = null;
        if(hoaDonDTO.getHopDong()!=null){
            hopDong = hopDongService.findById(hoaDonDTO.getHopDong().getIdHopDong());
        }
        
        ChiPhiThem chiPhiThem = null;
        if(hoaDonDTO.getChiPhiThem()!=null){
            chiPhiThem = chiPhiThemService.findById(hoaDonDTO.getChiPhiThem().getIdCPThem());
        }
        
        return HoaDon.builder()
            .soHoaDon(hoaDonDTO.getSoHoaDon())
            .thoiGianDong(hoaDonDTO.getThoiGianDong())
            .tongHoaDon(hoaDonDTO.getTongHoaDon())
            .hopDong(hopDong)
            .chiPhiThem(chiPhiThem)
            .build();
    }

    public List<HoaDon> findAll(){
        return hoaDonRepository.findAll();
    }

    public HoaDon findById(long id){
        Optional<HoaDon> hoaDonOptional = hoaDonRepository.findById(id);
        HoaDon hoaDon = hoaDonOptional.orElse(null);
        if(hoaDon==null){
            return null;
        }
        return hoaDon;
    }

    public boolean isExistsById(long id){
        return hoaDonRepository.existsById(id);
    }

    public HoaDon save(HoaDon hoaDon){
        return hoaDonRepository.save(hoaDon);
    }

    public void deleteById(long id){
        hoaDonRepository.deleteById(id);;
    }
}
