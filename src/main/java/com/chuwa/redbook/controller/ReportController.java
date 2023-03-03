package com.chuwa.redbook.controller;

import com.chuwa.redbook.exception.BlogAPIException;
import com.chuwa.redbook.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDate;
import java.util.Date;

/**
 * a. GET /api/v1/reports?reportDate=mmddyyyy
 *      i.  返回CSV
 * b. GET /api/v1/reportAvailability?reportDate=mmddyyyy
 *      i.  返回True or false
 * */

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {
    @Autowired
    private ReportService reportService;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyy");

    @GetMapping("")
    public ResponseEntity<File> getTodayReport(@RequestParam("reportDate")String date){
        try {
            Date d = dateFormat.parse(date);
            LocalDate localDate = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            File report = reportService.getTodayReport(localDate);
            if(!reportService.ifReportExists(localDate)){
                throw new BlogAPIException(HttpStatus.NOT_FOUND, "The report is not available.");
            }
            return new ResponseEntity<>(report, HttpStatus.FOUND);
        } catch (ParseException e) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Can't parse the date time.");
        }
    }

    @GetMapping("/reportAvailability")
    public ResponseEntity<Boolean> existsTodayReport(@RequestParam("reportDate")String date){
        try {
            Date d = dateFormat.parse(date);
            LocalDate localDate = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            return new ResponseEntity<>(reportService.ifReportExists(localDate), HttpStatus.OK);
        } catch (ParseException e) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Can't parse the date time.");
        }
    }
}
