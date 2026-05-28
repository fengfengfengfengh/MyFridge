package com.myfridge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "notes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String nickname;

    @Column(name = "color")
    private String color; // 便签颜色: cream, pink, mint, blue, purple, white

    @Column(name = "icon")
    private String icon; // 贴纸图标: emoji

    private Integer magnetCount; // 磁铁数量

    @Column(name = "position_x")
    private Double positionX; // 拖拽位置

    @Column(name = "position_y")
    private Double positionY;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_active")
    private Boolean isActive;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (magnetCount == null) {
            magnetCount = 0;
        }
        if (isActive == null) {
            isActive = true;
        }
        if (positionX == null) {
            positionX = Math.random() * 80;
        }
        if (positionY == null) {
            positionY = Math.random() * 60;
        }
    }
}
