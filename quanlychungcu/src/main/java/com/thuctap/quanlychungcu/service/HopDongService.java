package com.thuctap.quanlychungcu.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thuctap.quanlychungcu.dto.BanQuanLyDTO;
import com.thuctap.quanlychungcu.dto.CanHoDTO;
import com.thuctap.quanlychungcu.dto.DichVuDTO;
import com.thuctap.quanlychungcu.dto.HopDongDTO;
import com.thuctap.quanlychungcu.dto.HopDongDichVuKhachHangDTO;
import com.thuctap.quanlychungcu.dto.HopDongKhachHangDTO;
import com.thuctap.quanlychungcu.dto.KhachHangDTO;
import com.thuctap.quanlychungcu.dto.YeuCauDichVuDTO;
import com.thuctap.quanlychungcu.model.BanQuanLy;
import com.thuctap.quanlychungcu.model.CTHopDong;
import com.thuctap.quanlychungcu.model.CTYeuCauDichVu;
import com.thuctap.quanlychungcu.model.CanHo;
import com.thuctap.quanlychungcu.model.DichVu;
import com.thuctap.quanlychungcu.model.HoaDon;
import com.thuctap.quanlychungcu.model.HopDong;
import com.thuctap.quanlychungcu.model.KhachHang;
import com.thuctap.quanlychungcu.model.YeuCauDichVu;
import com.thuctap.quanlychungcu.repository.CTHopDongRepository;
import com.thuctap.quanlychungcu.repository.CTYeuCauDichVuRepository;
import com.thuctap.quanlychungcu.repository.HoaDonRepository;
import com.thuctap.quanlychungcu.repository.HopDongRepository;
import com.thuctap.quanlychungcu.repository.YeuCauDichVuRepository;
import com.thuctap.quanlychungcu.utils.TimeTool;

@Service
public class HopDongService {

    @Autowired
    HopDongRepository hopDongRepository;

    @Autowired
    YeuCauDichVuRepository yeuCauDichVuRepository;

    @Autowired
    CTHopDongRepository chiTietHopDongRepository;

    @Autowired
    CTYeuCauDichVuRepository chiTietYeuCauDichVuRepository;

    @Autowired
    HoaDonRepository hoaDonRepository;

    @Autowired
    CanHoService canHoService;

    @Autowired
    DichVuService dichVuService;

    @Autowired
    KhachHangService khachHangService;

    @Autowired
    BanQuanLyService banQuanLyService;

    public HopDongDTO mapToHopDongDTO(HopDong hopDong){
        if(hopDong==null)return null;
        KhachHangDTO khachHangDTO = khachHangService.mapToKhachHangDTO(hopDong.getKhachHang());
        CanHoDTO canHoDTO = canHoService.mapToCanHoDTO(hopDong.getCanHo(),false);
        BanQuanLyDTO banQuanLyDTO = banQuanLyService.mapToBanQuanLyDTO(hopDong.getBanQuanLy());
        return HopDongDTO.builder()
            .idHopDong(hopDong.getIdHopDong())
            .ngayLap(hopDong.getNgayLap())
            .khachHang(khachHangDTO)
            .canHo(canHoDTO)
            .giaTri(hopDong.getGiaTri())
            .ngayBatDau(hopDong.getNgayBatDau())
            .thoiHan(hopDong.getThoiHan())
            .chuKy(hopDong.getChuKy())
            .trangThai(hopDong.getTrangThai())
            .chuKyDong(hopDong.getChuKyDong())
            .thoiGianDong(hopDong.getThoiGianDong())
            .yeuCau(hopDong.getYeuCau())
            .duyet(hopDong.getDuyet())
            .banQuanLy(banQuanLyDTO)
            .build();
    }
    public HopDongKhachHangDTO mapToHopDongKhachHangDTO(HopDong hopDong){
        if(hopDong==null)return null;
        KhachHangDTO khachHangDTO = khachHangService.mapToKhachHangDTO(hopDong.getKhachHang());
        CanHoDTO canHoDTO = canHoService.mapToCanHoDTO(hopDong.getCanHo(),false);
        BanQuanLyDTO banQuanLyDTO = banQuanLyService.mapToBanQuanLyDTO(hopDong.getBanQuanLy());
        HopDongKhachHangDTO hopDongKhachHangDTO = HopDongKhachHangDTO.builder()
        .idHopDong(hopDong.getIdHopDong())
            .ngayLap(hopDong.getNgayLap())
            .khachHang(khachHangDTO)
            .canHo(canHoDTO)
            .giaTri(hopDong.getGiaTri())
            .ngayBatDau(hopDong.getNgayBatDau())
            .thoiHan(hopDong.getThoiHan())
            .thoiGianDong(hopDong.getThoiGianDong())
            .chuKy(hopDong.getChuKy())
            .chuKyDong(hopDong.getChuKyDong())
            .trangThai(hopDong.getTrangThai())
            .yeuCau(hopDong.getYeuCau())
            .duyet(hopDong.getDuyet())
            .banQuanLy(banQuanLyDTO)
            .build();
        Timestamp thoiHanTruoc = TimeTool.minusDay(hopDong.getThoiHan(), 7);
        Timestamp thoiHanSau = TimeTool.plusDay(hopDong.getThoiHan(), 1);
        Timestamp now = TimeTool.getNow();
        if(now.after(thoiHanTruoc)&&now.before(thoiHanSau)){
            hopDongKhachHangDTO.setGiaHan(true);
        }
        else{
            hopDongKhachHangDTO.setGiaHan(false);
        }
        return hopDongKhachHangDTO;
    }

    

