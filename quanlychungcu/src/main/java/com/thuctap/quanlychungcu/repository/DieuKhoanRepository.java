package com.thuctap.quanlychungcu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.thuctap.quanlychungcu.model.DieuKhoan;

public interface DieuKhoanRepository extends JpaRepository<DieuKhoan,String>{
    @Query(value = "SELECT DBO.GENERATE_MADIEUKHOAN()",nativeQuery = true)
    String generateMaMoi();
}
