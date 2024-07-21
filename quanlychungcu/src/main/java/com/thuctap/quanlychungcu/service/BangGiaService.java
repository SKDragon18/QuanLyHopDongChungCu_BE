package com.thuctap.quanlychungcu.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.thuctap.quanlychungcu.dto.BanQuanLyDTO;
import com.thuctap.quanlychungcu.dto.BangGiaDTO;
import com.thuctap.quanlychungcu.dto.CanHoBangGiaDTO;
import com.thuctap.quanlychungcu.dto.CanHoBayBanDTO;
import com.thuctap.quanlychungcu.dto.CanHoDTO;
import com.thuctap.quanlychungcu.dto.DichVuBangGiaDTO;
import com.thuctap.quanlychungcu.dto.DichVuBayBanDTO;
import com.thuctap.quanlychungcu.dto.DieuKhoanDTO;
import com.thuctap.quanlychungcu.dto.LoaiPhongDTO;
import com.thuctap.quanlychungcu.model.BanQuanLy;
import com.thuctap.quanlychungcu.model.BangGia;
import com.thuctap.quanlychungcu.model.CTDKCanHo;
import com.thuctap.quanlychungcu.model.CTDKDichVu;
import com.thuctap.quanlychungcu.model.CanHo;
import com.thuctap.quanlychungcu.model.DichVu;
import com.thuctap.quanlychungcu.model.GiaCanHo;
import com.thuctap.quanlychungcu.model.GiaCanHoPK;
import com.thuctap.quanlychungcu.model.GiaDichVu;
import com.thuctap.quanlychungcu.model.GiaDichVuPK;
import com.thuctap.quanlychungcu.model.HinhAnh;
import com.thuctap.quanlychungcu.repository.BangGiaRepository;
import com.thuctap.quanlychungcu.repository.GiaCanHoRepository;
import com.thuctap.quanlychungcu.repository.GiaDichVuRepository;
@Service
public class BangGiaService {

    @Autowired
    BangGiaRepository bangGiaRepository;

    @Autowired
    BanQuanLyService banQuanLyService;

    @Autowired
    GiaCanHoRepository giaCanHoRepository;

    @Autowired
    GiaDichVuRepository giaDichVuRepository;

    @Autowired
    DieuKhoanService dieuKhoanService;

    @Autowired
    CanHoService canHoService;

    @Autowired
    HinhAnhService hinhAnhService;


    public BangGiaDTO mapToBangGiaDTO(BangGia bangGia){
        if(bangGia==null)return null;

        BanQuanLyDTO banQuanLyDTO = banQuanLyService.mapToBanQuanLyDTO(bangGia.getBanQuanLy());

        return BangGiaDTO.builder()
            .idBangGia(bangGia.getIdBangGia())
            .noiDung(bangGia.getNoiDung())
            .apDung(bangGia.getApDung())
            .thoiGianBatDau(bangGia.getThoiGianBatDau())
            .thoiGianKetThuc(bangGia.getThoiGianKetThuc())
            .banQuanLy(banQuanLyDTO)
            .build();
    }

    public BangGia mapToBangGia(BangGiaDTO bangGiaDTO){
        if(bangGiaDTO==null)return null;
        BanQuanLy banQuanLy = null;
        if(bangGiaDTO.getBanQuanLy()!=null){
            banQuanLy=banQuanLyService.findById(bangGiaDTO.getBanQuanLy().getMa());
        }
        return BangGia.builder()
            .idBangGia(bangGiaDTO.getIdBangGia())
            .noiDung(bangGiaDTO.getNoiDung())
            .apDung(bangGiaDTO.getApDung())
            .thoiGianBatDau(bangGiaDTO.getThoiGianBatDau())
            .thoiGianKetThuc(bangGiaDTO.getThoiGianKetThuc())
            .banQuanLy(banQuanLy)
            .build();
    }

    public List<BangGia> findAll(){
        return bangGiaRepository.findAll();
    }

    public BangGia findById(long id){
        Optional<BangGia> bangGiaOptional = bangGiaRepository.findById(id);
        BangGia bangGia = bangGiaOptional.orElse(null);
        if(bangGia==null){
            return null;
        }
        return bangGia;
    }

    public List<CanHoBangGiaDTO> findCanHoListById(long id){
        List<GiaCanHo> giaCanHoList = giaCanHoRepository.findAll();
        List<CanHoBangGiaDTO> result = new ArrayList<>();
        if(giaCanHoList!=null){
            for(GiaCanHo x: giaCanHoList){
                if(x.getBangGia().getIdBangGia()==id){
                    CanHo canHo = x.getCanHo();
                    CanHoBangGiaDTO canHoBangGiaDTO = new CanHoBangGiaDTO();
                    canHoBangGiaDTO.setIdCanHo(canHo.getIdCanHo());
                    canHoBangGiaDTO.setSoPhong(canHo.getSoPhong());
                    canHoBangGiaDTO.setLo(canHo.getLo());
                    canHoBangGiaDTO.setTang(canHo.getTang());
                    canHoBangGiaDTO.setGiaGoc(canHo.getGiaThue());
                    canHoBangGiaDTO.setGiaKhuyenMai(x.getGia());
                    result.add(canHoBangGiaDTO);
                }
            }
        }
        return result;
    }

