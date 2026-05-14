package com.group.thr.hedi.Controller;

import com.group.thr.hedi.DTO.Admin.Response.AdminStatsResponse;
import com.group.thr.hedi.DTO.Common.Response.ResponseFormat;
import com.group.thr.hedi.Enum.ResponseCode;
import com.group.thr.hedi.Repository.IAuthenticationRepository;
import com.group.thr.hedi.Repository.IHealthMetricRepository;
import com.group.thr.hedi.Repository.IHealthRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AdminController {

    @Autowired private IAuthenticationRepository userRepository;
    @Autowired private IHealthRecordRepository recordRepository;
    @Autowired private IHealthMetricRepository metricRepository;

    @GetMapping("/stats")
    public ResponseFormat getSystemStats() {
        try {
            long users = userRepository.count();
            long records = recordRepository.count();
            long metrics = metricRepository.count();
            return new ResponseFormat(ResponseCode.SUCCESS, new AdminStatsResponse(users, records, metrics));
        } catch (Exception e) {
            return new ResponseFormat(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}