    public HopDong mapToHopDong(HopDongDTO hopDongDTO){
        if(hopDongDTO==null)return null;
        KhachHang khachHang = null;
        if(hopDongDTO.getKhachHang()!=null){
            khachHang=khachHangService.findById(hopDongDTO.getKhachHang().getMaKhachHang());
        } 
        
        CanHo canHo = null;
        if(hopDongDTO.getCanHo()!=null){
            canHo = canHoService.findById(hopDongDTO.getCanHo().getIdCanHo());
        }

        BanQuanLy banQuanLy = null;
        if(hopDongDTO.getBanQuanLy()!=null){
            banQuanLy = banQuanLyService.findById(hopDongDTO.getBanQuanLy().getMa());
        }

        return HopDong.builder()
            .idHopDong(hopDongDTO.getIdHopDong())
            .ngayLap(hopDongDTO.getNgayLap())
            .khachHang(khachHang)
            .canHo(canHo)
            .giaTri(hopDongDTO.getGiaTri())
            .ngayBatDau(hopDongDTO.getNgayBatDau())
            .thoiHan(hopDongDTO.getThoiHan())
            .chuKy(hopDongDTO.getChuKy())
            .trangThai(hopDongDTO.getTrangThai())
            .chuKyDong(hopDongDTO.getChuKyDong())
            .thoiGianDong(hopDongDTO.getThoiGianDong())
            .yeuCau(hopDongDTO.getYeuCau())
            .duyet(hopDongDTO.getDuyet())
            .banQuanLy(banQuanLy)
            .build();
    }

    public List<HopDong> findAll(){
        return hopDongRepository.findAll();
    }

