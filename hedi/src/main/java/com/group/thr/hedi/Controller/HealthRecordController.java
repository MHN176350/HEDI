package com.group.thr.hedi.Controller;

import com.group.thr.hedi.DTO.Common.Response.ResponseFormat;
import com.group.thr.hedi.DTO.HealthRecord.Request.HealthRecordRequest;
import com.group.thr.hedi.DTO.HealthRecord.Response.HealthRecordResponse;
import com.group.thr.hedi.Enum.ResponseCode;
import com.group.thr.hedi.Service.Interface.IHealthRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/records")
@CrossOrigin(origins = "*", maxAge = 3600)
public class HealthRecordController {

    @Autowired
    private IHealthRecordService healthRecordService;

    @PostMapping("/user/{userId}")
    public ResponseFormat createRecord(@PathVariable Long userId, @RequestBody HealthRecordRequest request) {
        try {
            HealthRecordResponse record = healthRecordService.createRecord(userId, request);
            return new ResponseFormat(ResponseCode.SUCCESS, "Record created successfully", record);
        } catch (Exception e) {
            return new ResponseFormat(ResponseCode.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseFormat getUserRecords(@PathVariable Long userId) {
        try {
            List<HealthRecordResponse> records = healthRecordService.getRecordsByUserId(userId);
            return new ResponseFormat(ResponseCode.SUCCESS, records);
        } catch (Exception e) {
            return new ResponseFormat(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/user/{userId}/metric/{metricName}/latest")
    public ResponseFormat getLatestUserRecord(@PathVariable Long userId, @PathVariable String metricName) {
        try {
            HealthRecordResponse record = healthRecordService.getLatestRecord(userId, metricName);
            return new ResponseFormat(ResponseCode.SUCCESS, record);
        } catch (Exception e) {
            return new ResponseFormat(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseFormat deleteRecord(@PathVariable Long id) {
        try {
            healthRecordService.deleteRecord(id);
            return new ResponseFormat(ResponseCode.SUCCESS, "Record deleted successfully", null);
        } catch (Exception e) {
            return new ResponseFormat(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}