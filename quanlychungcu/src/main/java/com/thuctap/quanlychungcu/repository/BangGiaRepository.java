package com.thuctap.quanlychungcu.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.thuctap.quanlychungcu.model.BangGia;

public interface BangGiaRepository extends JpaRepository<BangGia,Long>{
    @Query(value = "EXECUTE SP_HIENTHICANHO",nativeQuery = true)
    List<Map<?,?>> canHoHienThiList();

    @Query(value = "EXECUTE SP_HIENTHIDICHVU",nativeQuery = true)
    List<Map<?,?>> dichVuHienThiList();
    
} 
