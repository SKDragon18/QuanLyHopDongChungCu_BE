package com.thuctap.quanlychungcu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thuctap.quanlychungcu.dto.DichVuDTO;
import com.thuctap.quanlychungcu.dto.DichVuDTO;
import com.thuctap.quanlychungcu.dto.DieuKhoanDTO;
import com.thuctap.quanlychungcu.model.CTDKDichVu;
import com.thuctap.quanlychungcu.model.CTDKDichVu;
import com.thuctap.quanlychungcu.model.DichVu;
import com.thuctap.quanlychungcu.model.DichVu;
import com.thuctap.quanlychungcu.model.DieuKhoan;
import com.thuctap.quanlychungcu.repository.DichVuRepository;

import jakarta.transaction.Transactional;
@Service
public class DichVuService {

    @Autowired
    DichVuRepository dichVuRepository;

    @Autowired
    DieuKhoanService dieuKhoanService;

    public DichVuDTO mapToDichVuDTO(DichVu dichVu){
        if(dichVu==null)return null;
        List<DieuKhoanDTO> dieuKhoanDTOList = new ArrayList<>();
        List<CTDKDichVu> chiTietDieuKhoanDichVuList = dichVu.getChiTietDieuKhoanDichVuList();
        if(chiTietDieuKhoanDichVuList!=null&&chiTietDieuKhoanDichVuList.size()>0){
            for(CTDKDichVu x: chiTietDieuKhoanDichVuList){
                DieuKhoanDTO dieuKhoanDTO = DieuKhoanDTO.builder()
                .ma(x.getDieuKhoan().getMa())
                .noiDung(x.getDieuKhoan().getNoiDung())
                .build();
                dieuKhoanDTOList.add(dieuKhoanDTO);
            }
        }
        return DichVuDTO.builder()
            .idDichVu(dichVu.getIdDichVu())
            .tenDichVu(dichVu.getTenDichVu())
            .ghiChu(dichVu.getGhiChu())
            .giaHienTai(dichVu.getGiaHienTai())
            .chuKy(dichVu.getChuKy())
            .trangThai(dichVu.getTrangThai())
            .dieuKhoanList(dieuKhoanDTOList)
            .build();
    }

    public DichVu mapToDichVu(DichVuDTO dichVuDTO){
        if(dichVuDTO==null)return null;
        return DichVu.builder()
            .idDichVu(dichVuDTO.getIdDichVu())
            .tenDichVu(dichVuDTO.getTenDichVu())
            .ghiChu(dichVuDTO.getGhiChu())
            .giaHienTai(dichVuDTO.getGiaHienTai())
            .chuKy(dichVuDTO.getChuKy())
            .trangThai(dichVuDTO.getTrangThai())
            .build();
    }

    public List<DichVu> findAll(){
        return dichVuRepository.findAll();
    }

    public DichVu findById(int id){
        Optional<DichVu> dichVuOptional = dichVuRepository.findById(id);
        DichVu dichVu = dichVuOptional.orElse(null);
        if(dichVu==null){
            return null;
        }
        return dichVu;
    }

    public boolean isExistsById(int id){
        return dichVuRepository.existsById(id);
    }

    public DichVu save(DichVu dichVu){
        return dichVuRepository.save(dichVu);
    }

    public void deleteById(int id){
        dichVuRepository.deleteById(id);;
    }

    @Transactional
    public Boolean updateDieuKhoan(DichVuDTO dichVuDTO, DichVu dichVu){
        if(dichVuDTO.getDieuKhoanList()==null)return true;
        try{
            //Chuyển đổi danh sách điều khoản mới
            List<DieuKhoanDTO> dieuKhoanDTOList= dichVuDTO.getDieuKhoanList();
            List<DieuKhoan> dieuKhoanList = dieuKhoanDTOList.stream()
            .map(dieuKhoan -> dieuKhoanService.mapToDieuKhoan(dieuKhoan)).toList();

            //Lấy lại danh sách điều khoản đã tồn tại
            List<CTDKDichVu> chiTietDieuKhoanDichVuList = dieuKhoanService.findCTDKDichVuByDichVu(dichVu);
            List<DieuKhoan> dieuKhoanList2= new ArrayList<>();
            if(chiTietDieuKhoanDichVuList!=null&&chiTietDieuKhoanDichVuList.size()>0){
                //Lọc và xóa danh sách cũ nếu không có trong danh sách mới
                for(CTDKDichVu x: chiTietDieuKhoanDichVuList){
                    if(!dieuKhoanList.contains(x.getDieuKhoan())){
                        dieuKhoanService.deleteCTDKDichVuById(x.getIdCTDKDichVu());
                    }
                    else{
                        dieuKhoanList2.add(x.getDieuKhoan());
                        
                    }
                }
            }
            //Thêm danh sách mới nếu chưa có, nếu mã New thì tạo thêm
            for(DieuKhoan x: dieuKhoanList){
                if(!dieuKhoanList2.contains(x)){
                    DieuKhoan y = x;
                    if(y.getMa().contains("New")){
                        y = dieuKhoanService.save(y);
                        
                    }
                    CTDKDichVu chiTietDieuKhoanDichVu = CTDKDichVu.builder()
                    .dichVu(dichVu)
                    .dieuKhoan(y)
                    .build();
                    dieuKhoanService.saveCTDKDichVu(chiTietDieuKhoanDichVu);
                    
                }
            }
            return true;
        }
        catch(Exception e){
            System.out.println("Error: "+e.getMessage());
            return false;
        }
    }
}
