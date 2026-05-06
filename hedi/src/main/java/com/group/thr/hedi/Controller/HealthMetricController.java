package com.group.thr.hedi.Controller;

import com.group.thr.hedi.DTO.Common.Response.ResponseFormat;
import com.group.thr.hedi.Enum.ResponseCode;
import com.group.thr.hedi.Service.Interface.IHealthMetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/metrics")
@CrossOrigin(origins = "*", maxAge = 3600)
public class HealthMetricController {

    @Autowired
    private IHealthMetricService healthMetricService;

    @GetMapping
    public ResponseFormat getAllMetrics() {
        return new ResponseFormat(ResponseCode.SUCCESS, healthMetricService.getAllActiveMetrics());
    }
}