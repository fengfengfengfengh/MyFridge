package com.myfridge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "fridge_status")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FridgeStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer energyLevel; // 精神电量 0-100

    private Integer socialLevel; // 社交电量 0-100

    private Integer creativityLevel; // 创作欲 0-100

    private String todayDrink; // 今日饮品

    private String todayKeyword; // 今日关键词

    @Column(columnDefinition = "TEXT")
    private String todayNote; // 今日备注

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
