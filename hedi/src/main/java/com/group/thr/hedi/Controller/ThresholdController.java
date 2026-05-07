// src/main/java/com/group/thr/hedi/Controller/ThresholdController.java
package com.group.thr.hedi.Controller;

import com.group.thr.hedi.DTO.Common.Response.ResponseFormat;
import com.group.thr.hedi.DTO.Threshold.Request.ThresholdBatchRequest;
import com.group.thr.hedi.DTO.Threshold.Request.ThresholdToggleRequest;
import com.group.thr.hedi.Enum.ResponseCode;
import com.group.thr.hedi.Service.Interface.IThresholdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/thresholds")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ThresholdController {

    @Autowired
    private IThresholdService thresholdService;

    @PostMapping("/user/{userId}/batch")
    public ResponseFormat createBatchThresholds(@PathVariable Long userId, @RequestBody List<ThresholdBatchRequest> requests) {
        try {
            thresholdService.saveBatchThresholds(userId, requests);
            return new ResponseFormat(ResponseCode.SUCCESS, "Thresholds saved successfully", null);
        } catch (Exception e) {
            return new ResponseFormat(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
   @GetMapping("/user/{userId}")
    public ResponseFormat getUserThresholds(@PathVariable Long userId) {
    try {
        return new ResponseFormat(ResponseCode.SUCCESS, thresholdService.getUserThresholds(userId));
    } catch (Exception e) {
        return new ResponseFormat(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
@PutMapping("/user/{userId}/settings")
    public ResponseFormat updateThresholdSettings(
            @PathVariable Long userId, 
            @RequestBody List<ThresholdToggleRequest> requests) {
        try {
            thresholdService.updateThresholdSettings(userId, requests);
            return new ResponseFormat(ResponseCode.SUCCESS, "Settings updated successfully", null);
        } catch (Exception e) {
            return new ResponseFormat(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}