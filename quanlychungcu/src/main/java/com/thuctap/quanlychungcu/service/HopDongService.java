package com.thuctap.quanlychungcu.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thuctap.quanlychungcu.dto.CanHoDTO;
import com.thuctap.quanlychungcu.dto.DichVuDTO;
import com.thuctap.quanlychungcu.dto.HopDongDTO;
import com.thuctap.quanlychungcu.dto.KhachHangDTO;
import com.thuctap.quanlychungcu.model.CanHo;
import com.thuctap.quanlychungcu.model.DichVu;
import com.thuctap.quanlychungcu.model.HopDong;
import com.thuctap.quanlychungcu.model.KhachHang;
import com.thuctap.quanlychungcu.repository.HopDongRepository;
@Service
public class HopDongService {

    @Autowired
    HopDongRepository hopDongRepository;

    @Autowired
    CanHoService canHoService;

    @Autowired
    DichVuService dichVuService;

    @Autowired
    KhachHangService khachHangService;

    public HopDongDTO mapToHopDongDTO(HopDong hopDong){
        if(hopDong==null)return null;
        KhachHangDTO khachHangDTO = khachHangService.mapToKhachHangDTO(hopDong.getKhachHang());
        CanHoDTO canHoDTO = canHoService.mapToCanHoDTO(hopDong.getCanHo(),false);;
        DichVuDTO dichVuDTO= dichVuService.mapToDichVuDTO(hopDong.getDichVu());
        return HopDongDTO.builder()
            .idHopDong(hopDong.getIdHopDong())
            .ngayLap(hopDong.getNgayLap())
            .khachHang(khachHangDTO)
            .canHo(canHoDTO)
            .dichVu(dichVuDTO)
            .giaTri(hopDong.getGiaTri())
            .thoiHan(hopDong.getThoiHan())
            .chuKy(hopDong.getChuKy())
            .build();
    }

    public HopDong mapToHopDong(HopDongDTO hopDongDTO){
        if(hopDongDTO==null)return null;
        KhachHang khachHang = khachHangService.findById(hopDongDTO.getKhachHang().getMaKhachHang());
        CanHo canHo = null;
        DichVu dichVu = null;
        if(hopDongDTO.getCanHo()!=null){
            canHo = canHoService.findById(hopDongDTO.getCanHo().getIdCanHo());
        }
        if(hopDongDTO.getDichVu()!=null){
            dichVu = dichVuService.findById(hopDongDTO.getDichVu().getIdDichVu());
        }
        return HopDong.builder()
            .idHopDong(hopDongDTO.getIdHopDong())
            .ngayLap(hopDongDTO.getNgayLap())
            .khachHang(khachHang)
            .canHo(canHo)
            .dichVu(dichVu)
            .giaTri(hopDongDTO.getGiaTri())
            .thoiHan(hopDongDTO.getThoiHan())
            .chuKy(hopDongDTO.getChuKy())
            .build();
    }

    public List<HopDong> findAll(){
        return hopDongRepository.findAll();
    }

    public HopDong findById(long id){
        Optional<HopDong> hopDongOptional = hopDongRepository.findById(id);
        HopDong hopDong = hopDongOptional.orElse(null);
        if(hopDong==null){
            return null;
        }
        return hopDong;
    }

    public boolean isExistsById(long id){
        return hopDongRepository.existsById(id);
    }

    public HopDong save(HopDong hopDong){
        return hopDongRepository.save(hopDong);
    }

    public void deleteById(long id){
        hopDongRepository.deleteById(id);;
    }
}
