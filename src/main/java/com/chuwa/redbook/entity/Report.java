package com.chuwa.redbook.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name="reports", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"report_date"})
})
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @Column(name = "report_date")
    private LocalDate reportDate;

    private Long totalPosts;
    private Long totalPicturePosts;
    private Long totalVideoPosts;
    private Long hotPost;
}
