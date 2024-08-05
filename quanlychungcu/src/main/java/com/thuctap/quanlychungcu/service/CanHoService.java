package com.thuctap.quanlychungcu.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thuctap.quanlychungcu.dto.CanHoDTO;
import com.thuctap.quanlychungcu.dto.DieuKhoanDTO;
import com.thuctap.quanlychungcu.dto.LoaiPhongDTO;
import com.thuctap.quanlychungcu.model.CTDKCanHo;
import com.thuctap.quanlychungcu.model.CanHo;
import com.thuctap.quanlychungcu.model.DieuKhoan;
import com.thuctap.quanlychungcu.model.HinhAnh;
import com.thuctap.quanlychungcu.model.LoaiPhong;
import com.thuctap.quanlychungcu.repository.CanHoRepository;

import jakarta.transaction.Transactional;
@Service
public class CanHoService {

    @Autowired
    CanHoRepository canHoRepository;

    @Autowired
    LoaiPhongService loaiPhongService;

    @Autowired
    HinhAnhService hinhAnhService;

    @Autowired
    DieuKhoanService dieuKhoanService;

    public CanHoDTO mapToCanHoDTO(CanHo canHo, Boolean hasImage){
        if(canHo==null)return null;
        
        LoaiPhongDTO loaiPhongDTO = loaiPhongService.mapToLoaiPhongDTO(canHo.getLoaiPhong());
        List<DieuKhoanDTO> dieuKhoanDTOList = new ArrayList<>();
        List<CTDKCanHo> chiTietDieuKhoanCanHoList = canHo.getChiTietDieuKhoanCanHoList();
        if(chiTietDieuKhoanCanHoList!=null&&chiTietDieuKhoanCanHoList.size()>0){
            for(CTDKCanHo x: chiTietDieuKhoanCanHoList){
                DieuKhoanDTO dieuKhoanDTO = DieuKhoanDTO.builder()
                .ma(x.getDieuKhoan().getMa())
                .noiDung(x.getDieuKhoan().getNoiDung())
                .build();
                dieuKhoanDTOList.add(dieuKhoanDTO);
            }
        }
        CanHoDTO canHoDTO = CanHoDTO.builder()
            .idCanHo(canHo.getIdCanHo())
            .soPhong(canHo.getSoPhong())
            .lo(canHo.getLo())
            .tang(canHo.getTang())
            .loaiPhong(loaiPhongDTO)
            .dienTich(canHo.getDienTich())
            .tienNghi(canHo.getTienNghi())
            .moTa(canHo.getMoTa())
            .giaThue(canHo.getGiaThue())
            .trangThai(canHo.getTrangThai())
            .chuKy(canHo.getChuKy())
            .chuKyDong(canHo.getChuKyDong())
            .dieuKhoanList(dieuKhoanDTOList)
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
            .lo(canHoDTO.getLo())
            .loaiPhong(loaiPhong)
            .dienTich(canHoDTO.getDienTich())
            .tienNghi(canHoDTO.getTienNghi())
            .moTa(canHoDTO.getMoTa())
            .giaThue(canHoDTO.getGiaThue())
            .trangThai(canHoDTO.getTrangThai())
            .chuKy(canHoDTO.getChuKy())
            .chuKyDong(canHoDTO.getChuKyDong())
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

    @Transactional
    public Boolean updateDieuKhoan(CanHoDTO canHoDTO, CanHo canHo){
        if(canHoDTO.getDieuKhoanList()==null)return true;
        try{
            //Chuyển đổi danh sách điều khoản mới
            List<DieuKhoanDTO> dieuKhoanDTOList= canHoDTO.getDieuKhoanList();
            List<DieuKhoan> dieuKhoanList = dieuKhoanDTOList.stream()
            .map(dieuKhoan -> dieuKhoanService.mapToDieuKhoan(dieuKhoan)).toList();

            //Lấy lại danh sách điều khoản đã tồn tại
            List<CTDKCanHo> chiTietDieuKhoanCanHoList = dieuKhoanService.findCTDKCanHoById(canHo.getIdCanHo());
            List<DieuKhoan> dieuKhoanList2= new ArrayList<>();
            if(chiTietDieuKhoanCanHoList!=null&&chiTietDieuKhoanCanHoList.size()>0){
                //Lọc và xóa danh sách cũ nếu không có trong danh sách mới
                for(CTDKCanHo x: chiTietDieuKhoanCanHoList){
                    if(!dieuKhoanList.contains(x.getDieuKhoan())){
                        dieuKhoanService.deleteCTDKCanHoById(x.getIdCTDKCanHo());
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
                    CTDKCanHo chiTietDieuKhoanCanHo = CTDKCanHo.builder()
                    .canHo(canHo)
                    .dieuKhoan(y)
                    .build();
                    dieuKhoanService.saveCTDKCanHo(chiTietDieuKhoanCanHo);
                    
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