    public List<HopDong> findAllByMaKhachHang(String id){
        List<HopDong> hopDongList= hopDongRepository.findAll();
        List<HopDong> hopDongKhachHangList = new ArrayList<>();
        for(HopDong x: hopDongList){
            if(x.getKhachHang().getMaKhachHang().equals(id)){
                hopDongKhachHangList.add(x);
            }
        }
        return hopDongKhachHangList;
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

    public CTHopDong saveCTHopDong(CTHopDong chiTietHopDong){
        return chiTietHopDongRepository.save(chiTietHopDong);
    }

    public Boolean checkActiveHopDong(long idCanHo){
        List<HopDong> hopDongList = findAll();
        for(HopDong x: hopDongList){
            if(!x.getTrangThai()&&x.getCanHo().getIdCanHo()==idCanHo){
                return true;
            }
        }
        return false;
    }

    //yêu cầu dịch vụ

    public HopDongDichVuKhachHangDTO mapToHopDongDichVuKhachHangDTO(YeuCauDichVu yeuCauDichVu){
        if(yeuCauDichVu==null)return null;
        HopDongDTO hopDongDTO = mapToHopDongDTO(yeuCauDichVu.getHopDong());
        DichVuDTO dichVuDTO = dichVuService.mapToDichVuDTO(yeuCauDichVu.getDichVu());
        BanQuanLyDTO banQuanLyDTO = banQuanLyService.mapToBanQuanLyDTO(yeuCauDichVu.getBanQuanLy());
        HopDongDichVuKhachHangDTO hopDongDichVuKhachHangDTO = HopDongDichVuKhachHangDTO.builder()
        .idYeuCauDichVu(yeuCauDichVu.getIdYeuCauDichVu())
        .hopDong(hopDongDTO)
        .dichVu(dichVuDTO)
        .giaTra(yeuCauDichVu.getGiaTra())
        .ngayYeuCau(yeuCauDichVu.getNgayYeuCau())
        .thoiHan(yeuCauDichVu.getThoiHan())
        .chuKy(yeuCauDichVu.getChuKy())
        .trangThai(yeuCauDichVu.getTrangThai())
        .yeuCau(yeuCauDichVu.getYeuCau())
        .duyet(yeuCauDichVu.getDuyet())
        .banQuanLy(banQuanLyDTO)
        .build();

        Timestamp thoiHanTruoc = TimeTool.minusDay(yeuCauDichVu.getThoiHan(), 7);
        Timestamp thoiHanSau = TimeTool.plusDay(yeuCauDichVu.getThoiHan(), 1);
        Timestamp now = TimeTool.getNow();
        if(yeuCauDichVu.getChuKy()==0){
            hopDongDichVuKhachHangDTO.setGiaHan(false);
        }
        else if(now.after(thoiHanTruoc)&&now.before(thoiHanSau)){
            hopDongDichVuKhachHangDTO.setGiaHan(true);
        }
        else{
            hopDongDichVuKhachHangDTO.setGiaHan(false);
        }
        return hopDongDichVuKhachHangDTO;
    }

    public YeuCauDichVuDTO mapToYeuCauDichVuDTO(YeuCauDichVu yeuCauDichVu){
        if(yeuCauDichVu==null)return null;
        HopDongDTO hopDongDTO = mapToHopDongDTO(yeuCauDichVu.getHopDong());
        DichVuDTO dichVuDTO = dichVuService.mapToDichVuDTO(yeuCauDichVu.getDichVu());
        BanQuanLyDTO banQuanLyDTO = banQuanLyService.mapToBanQuanLyDTO(yeuCauDichVu.getBanQuanLy());
        return YeuCauDichVuDTO.builder()
            .idYeuCauDichVu(yeuCauDichVu.getIdYeuCauDichVu())
            .hopDong(hopDongDTO)
            .dichVu(dichVuDTO)
            .giaTra(yeuCauDichVu.getGiaTra())
            .ngayYeuCau(yeuCauDichVu.getNgayYeuCau())
            .thoiHan(yeuCauDichVu.getThoiHan())
            .chuKy(yeuCauDichVu.getChuKy())
            .trangThai(yeuCauDichVu.getTrangThai())
            .duyet(yeuCauDichVu.getDuyet())
            .yeuCau(yeuCauDichVu.getYeuCau())
            .banQuanLy(banQuanLyDTO)
            .build();
    }

    public YeuCauDichVu mapToYeuCauDichVu(YeuCauDichVuDTO yeuCauDichVuDTO){
        if(yeuCauDichVuDTO==null)return null;
        HopDong hopDong = null;
        if(yeuCauDichVuDTO.getHopDong()!=null){
            hopDong=findById(yeuCauDichVuDTO.getHopDong().getIdHopDong());
        } 
        
        DichVu dichVu = null;
        if(yeuCauDichVuDTO.getDichVu()!=null){
            dichVu = dichVuService.findById(yeuCauDichVuDTO.getDichVu().getIdDichVu());
        }

        BanQuanLy banQuanLy = null;
        if(yeuCauDichVuDTO.getBanQuanLy()!=null){
            banQuanLy = banQuanLyService.findById(yeuCauDichVuDTO.getBanQuanLy().getMa());
        }

        return YeuCauDichVu.builder()
        .idYeuCauDichVu(yeuCauDichVuDTO.getIdYeuCauDichVu())
        .hopDong(hopDong)
        .dichVu(dichVu)
        .giaTra(yeuCauDichVuDTO.getGiaTra())
        .ngayYeuCau(yeuCauDichVuDTO.getNgayYeuCau())
        .thoiHan(yeuCauDichVuDTO.getThoiHan())
        .chuKy(yeuCauDichVuDTO.getChuKy())
        .trangThai(yeuCauDichVuDTO.getTrangThai())
        .duyet(yeuCauDichVuDTO.getDuyet())
        .yeuCau(yeuCauDichVuDTO.getYeuCau())
        .banQuanLy(banQuanLy)
        .build();
    }

    public List<YeuCauDichVu> findAllDichVu(){
        return yeuCauDichVuRepository.findAll();
    }

    
    public List<YeuCauDichVu> findAllDichVuByMaKhachHang(String id){
        List<YeuCauDichVu> yeuCauDichVuList= yeuCauDichVuRepository.findAll();
        List<YeuCauDichVu> yeuCauDichVuKhachHangList = new ArrayList<>();
        for(YeuCauDichVu x: yeuCauDichVuList){
            if(x.getHopDong().getKhachHang().getMaKhachHang().equals(id)){
                yeuCauDichVuKhachHangList.add(x);
            }
        }
        return yeuCauDichVuKhachHangList;
    }

    public YeuCauDichVu findDichVuById(long id){
        Optional<YeuCauDichVu> yeuCauDichVuOptional = yeuCauDichVuRepository.findById(id);
        YeuCauDichVu yeuCauDichVu = yeuCauDichVuOptional.orElse(null);
        if(yeuCauDichVu==null){
            return null;
        }
        return yeuCauDichVu;
    }

    public boolean isExistsDichVuById(long id){
        return yeuCauDichVuRepository.existsById(id);
    }
    public boolean isExistsByHopDongDichVu(HopDong hopDong, DichVu dichVu){
        List<YeuCauDichVu> list = findAllDichVu();
        for(YeuCauDichVu x: list){
            if(!x.getTrangThai()
            &&x.getHopDong().getCanHo().getIdCanHo()==hopDong.getCanHo().getIdCanHo()
            &&x.getDichVu().equals(dichVu)){
                return true;
            }
        }
        return false;
    }


    public YeuCauDichVu saveDichVu(YeuCauDichVu yeuCauDichVu){
        return yeuCauDichVuRepository.save(yeuCauDichVu);
    }

    public CTYeuCauDichVu saveCTYeuCauDichVu(CTYeuCauDichVu chiTietYeuCauDichVu){
        return chiTietYeuCauDichVuRepository.save(chiTietYeuCauDichVu);
    }

    public void huyDichVu(YeuCauDichVu yeuCauDichVu){
        Timestamp thoiHan = yeuCauDichVu.getThoiHan();
        Timestamp thoiHanTruoc = TimeTool.minusDay(thoiHan, yeuCauDichVu.getChuKy());
        Timestamp thoiHanTruoc7Ngay = TimeTool.minusDay(thoiHanTruoc,7);
        Timestamp now = TimeTool.getNow();
        if(now.compareTo(thoiHanTruoc)<=0){
            List<HoaDon> hoaDonList = hoaDonRepository.findAll();
            for(HoaDon x: hoaDonList){
                //Có hóa đơn chưa thanh toán của yêu cầu dịch vụ
                if(!x.getTrangThai()
                &&x.getYeuCauDichVu().getIdYeuCauDichVu()==yeuCauDichVu.getIdYeuCauDichVu()){
                    //Tự động thiết lập lại phát sinh hóa đơn trước kỳ hạn 7 ngày
                    if(x.getThoiGianTao().compareTo(thoiHanTruoc7Ngay)>=0){
                        hoaDonRepository.deleteById(x.getSoHoaDon());
                        yeuCauDichVu.setThoiHan(thoiHanTruoc);
                        break;
                    }
                }
            }
        }
        
        yeuCauDichVu.setTrangThai(true);
        saveDichVu(yeuCauDichVu);
    }

}
