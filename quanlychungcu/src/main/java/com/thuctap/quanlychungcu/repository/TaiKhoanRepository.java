package com.thuctap.quanlychungcu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.thuctap.quanlychungcu.model.TaiKhoan;

public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, String>{
    @Query(value = "SELECT DBO.GENERATE_MA()",nativeQuery = true)
    String generateMaMoi();
}
