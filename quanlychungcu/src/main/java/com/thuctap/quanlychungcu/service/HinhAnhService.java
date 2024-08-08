package com.thuctap.quanlychungcu.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.thuctap.quanlychungcu.model.CanHo;
import com.thuctap.quanlychungcu.model.HinhAnh;
import com.thuctap.quanlychungcu.model.TaiKhoan;
import com.thuctap.quanlychungcu.repository.CanHoRepository;
import com.thuctap.quanlychungcu.repository.HinhAnhRepository;

import jakarta.servlet.ServletContext;
import jakarta.transaction.Transactional;
@Service
public class HinhAnhService {
    @Autowired
    HinhAnhRepository hinhAnhRepository;

    @Autowired
    CanHoRepository canHoRepository;

    @Autowired
    ServletContext servletContext;
    
    private final String IMAGE_PATH = "D:\\SpringBoostWorkspace\\QuanLyHopDong\\QuanLyHopDongChungCu_BE\\quanlychungcu\\src\\main\\resources\\static\\images/";

    @Transactional
    public String downloadHinhAnh(MultipartFile file, TaiKhoan taiKhoan, CanHo canHo) throws IllegalStateException, IOException{
        
        String uuid = UUID.randomUUID().toString();
        String filePath = IMAGE_PATH+uuid+".jpg";
        
        try{
            file.transferTo(new File(filePath));
            HinhAnh hinhAnh = new HinhAnh();
            
            hinhAnh.setDuongDan(filePath);
            hinhAnh.setTaiKhoan(taiKhoan);;
            hinhAnh.setCanHo(canHo);
            hinhAnh=hinhAnhRepository.save(hinhAnh);
            return  "Lưu thành công: " +hinhAnh.getDuongDan();
        }
        catch(Exception e){
            return e.getMessage();
        }
        
    }

    @Transactional
    public String deleteAllHinhAnhCanHo(CanHo canHo){
        List<HinhAnh> hinhAnhList = canHo.getHinhAnhList();
        if(hinhAnhList==null||hinhAnhList.size()<1)return "Không có hình ảnh";
        try{
            for(HinhAnh hinhAnh: hinhAnhList){
                String filePath = hinhAnh.getDuongDan();
                File file = new File(filePath);
                if(!file.delete()){
                    System.out.println("Xóa ảnh thất bại, path: "+filePath);
                }
                hinhAnhRepository.delete(hinhAnh);
            }
            return "Xóa toàn bộ ảnh thành công";
        }
        catch(Exception e){
            return e.getMessage();
        }
    }

    @Transactional
    public String deleteAllAvatar(TaiKhoan taiKhoan){
        List<HinhAnh> hinhAnhList = taiKhoan.getHinhAnhList();
        if(hinhAnhList==null||hinhAnhList.size()<1)return "Không có hình ảnh";
        try{
            for(HinhAnh hinhAnh: hinhAnhList){
                String filePath = hinhAnh.getDuongDan();
                File file = new File(filePath);
                if(!file.delete()){
                    System.out.println("Xóa ảnh thất bại, path: "+filePath);
                }
                hinhAnhRepository.delete(hinhAnh);
            }
            return "Xóa toàn bộ ảnh thành công";
        }
        catch(Exception e){
            return e.getMessage();
        }
    }

    @Transactional
    public byte[]getHinhAnh(HinhAnh hinhAnh) throws IOException{
        if(hinhAnh==null)return null;
        String filePath = hinhAnh.getDuongDan();
        byte[] image= Files.readAllBytes(new File(filePath).toPath());
        return image;
    }

    public List<HinhAnh> findAll(){
        return hinhAnhRepository.findAll();
    }
}