    public List<DichVuBangGiaDTO> findDichVuListById(long id){
        List<GiaDichVu> giaDichVuList = giaDichVuRepository.findAll();
        List<DichVuBangGiaDTO> result = new ArrayList<>();
        if(giaDichVuList!=null){
            for(GiaDichVu x: giaDichVuList){
                if(x.getBangGia().getIdBangGia()==id){
                    DichVu dichVu = x.getDichVu();
                    DichVuBangGiaDTO dichVuBangGiaDTO = new DichVuBangGiaDTO();
                    dichVuBangGiaDTO.setIdDichVu(dichVu.getIdDichVu());
                    dichVuBangGiaDTO.setTenDichVu(dichVu.getTenDichVu());
                    dichVuBangGiaDTO.setGiaGoc(dichVu.getGiaHienTai());
                    dichVuBangGiaDTO.setGiaKhuyenMai(x.getGia());
                    result.add(dichVuBangGiaDTO);
                }
            }
        }
        return result;
    }

    public boolean isExistsById(long id){
        return bangGiaRepository.existsById(id);
    }

    public BangGia save(BangGia bangGia){
        return bangGiaRepository.save(bangGia);
    }

    public GiaCanHo saveGiaCanHo(GiaCanHo giaCanHo){
        return giaCanHoRepository.save(giaCanHo);
    }

    public GiaDichVu saveGiaDichVu(GiaDichVu giaDichVu){
        return giaDichVuRepository.save(giaDichVu);
    }

    public void deleteById(long id){
        bangGiaRepository.deleteById(id);;
    }

    public void deleteGiaCanHoById(GiaCanHoPK giaCanHoPK){
        giaCanHoRepository.deleteById(giaCanHoPK);
    }

    public void deleteGiaDichVuById(GiaDichVuPK giaDichVuPK){
        giaDichVuRepository.deleteById(giaDichVuPK);
    }

    //Hiển thị bày bán giá các sản phẩm dịch vụ/ căn hộ

    public List<CanHoBayBanDTO> getCanHoHienThiList(){
        List<Map<?,?>> list = bangGiaRepository.canHoHienThiList();
        if (list.isEmpty()) {
            return new ArrayList<>();
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
        return canHoBayBanDTOList;
    }

    public List<DichVuBayBanDTO> getDichVuHienThiList(){
        List<Map<?,?>> list = bangGiaRepository.dichVuHienThiList();
        if (list.isEmpty()) {
            return new ArrayList<>();
        }
        List<DichVuBayBanDTO> dichVuBayBanDTOList = new ArrayList<>();
        for(Map<?,?> x:list){
            if(x.get("IDDICHVU")!=null){
                DichVuBayBanDTO dichVuBayBanDTO = DichVuBayBanDTO.builder()
                .idDichVu((int)x.get("IDDICHVU"))
                .tenDichVu((String)x.get("TENDICHVU"))
                .ghiChu((String)x.get("GHICHU"))
                .chuKy((int)x.get("CHUKY"))
                .giaHienTai((BigDecimal)x.get("GIAHIENTAI"))
                .giaKhuyenMai((BigDecimal)x.get("GIAKHUYENMAI"))
                .build();

                //set điều khoản
                List<CTDKDichVu> chiTietDieuKhoanDichVuList = dieuKhoanService.findCTDKDichVuById(dichVuBayBanDTO.getIdDichVu());
                List<DieuKhoanDTO> dieuKhoanDTOList = new ArrayList<>();
                if(chiTietDieuKhoanDichVuList!=null&&chiTietDieuKhoanDichVuList.size()>0){
                    for(CTDKDichVu y: chiTietDieuKhoanDichVuList){
                        DieuKhoanDTO dieuKhoanDTO = DieuKhoanDTO.builder()
                        .ma(y.getDieuKhoan().getMa())
                        .noiDung(y.getDieuKhoan().getNoiDung())
                        .build();
                        dieuKhoanDTOList.add(dieuKhoanDTO);
                    }
                }
                dichVuBayBanDTO.setDieuKhoanList(dieuKhoanDTOList);
                dichVuBayBanDTOList.add(dichVuBayBanDTO);
            }
        }
        return dichVuBayBanDTOList;
    }
}
