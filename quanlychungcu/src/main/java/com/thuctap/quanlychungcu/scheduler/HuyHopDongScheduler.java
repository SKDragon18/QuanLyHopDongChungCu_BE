package com.thuctap.quanlychungcu.scheduler;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.thuctap.quanlychungcu.model.HopDong;
import com.thuctap.quanlychungcu.service.HopDongService;
import com.thuctap.quanlychungcu.utils.TimeTool;

@Component
public class HuyHopDongScheduler {
    

    @Autowired
    private HopDongService hopDongService;

    @Scheduled(fixedDelay = 60000)
    public void cancel(){
        Timestamp now = TimeTool.getNow();
        List<HopDong> hopDongList = hopDongService.findAll();
        for(HopDong x: hopDongList){
            if(!x.getTrangThai()){
                Timestamp thoiHanSau= TimeTool.plusDay(x.getThoiHan(), 1);
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
