package com.myfridge.service;

import com.myfridge.entity.FridgeStatus;
import com.myfridge.repository.FridgeStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FridgeStatusService {

    @Autowired
    private FridgeStatusRepository fridgeStatusRepository;

    // 获取最新的冰箱状态
    public FridgeStatus getLatestStatus() {
        List<FridgeStatus> statuses = fridgeStatusRepository.findAll();
        if (statuses.isEmpty()) {
            // 创建默认状态
            FridgeStatus defaultStatus = FridgeStatus.builder()
                    .energyLevel(50)
                    .socialLevel(50)
                    .creativityLevel(50)
                    .todayDrink("暖水")
                    .todayKeyword("日常")
                    .todayNote("一切正常运行中")
                    .build();
            return fridgeStatusRepository.save(defaultStatus);
        }
        return statuses.get(0);
    }

    // 更新冰箱状态
    public FridgeStatus updateStatus(FridgeStatus status) {
        return fridgeStatusRepository.save(status);
    }

    // 初始化或重置状态
    public FridgeStatus initializeStatus() {
        FridgeStatus defaultStatus = FridgeStatus.builder()
                .energyLevel(62)
                .socialLevel(24)
                .creativityLevel(88)
                .todayDrink("冰美式")
                .todayKeyword("缓慢重启")
                .todayNote("不要问，问就是正在加载。")
                .build();
        return fridgeStatusRepository.save(defaultStatus);
    }
}
