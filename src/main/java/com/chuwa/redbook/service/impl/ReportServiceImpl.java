package com.chuwa.redbook.service.impl;

import com.chuwa.redbook.dao.PostRepository;
import com.chuwa.redbook.dao.ReportRepository;
import com.chuwa.redbook.entity.Post;
import com.chuwa.redbook.entity.Report;
import com.chuwa.redbook.exception.BlogAPIException;
import com.chuwa.redbook.service.ReportService;
import com.chuwa.redbook.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * b. 内容：
 * i.  total-posts: number
 * ii. Total-pic-posts: number
 * iii.  Total-video-posts: number
 * iv.  Hot-post: postID (获得comment最多的post)
 * */
@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Override
    public File getTodayReport(LocalDate date){
        if(reportRepository.existsByReportDate(date)){
            Report report = reportRepository.findByReportDate(date);
            return convertToFile(report);
        }
        else{
            Report report = generateTodayReport(date);
            reportRepository.save(report);
            return convertToFile(report);
        }
    }

    @Override
    public boolean ifReportExists(LocalDate date){
        if(reportRepository.existsByReportDate(date)){
            return true;
        }
        else{
            return false;
        }
    }
    private Report generateTodayReport(LocalDate date){
        Long cnt = countTodayPosts(date);
        Long pictureCnt = countTodayPicturePosts(date);
        Long videoCnt = countTodayVideoPosts(date);
        Long hotPost = getHottestPost(date);
        Report report = new Report();

        report.setReportDate(date);
        report.setTotalPosts(cnt);
        report.setTotalPicturePosts(pictureCnt);
        report.setTotalVideoPosts(videoCnt);
        report.setHotPost(hotPost);
        return report;
    }

    private Long countTodayPosts(LocalDate date){
        List<Post> posts = postRepository.getPostByCreateDateTimeBetween(date.atStartOfDay(), date.atStartOfDay().plusDays(1));
        Long total = Long.valueOf(posts.size());
        return total;
    }

    private Long countTodayPicturePosts(LocalDate date){
        List<Post> posts = postRepository.getPostByCreateDateTimeBetween(date.atStartOfDay(), date.atStartOfDay().plusDays(1));
        Long cnt = posts.stream()
                .filter(p -> p.getVideo() == null)
                .count();
        return cnt;
    }


    private Long countTodayVideoPosts(LocalDate date){
        List<Post> posts = postRepository.getPostByCreateDateTimeBetween(date.atStartOfDay(), date.atStartOfDay().plusDays(1));
        Long cnt = posts.stream()
                .filter(p -> p.getVideo() != null)
                .count();
        return cnt;
    }

    private Long getHottestPost(LocalDate date){
        List<Post> posts = postRepository.getPostByCreateDateTimeBetween(date.atStartOfDay(), date.atStartOfDay().plusDays(1));
        Post post = posts.stream()
                .sorted(Comparator.comparingInt(p -> p.getComments().size()))
                .findFirst().orElseGet(null);
        if(post == null){
            return 0L;
        }
        return post.getId();
    }

    public File convertToFile(Report report) {
        List<String[]> strlist = new ArrayList<>();
        Field[] fields = report.getClass().getFields();
        try {
            for (Field f : fields) {
                String name = f.getName();
                String value = String.valueOf(f.get(report));
                String [] pair = new String[2];
                pair[0] = name;
                pair[1] = value;
                strlist.add(pair);
            }
        }
        catch (Exception e){
            throw new BlogAPIException(HttpStatus.NOT_FOUND, "Fails to generate the report. ");
        }
        return convertToCSV(strlist, report.getReportDate());
    }

    private File convertToCSV(List<String[]> report, LocalDate date) {
        File csvFile = new File(AppConstants.DEFAULT_REPORT_BASE_URL+ date+"-report.csv");
        try{
            FileWriter fileWriter = new FileWriter(csvFile);
            for (String[] data : report) {
                StringBuilder line = new StringBuilder();
                for (int i = 0; i < data.length; i++) {
                    line.append("\"");
                    line.append(data[i].replaceAll("\"","\"\""));
                    line.append("\"");
                    if (i != data.length - 1) {
                        line.append(',');
                    }
                }
                line.append("\n");
                fileWriter.write(line.toString());
            }
            fileWriter.close();
        }
        catch(Exception e){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Fail to generate the report.");
        }
        return csvFile;
    }

}
