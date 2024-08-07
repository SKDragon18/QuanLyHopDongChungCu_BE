package com.thuctap.quanlychungcu.scheduler;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.thuctap.quanlychungcu.dto.HopDongDichVuKhachHangDTO;
import com.thuctap.quanlychungcu.dto.HopDongKhachHangDTO;
import com.thuctap.quanlychungcu.model.HoaDon;
import com.thuctap.quanlychungcu.model.HopDong;
import com.thuctap.quanlychungcu.model.YeuCauDichVu;
import com.thuctap.quanlychungcu.service.HoaDonService;
import com.thuctap.quanlychungcu.service.HopDongService;

@Component
public class TaoHoaDonScheduler {
    

    @Autowired
    private HopDongService hopDongService;

    @Autowired
    private HoaDonService hoaDonService;

    public Timestamp plusSeconds(Timestamp time, int seconds){
        LocalDateTime localDateTime = time.toLocalDateTime();
        localDateTime = localDateTime.plusSeconds(seconds);
        return Timestamp.valueOf(localDateTime);
    }

    public Timestamp minusSeconds(Timestamp time, int seconds){
        LocalDateTime localDateTime = time.toLocalDateTime();
        localDateTime = localDateTime.minusSeconds(seconds);
        return Timestamp.valueOf(localDateTime);
    }

    public Timestamp minusDay(Timestamp time, int day){
        LocalDateTime localDateTime = time.toLocalDateTime();
        localDateTime = localDateTime.minusDays(day);
        return Timestamp.valueOf(localDateTime);
    }

    public Timestamp plusDay(Timestamp time, int day){
        LocalDateTime localDateTime = time.toLocalDateTime();
        localDateTime = localDateTime.plusDays(day);
        return Timestamp.valueOf(localDateTime);
    }


    public Timestamp getNow(){
        Date date = new Date();
        return new Timestamp(date.getTime());
    }

    @Scheduled(fixedDelay = 60000)
    public void createBill(){
        Timestamp now = getNow();
        List<HopDong> hopDongList = hopDongService.findAll();
        for(HopDong x: hopDongList){
            if(!x.getTrangThai()){//Hợp đồng đang hoạt động
                Timestamp thoiHan = x.getThoiHan();
                //Thời hạn tạo hợp đồng đã qua hoặc hiện là thời gian ấy
                while(now.compareTo(minusDay(x.getThoiGianDong(), 7))>=0){
                    Timestamp thoiGianDongMoi = plusDay(x.getThoiGianDong(), x.getChuKyDong());
                    if(thoiGianDongMoi.compareTo(thoiHan)>0){
                        break;//Vượt quá ngưỡng thời hạn hợp đồng
                    }
                    try{
                        x.setThoiGianDong(thoiGianDongMoi);
                        x = hopDongService.save(x);
                        HoaDon hoaDon = HoaDon.builder()
                        .hopDong(x)
                        .thoiGianTao(now)
                        .tongHoaDon(x.getGiaTri())
                        .trangThai(false)
                        .build();
                        hoaDon = hoaDonService.save(hoaDon);
                    }
                    catch(Exception e){
                        System.out.println("\nLỗi tạo hóa đơn hợp đồng mã hiệu: "+x.getIdHopDong());
                        System.out.println("Chi tiết: "+e.getMessage());
                        break;
                    }
                }
            }
        }
        List<YeuCauDichVu> hopDongDichVuList = hopDongService.findAllDichVu();
        for(YeuCauDichVu x: hopDongDichVuList){
            //Hợp đồng dịch vụ đang hoạt động và không phải loại yêu cầu ngay
            if(!x.getTrangThai()&&x.getChuKy()!=0){
                while(now.compareTo(minusDay(x.getThoiHan(), 7))>=0){
                    try{
                        x.setThoiHan(plusDay(x.getThoiHan(), x.getChuKy()));
                        x = hopDongService.saveDichVu(x);
                        HoaDon hoaDon = HoaDon.builder()
                        .yeuCauDichVu(x)
                        .thoiGianTao(now)
                        .tongHoaDon(x.getGiaTra())
                        .trangThai(false)
                        .build();
                        hoaDon = hoaDonService.save(hoaDon);
                    }
                    catch(Exception e){
                        System.out.println("\nLỗi tạo hóa đơn hợp đồng dịch vụ mã hiệu: "+x.getIdYeuCauDichVu());
                        System.out.println("Chi tiết: "+e.getMessage());
                        break;
                    }
                    
                }
            }
        }
    }
}
