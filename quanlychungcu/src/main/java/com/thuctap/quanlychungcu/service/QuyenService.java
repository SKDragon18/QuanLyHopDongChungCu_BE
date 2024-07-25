package com.thuctap.quanlychungcu.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thuctap.quanlychungcu.dto.QuyenDTO;
import com.thuctap.quanlychungcu.model.Quyen;
import com.thuctap.quanlychungcu.repository.QuyenRepository;
@Service
public class QuyenService {

    @Autowired
    QuyenRepository quyenRepository;

    public QuyenDTO mapToQuyenDTO(Quyen quyen){
        if(quyen==null)return null;
        return QuyenDTO.builder()
            .idQuyen(quyen.getIdQuyen())
            .tenQuyen(quyen.getTenQuyen())
            .build();
    }

    public Quyen mapToQuyen(QuyenDTO quyenDTO){
        if(quyenDTO==null)return null;
        return Quyen.builder()
            .idQuyen(quyenDTO.getIdQuyen())
            .tenQuyen(quyenDTO.getTenQuyen())
            .build();
    }

    public List<Quyen> findAll(){
        return quyenRepository.findAll();
    }

    public Quyen findById(int id){
        Optional<Quyen> quyenOptional = quyenRepository.findById(id);
        Quyen quyen = quyenOptional.orElse(null);
        if(quyen==null){
            return null;
        }
        return quyen;
    }

    public Quyen findByTenQuyen(String id){
        Quyen quyen = quyenRepository.findByTenQuyen(id);
        if(quyen==null){
            return null;
        }
        return quyen;
    }

    public boolean isExistsById(int id){
        return quyenRepository.existsById(id);
    }

    public Quyen save(Quyen quyen){
        return quyenRepository.save(quyen);
    }

    public void deleteById(int id){
        quyenRepository.deleteById(id);;
    }
}
