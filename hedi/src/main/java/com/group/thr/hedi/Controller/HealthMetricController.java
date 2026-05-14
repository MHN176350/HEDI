package com.group.thr.hedi.Controller;

import com.group.thr.hedi.DTO.Common.Response.ResponseFormat;
import com.group.thr.hedi.DTO.Metric.Request.MetricRequest;
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
    @GetMapping("/all")
    public ResponseFormat getAllMetricsAdmin() {
        return new ResponseFormat(ResponseCode.SUCCESS, healthMetricService.getAllMetrics());
    }

    @PostMapping
    public ResponseFormat addMetric(@RequestBody MetricRequest request) {
        try {
            return new ResponseFormat(ResponseCode.SUCCESS, "Metric added", healthMetricService.addMetric(request));
        } catch (Exception e) {
            return new ResponseFormat(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/{id}/toggle")
    public ResponseFormat toggleMetric(@PathVariable Long id) {
        try {
            return new ResponseFormat(ResponseCode.SUCCESS, "Metric toggled", healthMetricService.toggleMetricStatus(id));
        } catch (Exception e) {
            return new ResponseFormat(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseFormat updateMetric(@PathVariable Long id, @RequestBody MetricRequest request) {
        try {
            return new ResponseFormat(ResponseCode.SUCCESS, "Metric updated", healthMetricService.updateMetric(id, request));
        } catch (Exception e) {
            return new ResponseFormat(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}