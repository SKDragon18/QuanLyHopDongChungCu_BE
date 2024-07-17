package com.thuctap.quanlychungcu.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thuctap.quanlychungcu.dto.CPKTXDTO;
import com.thuctap.quanlychungcu.model.CPKTX;
import com.thuctap.quanlychungcu.repository.CPKTXRepository;
@Service
public class CPKTXService {

    @Autowired
    CPKTXRepository chiPhiKTXRepository;

    public CPKTXDTO mapToCPKTXDTO(CPKTX chiPhiKTX){
        if(chiPhiKTX==null)return null;
        return CPKTXDTO.builder()
            .idCPKTX(chiPhiKTX.getIdCPKTX())
            .tenDichVu(chiPhiKTX.getTenDichVu())
            .giaHienTai(chiPhiKTX.getGiaHienTai())
            .build();
    }

    public CPKTX mapToCPKTX(CPKTXDTO chiPhiKTXDTO){
        if(chiPhiKTXDTO==null)return null;
        return CPKTX.builder()
            .idCPKTX(chiPhiKTXDTO.getIdCPKTX())
            .tenDichVu(chiPhiKTXDTO.getTenDichVu())
            .giaHienTai(chiPhiKTXDTO.getGiaHienTai())
            .build();
    }

    public List<CPKTX> findAll(){
        return chiPhiKTXRepository.findAll();
    }

    public CPKTX findById(int id){
        Optional<CPKTX> chiPhiKTXOptional = chiPhiKTXRepository.findById(id);
        CPKTX chiPhiKTX = chiPhiKTXOptional.orElse(null);
        if(chiPhiKTX==null){
            return null;
        }
        return chiPhiKTX;
    }

    public boolean isExistsById(int id){
        return chiPhiKTXRepository.existsById(id);
    }

    public CPKTX save(CPKTX chiPhiKTX){
        return chiPhiKTXRepository.save(chiPhiKTX);
    }

    public void deleteById(int id){
        chiPhiKTXRepository.deleteById(id);;
    }
}
