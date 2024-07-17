package com.thuctap.quanlychungcu.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thuctap.quanlychungcu.service.BangGiaService;

@RestController
@RequestMapping("/hienthi")
public class HienThiDichVuController {
    @Autowired
    BangGiaService bangGiaService;

}
