package com.thuctap.quanlychungcu.scheduler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.thuctap.quanlychungcu.dto.HopDongDichVuKhachHangDTO;
import com.thuctap.quanlychungcu.dto.HopDongKhachHangDTO;
import com.thuctap.quanlychungcu.model.HopDong;
import com.thuctap.quanlychungcu.model.YeuCauDichVu;
import com.thuctap.quanlychungcu.service.HopDongService;

@Component
public class DeadlineNotificationScheduler {
    

    @Autowired
    private HopDongService hopDongService;

    // @Scheduled(fixedDelay = 60000)
    // public void checkDeadline(){
        // List<HopDong> hopDongList = hopDongService.findAll();
        // List<HopDongKhachHangDTO> hopDongDTOList = hopDongList.stream()
        // .map(hopDong -> hopDongService.mapToHopDongKhachHangDTO(hopDong)).toList();
        // List<YeuCauDichVu> hopDongDichVuList = hopDongService.findAllDichVu();
        // List<HopDongDichVuKhachHangDTO> hopDongDichVuKhachHangDTOList= hopDongDichVuList.stream()
        // .map(hopDongDichVu->hopDongService.mapToHopDongDichVuKhachHangDTO(hopDongDichVu)).toList();
        // this.template.convertAndSend("/topic/notifications/trangialong", "Bạn có hợp đồng");
    // }
}
