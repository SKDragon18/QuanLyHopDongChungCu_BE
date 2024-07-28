package com.thuctap.quanlychungcu.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thuctap.quanlychungcu.dto.CanHoDTO;
import com.thuctap.quanlychungcu.dto.DichVuDTO;
import com.thuctap.quanlychungcu.dto.HopDongDTO;
import com.thuctap.quanlychungcu.dto.HopDongDichVuKhachHangDTO;
import com.thuctap.quanlychungcu.dto.HopDongKhachHangDTO;
import com.thuctap.quanlychungcu.dto.KhachHangDTO;
import com.thuctap.quanlychungcu.dto.YeuCauDichVuDTO;
import com.thuctap.quanlychungcu.model.CTHopDong;
import com.thuctap.quanlychungcu.model.CTYeuCauDichVu;
import com.thuctap.quanlychungcu.model.CanHo;
import com.thuctap.quanlychungcu.model.DichVu;
import com.thuctap.quanlychungcu.model.HopDong;
import com.thuctap.quanlychungcu.model.KhachHang;
import com.thuctap.quanlychungcu.model.YeuCauDichVu;
import com.thuctap.quanlychungcu.repository.CTHopDongRepository;
import com.thuctap.quanlychungcu.repository.CTYeuCauDichVuRepository;
import com.thuctap.quanlychungcu.repository.HopDongRepository;
import com.thuctap.quanlychungcu.repository.YeuCauDichVuRepository;

import jakarta.transaction.Transactional;
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
    CanHoService canHoService;

    @Autowired
    DichVuService dichVuService;

    @Autowired
    KhachHangService khachHangService;

    public Timestamp plusDay(Timestamp time, int day){
        LocalDateTime localDateTime = time.toLocalDateTime();
        localDateTime = localDateTime.plusDays(day);
        return Timestamp.valueOf(localDateTime);
    }

    public Timestamp minusDay(Timestamp time, int day){
        LocalDateTime localDateTime = time.toLocalDateTime();
        localDateTime = localDateTime.minusDays(day);
        return Timestamp.valueOf(localDateTime);
    }

    public Timestamp getNow(){
        Date date = new Date();
        return new Timestamp(date.getTime());
    }

    public HopDongDTO mapToHopDongDTO(HopDong hopDong){
        if(hopDong==null)return null;
        KhachHangDTO khachHangDTO = khachHangService.mapToKhachHangDTO(hopDong.getKhachHang());
        CanHoDTO canHoDTO = canHoService.mapToCanHoDTO(hopDong.getCanHo(),false);;
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
            .build();
    }
    public HopDongKhachHangDTO mapToHopDongKhachHangDTO(HopDong hopDong){
        if(hopDong==null)return null;
        KhachHangDTO khachHangDTO = khachHangService.mapToKhachHangDTO(hopDong.getKhachHang());
        CanHoDTO canHoDTO = canHoService.mapToCanHoDTO(hopDong.getCanHo(),false);;
        HopDongKhachHangDTO hopDongKhachHangDTO = HopDongKhachHangDTO.builder()
        .idHopDong(hopDong.getIdHopDong())
            .ngayLap(hopDong.getNgayLap())
            .khachHang(khachHangDTO)
            .canHo(canHoDTO)
            .giaTri(hopDong.getGiaTri())
            .ngayBatDau(hopDong.getNgayBatDau())
            .thoiHan(hopDong.getThoiHan())
            .chuKy(hopDong.getChuKy())
            .trangThai(hopDong.getTrangThai())
            .build();
        Timestamp thoiHanTruoc = minusDay(hopDong.getThoiHan(), 7);
        Timestamp thoiHanSau = plusDay(hopDong.getThoiHan(), 7);
        Timestamp now = getNow();
        if(now.after(thoiHanTruoc)&&now.before(thoiHanSau)){
            hopDongKhachHangDTO.setGiaHan(true);
        }
        else if(now.after(thoiHanSau)){
            try{
                hopDong.setTrangThai(true);//tự động hủy
                hopDong = save(hopDong);
                hopDongKhachHangDTO.setGiaHan(false);
            }
            catch(Exception e){
                throw new IllegalStateException("Hủy thất bại: " + e);
            }
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

    //yêu cầu dịch vụ

    public HopDongDichVuKhachHangDTO mapToHopDongDichVuKhachHangDTO(YeuCauDichVu yeuCauDichVu){
        if(yeuCauDichVu==null)return null;
        HopDongDTO hopDongDTO = mapToHopDongDTO(yeuCauDichVu.getHopDong());
        DichVuDTO dichVuDTO = dichVuService.mapToDichVuDTO(yeuCauDichVu.getDichVu());
        HopDongDichVuKhachHangDTO hopDongDichVuKhachHangDTO = HopDongDichVuKhachHangDTO.builder()
        .idYeuCauDichVu(yeuCauDichVu.getIdYeuCauDichVu())
        .hopDong(hopDongDTO)
        .dichVu(dichVuDTO)
        .giaTra(yeuCauDichVu.getGiaTra())
        .ngayYeuCau(yeuCauDichVu.getNgayYeuCau())
        .thoiHan(yeuCauDichVu.getThoiHan())
        .chuKy(yeuCauDichVu.getChuKy())
        .trangThai(yeuCauDichVu.getTrangThai())
        .build();

        Timestamp thoiHanTruoc = minusDay(yeuCauDichVu.getThoiHan(), 7);
        Timestamp thoiHanSau = plusDay(yeuCauDichVu.getThoiHan(), 7);
        Timestamp now = getNow();
        if(yeuCauDichVu.getChuKy()==0){
            hopDongDichVuKhachHangDTO.setGiaHan(false);
        }
        else if(now.after(thoiHanTruoc)&&now.before(thoiHanSau)){
            hopDongDichVuKhachHangDTO.setGiaHan(true);
        }
        else if(now.after(thoiHanSau)){
            try{
                yeuCauDichVu.setTrangThai(true);//tự động hủy
                yeuCauDichVu = saveDichVu(yeuCauDichVu);
                hopDongDichVuKhachHangDTO.setGiaHan(false);
            }
            catch(Exception e){
                throw new IllegalStateException("Hủy thất bại: " + e);
            }
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
        return YeuCauDichVuDTO.builder()
            .idYeuCauDichVu(yeuCauDichVu.getIdYeuCauDichVu())
            .hopDong(hopDongDTO)
            .dichVu(dichVuDTO)
            .giaTra(yeuCauDichVu.getGiaTra())
            .ngayYeuCau(yeuCauDichVu.getNgayYeuCau())
            .thoiHan(yeuCauDichVu.getThoiHan())
            .chuKy(yeuCauDichVu.getChuKy())
            .trangThai(yeuCauDichVu.getTrangThai())
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
        return YeuCauDichVu.builder()
        .idYeuCauDichVu(yeuCauDichVuDTO.getIdYeuCauDichVu())
        .hopDong(hopDong)
        .dichVu(dichVu)
        .giaTra(yeuCauDichVuDTO.getGiaTra())
        .ngayYeuCau(yeuCauDichVuDTO.getNgayYeuCau())
        .thoiHan(yeuCauDichVuDTO.getThoiHan())
        .chuKy(yeuCauDichVuDTO.getChuKy())
        .trangThai(yeuCauDichVuDTO.getTrangThai())
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
}
