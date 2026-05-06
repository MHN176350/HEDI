package com.group.thr.hedi.Controller;

import com.group.thr.hedi.DTO.Common.Response.ResponseFormat;
import com.group.thr.hedi.DTO.HealthRecord.Request.HealthRecordRequest;
import com.group.thr.hedi.DTO.HealthRecord.Response.HealthRecordResponse;
import com.group.thr.hedi.Enum.ResponseCode;
import com.group.thr.hedi.Service.Interface.IHealthRecordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/health-records")
@CrossOrigin(origins = "*", maxAge = 3600)
public class HealthRecordController {

    @Autowired
    private IHealthRecordService healthRecordService;

    @PostMapping
    public ResponseFormat addHealthRecord(@Valid @RequestBody HealthRecordRequest request) {
        try {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            HealthRecordResponse response = healthRecordService.addHealthRecord(userEmail, request);
            String message = response.isAlertTriggered()
                    ? "Add data successfully - Alert: " + response.getAlertMessage()
                    : "Add data successfully";
            return new ResponseFormat(ResponseCode.SUCCESS, message, response);
        } catch (IllegalArgumentException e) {
            return new ResponseFormat(ResponseCode.BAD_REQUEST, e.getMessage(), null);
        } catch (Exception e) {
            return new ResponseFormat(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }
}