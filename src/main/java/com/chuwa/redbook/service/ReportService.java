package com.chuwa.redbook.service;

import java.io.File;
import java.time.LocalDate;

public interface ReportService {
    File getTodayReport(LocalDate date);
    boolean ifReportExists(LocalDate date);
}
