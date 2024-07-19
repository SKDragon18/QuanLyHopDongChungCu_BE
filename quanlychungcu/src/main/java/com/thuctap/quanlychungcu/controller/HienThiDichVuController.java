package com.thuctap.quanlychungcu.controller;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thuctap.quanlychungcu.dto.CanHoBayBanDTO;
import com.thuctap.quanlychungcu.dto.CanHoDTO;
import com.thuctap.quanlychungcu.dto.DieuKhoanDTO;
import com.thuctap.quanlychungcu.dto.LoaiPhongDTO;
import com.thuctap.quanlychungcu.model.CTDKCanHo;
import com.thuctap.quanlychungcu.model.CTDKDichVu;
import com.thuctap.quanlychungcu.model.CanHo;
import com.thuctap.quanlychungcu.model.HinhAnh;
import com.thuctap.quanlychungcu.service.BangGiaService;
import com.thuctap.quanlychungcu.service.CanHoService;
import com.thuctap.quanlychungcu.service.DieuKhoanService;
import com.thuctap.quanlychungcu.service.HinhAnhService;

@RestController
@RequestMapping("/hienthi")
public class HienThiDichVuController {
    @Autowired
    BangGiaService bangGiaService;

    @Autowired
    DieuKhoanService dieuKhoanService;

    @Autowired
    CanHoService canHoService;

    @Autowired
    HinhAnhService hinhAnhService;

    @GetMapping("/canhochothue")
    public ResponseEntity<?> getAllCanHo(){
        List<Map<?,?>> list = bangGiaService.getCanHoHienThiList();
        if (list.isEmpty()) {
            return new ResponseEntity<>("Không có dữ liệu", HttpStatus.BAD_REQUEST);
        }
        List<CanHoBayBanDTO> canHoBayBanDTOList = new ArrayList<>();
        for(Map<?,?> x:list){
            if(x.get("IDCANHO")!=null){
                CanHoBayBanDTO canHoBayBanDTO = CanHoBayBanDTO.builder()
                .idCanHo((int)x.get("IDCANHO"))
                .soPhong((String)x.get("SOPHONG"))
                .tang((int)x.get("TANG"))
                .lo((Character)x.get("LO"))
                .dienTich((BigDecimal)x.get("DIENTICH"))
                .tienNghi((String)x.get("TIENNGHI"))
                .moTa((String)x.get("MOTA"))
                .giaThue((BigDecimal)x.get("GIATHUE"))
                .chuKy((int)x.get("CHUKY"))
                .giaKhuyenMai((BigDecimal)x.get("GIAKHUYENMAI"))
                .trangThaiThue((int)x.get("TRANGTHAITHUE"))
                .build();
                //set loại phòng
                LoaiPhongDTO loaiPhongDTO = LoaiPhongDTO.builder()
                .tenLoaiPhong((String)x.get("TENLOAIPHONG"))
                .build();

                canHoBayBanDTO.setLoaiPhong(loaiPhongDTO);

                //set điều khoản
                List<CTDKCanHo> chiTietDieuKhoanDichVuList = dieuKhoanService.findCTDKCanHoById(canHoBayBanDTO.getIdCanHo());
                List<DieuKhoanDTO> dieuKhoanDTOList = new ArrayList<>();
                if(chiTietDieuKhoanDichVuList!=null&&chiTietDieuKhoanDichVuList.size()>0){
                    for(CTDKCanHo y: chiTietDieuKhoanDichVuList){
                        DieuKhoanDTO dieuKhoanDTO = DieuKhoanDTO.builder()
                        .ma(y.getDieuKhoan().getMa())
                        .noiDung(y.getDieuKhoan().getNoiDung())
                        .build();
                        dieuKhoanDTOList.add(dieuKhoanDTO);
                    }
                }
                canHoBayBanDTO.setDieuKhoanList(dieuKhoanDTOList);
                CanHo canHo = canHoService.findById(canHoBayBanDTO.getIdCanHo());
                //set hình ảnh
                try{
                    List<HinhAnh> hinhAnhList = canHo.getHinhAnhList();
                    List<byte[]> hinhAnhByteList = new ArrayList<>();
                    if(hinhAnhList!=null && hinhAnhList.size()>0){
                        for(HinhAnh hinhAnh: hinhAnhList){
                            hinhAnhByteList.add(hinhAnhService.getHinhAnh(hinhAnh));
                        }
                    }
                    canHoBayBanDTO.setHinhAnhList(hinhAnhByteList);
                }
                catch(IOException e){
                    System.out.println("Error: "+e.getMessage());
                }
                canHoBayBanDTOList.add(canHoBayBanDTO);
            }
        }
        return new ResponseEntity<>(canHoBayBanDTOList,HttpStatus.OK);
    }
}
