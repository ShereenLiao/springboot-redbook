package com.chuwa.redbook.task;

import com.chuwa.redbook.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
public class ReportCronTask {
    @Autowired
    private ReportService reportService;

    @Scheduled(cron = "0 0 0 ? * *")
    public void cron() {
        LocalDate date = LocalDate.now().minusDays(1);
        reportService.getTodayReport(date);
        log.info("Generate Report for "+date+".");
    }
}

