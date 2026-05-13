package com.group.thr.hedi.Controller;

import com.group.thr.hedi.DTO.Common.Response.ResponseFormat;
import com.group.thr.hedi.DTO.TrackedMetric.Request.TrackedMetricRequest;
import com.group.thr.hedi.DTO.TrackedMetric.Request.TrackedMetricToggleRequest;
import com.group.thr.hedi.Enum.ResponseCode;
import com.group.thr.hedi.Service.Interface.IUserTrackedMetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tracked-metrics")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserTrackedMetricController {

    @Autowired
    private IUserTrackedMetricService trackedMetricService;

    @PostMapping("/user/{userId}/batch")
    public ResponseFormat createBatchMetrics(@PathVariable Long userId, @RequestBody List<TrackedMetricRequest> requests) {
        try {
            trackedMetricService.saveBatchMetrics(userId, requests);
            return new ResponseFormat(ResponseCode.SUCCESS, "Metrics saved successfully", null);
        } catch (Exception e) {
            return new ResponseFormat(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseFormat getUserTrackedMetrics(@PathVariable Long userId) {
        try {
            return new ResponseFormat(ResponseCode.SUCCESS, trackedMetricService.getUserTrackedMetrics(userId));
        } catch (Exception e) {
            return new ResponseFormat(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/user/{userId}/settings")
    public ResponseFormat updateMetricSettings(
            @PathVariable Long userId, 
            @RequestBody List<TrackedMetricToggleRequest> requests) {
        try {
            trackedMetricService.updateMetricSettings(userId, requests);
            return new ResponseFormat(ResponseCode.SUCCESS, "Settings updated successfully", null);
        } catch (Exception e) {
            return new ResponseFormat(ResponseCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}