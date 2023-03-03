package com.chuwa.redbook.dao;

import com.chuwa.redbook.entity.Report;
import com.chuwa.redbook.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;


@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    Boolean existsByReportDate(LocalDate date);
    Report findByReportDate(LocalDate date);
}