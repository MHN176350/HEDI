package com.group.thr.hedi.DTO.Admin.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminStatsResponse {
    private long totalUsers;
    private long totalRecords;
    private long totalMetrics;
}