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
public class HuyHopDongScheduler {
    

    @Autowired
    private HopDongService hopDongService;

    @Autowired
    private HoaDonService hoaDonService;

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

    @Scheduled(fixedDelay = 60000)
    public void cancel(){
        Timestamp now = getNow();
        List<HopDong> hopDongList = hopDongService.findAll();
        for(HopDong x: hopDongList){
            if(!x.getTrangThai()){
                Timestamp thoiHanSau= plusDay(x.getThoiHan(), 1);
                if(now.compareTo(thoiHanSau)>=0){
                    x.setTrangThai(true);
                    try{
                        x= hopDongService.save(x);
                    }
                    catch(Exception e){
                        System.out.println("\nLỗi tự động hủy hợp đồng: "+e.getMessage());
                    }
                }
            }
        }
        //Không tự động hủy yêu cầu dịch vụ vì có trường hợp hệ thống dừng hoạt động lâu
        //ngày hoạt động lại tạo đơn hàng loạt và cũng bị hủy hàng loạt mặc dù đã dùng dịch vụ.
        // List<HoaDon> hoaDonList = hoaDonService.findAll();
        // for(HoaDon x: hoaDonList){
        //     if(x.getYeuCauDichVu()!=null&&!x.getTrangThai()){
        //         Timestamp thoiHanSau= plusDay(x.getThoiGianTao(), 8);
        //         if(now.compareTo(thoiHanSau)>=0){
        //             YeuCauDichVu y = x.getYeuCauDichVu();
        //             y.setTrangThai(true);
        //             y.setThoiHan(minusDay(y.getThoiHan(), y.getChuKy()));//reset về ban đầu
        //             try{
        //                 y = hopDongService.saveDichVu(y);
        //             }
        //             catch(Exception e){
        //                 System.out.println("\nLỗi tự động hủy hợp đồng dịch vụ: "+e.getMessage());
        //             }
        //         }
                
        //     }
        // }
    }
}
