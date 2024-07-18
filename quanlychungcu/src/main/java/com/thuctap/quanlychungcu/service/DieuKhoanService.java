package com.thuctap.quanlychungcu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thuctap.quanlychungcu.dto.DieuKhoanDTO;
import com.thuctap.quanlychungcu.model.CTDKCanHo;
import com.thuctap.quanlychungcu.model.CTDKDichVu;
import com.thuctap.quanlychungcu.model.CanHo;
import com.thuctap.quanlychungcu.model.DichVu;
import com.thuctap.quanlychungcu.model.DieuKhoan;
import com.thuctap.quanlychungcu.repository.CTDKCanHoRepository;
import com.thuctap.quanlychungcu.repository.CTDKDichVuRepository;
import com.thuctap.quanlychungcu.repository.DieuKhoanRepository;
@Service
public class DieuKhoanService {

    @Autowired
    DieuKhoanRepository dieuKhoanRepository;

    @Autowired
    CTDKCanHoRepository chiTietDieuKhoanCanHoRepository;

    @Autowired
    CTDKDichVuRepository chiTietDieuKhoanDichVuRepository;

    public DieuKhoanDTO mapToDieuKhoanDTO(DieuKhoan dieuKhoan){
        if(dieuKhoan==null)return null;
        return DieuKhoanDTO.builder()
            .ma(dieuKhoan.getMa())
            .noiDung(dieuKhoan.getNoiDung())
            .build();
    }

    public DieuKhoan mapToDieuKhoan(DieuKhoanDTO dieuKhoanDTO){
        if(dieuKhoanDTO==null)return null;
        return DieuKhoan.builder()
            .ma(dieuKhoanDTO.getMa())
            .noiDung(dieuKhoanDTO.getNoiDung())
            .build();
    }

    // public List<DieuKhoan> findAllCanHo(CanHo canHo){
    //     List<DieuKhoan> dieuKhoanList = new ArrayList<>();
    //     List<CTDKCanHo> chiTietDieuKhoanCanHo = canHo.getChiTietDieuKhoanCanHoList();
    //     for(CTDKCanHo x: chiTietDieuKhoanCanHo){
    //         dieuKhoanList.add(x.getDieuKhoan());
    //     }
    //     return dieuKhoanList;
    // }

    public List<CTDKCanHo> findAllCTDKCanHo(){
        return chiTietDieuKhoanCanHoRepository.findAll();
    }

    public List<CTDKDichVu> findAllCTDKDichVu(){
        return chiTietDieuKhoanDichVuRepository.findAll();
    }

    public List<CTDKCanHo> findCTDKCanHoByCanHo(CanHo canHo){
        List<CTDKCanHo> chiTietDieuKhoanCanHoList = findAllCTDKCanHo();
        List<CTDKCanHo> resultList = new ArrayList<>();
        for(CTDKCanHo x: chiTietDieuKhoanCanHoList){
            if(x.getCanHo().getIdCanHo()==canHo.getIdCanHo()){
                resultList.add(x);
            }
        }
        return resultList;
    }

    public List<CTDKDichVu> findCTDKDichVuByDichVu(DichVu dichVu){
        List<CTDKDichVu> chiTietDieuKhoanDichVuList = findAllCTDKDichVu();
        List<CTDKDichVu> resultList = new ArrayList<>();
        for(CTDKDichVu x: chiTietDieuKhoanDichVuList){
            if(x.getDichVu().getIdDichVu()==dichVu.getIdDichVu()){
                resultList.add(x);
            }
        }
        return resultList;
    }

    public String generateId(){
        List<DieuKhoan> dieuKhoanList = findAll();
        return "DK"+String.valueOf(dieuKhoanList.size()+1);
    }

    public List<DieuKhoan> findAll(){
        return dieuKhoanRepository.findAll();
    }

    public DieuKhoan findById(String id){
        Optional<DieuKhoan> dieuKhoanOptional = dieuKhoanRepository.findById(id);
        DieuKhoan dieuKhoan = dieuKhoanOptional.orElse(null);
        if(dieuKhoan==null){
            return null;
        }
        return dieuKhoan;
    }

    public boolean isExistsById(String id){
        return dieuKhoanRepository.existsById(id);
    }

    public DieuKhoan save(DieuKhoan dieuKhoan){
        if(dieuKhoan.getMa().contains("New")){
            dieuKhoan.setMa(generateId());
        }
        return dieuKhoanRepository.save(dieuKhoan);
    }

    public void deleteById(String id){
        dieuKhoanRepository.deleteById(id);;
    }

    public CTDKCanHo saveCTDKCanHo(CTDKCanHo chiTietDieuKhoanCanHo){
        return chiTietDieuKhoanCanHoRepository.save(chiTietDieuKhoanCanHo);
    }

    public void deleteCTDKCanHoById(long id){
        chiTietDieuKhoanCanHoRepository.deleteById(id);;
    }

    public CTDKDichVu saveCTDKDichVu(CTDKDichVu chiTietDieuKhoanDichVu){
        return chiTietDieuKhoanDichVuRepository.save(chiTietDieuKhoanDichVu);
    }

    public void deleteCTDKDichVuById(long id){
        chiTietDieuKhoanDichVuRepository.deleteById(id);;
    }
}
