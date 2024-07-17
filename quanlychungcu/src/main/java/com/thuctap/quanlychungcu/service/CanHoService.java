package com.thuctap.quanlychungcu.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thuctap.quanlychungcu.dto.CanHoDTO;
import com.thuctap.quanlychungcu.dto.LoaiPhongDTO;
import com.thuctap.quanlychungcu.model.CanHo;
import com.thuctap.quanlychungcu.model.HinhAnh;
import com.thuctap.quanlychungcu.model.LoaiPhong;
import com.thuctap.quanlychungcu.repository.CanHoRepository;
@Service
public class CanHoService {

    @Autowired
    CanHoRepository canHoRepository;

    @Autowired
    LoaiPhongService loaiPhongService;

    @Autowired
    HinhAnhService hinhAnhService;

    public CanHoDTO mapToCanHoDTO(CanHo canHo, Boolean hasImage){
        if(canHo==null)return null;
        
        LoaiPhongDTO loaiPhongDTO = loaiPhongService.mapToLoaiPhongDTO(canHo.getLoaiPhong());
        CanHoDTO canHoDTO = CanHoDTO.builder()
            .idCanHo(canHo.getIdCanHo())
            .soPhong(canHo.getSoPhong())
            .tang(canHo.getTang())
            .loaiPhong(loaiPhongDTO)
            .dienTich(canHo.getDienTich())
            .tienNghi(canHo.getTienNghi())
            .moTa(canHo.getMoTa())
            .giaThue(canHo.getGiaThue())
            .build();
        if(hasImage){
            try{
                List<HinhAnh> hinhAnhList = canHo.getHinhAnhList();
                List<byte[]> hinhAnhByteList = new ArrayList<>();
                if(hinhAnhList!=null && hinhAnhList.size()>0){
                    for(HinhAnh hinhAnh: hinhAnhList){
                        hinhAnhByteList.add(hinhAnhService.getHinhAnh(hinhAnh));
                    }
                }
                canHoDTO.setHinhAnhList(hinhAnhByteList);
            }
            catch(IOException e){
                System.out.println("Error: "+e.getMessage());
            }
        }
        return canHoDTO;
    }

    public CanHo mapToCanHo(CanHoDTO canHoDTO){
        if(canHoDTO==null)return null;
        LoaiPhong loaiPhong = null;
        if (canHoDTO.getLoaiPhong()!=null){
            loaiPhong = loaiPhongService.findById(canHoDTO.getLoaiPhong().getIdLoaiPhong());
        }
        
        return CanHo.builder()
            .idCanHo(canHoDTO.getIdCanHo())
            .soPhong(canHoDTO.getSoPhong())
            .tang(canHoDTO.getTang())
            .loaiPhong(loaiPhong)
            .dienTich(canHoDTO.getDienTich())
            .tienNghi(canHoDTO.getTienNghi())
            .moTa(canHoDTO.getMoTa())
            .giaThue(canHoDTO.getGiaThue())
            .build();
    }

    public List<CanHo> findAll(){
        return canHoRepository.findAll();
    }

    public CanHo findById(int id){
        Optional<CanHo> canHoOptional = canHoRepository.findById(id);
        CanHo canHo = canHoOptional.orElse(null);
        if(canHo==null){
            return null;
        }
        return canHo;
    }

    public boolean isExistsById(int id){
        return canHoRepository.existsById(id);
    }

    public CanHo save(CanHo canHo){
        return canHoRepository.save(canHo);
    }

    public void deleteById(int id){
        canHoRepository.deleteById(id);;
    }
}
