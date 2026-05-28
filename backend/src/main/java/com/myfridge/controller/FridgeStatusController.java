package com.myfridge.controller;

import com.myfridge.entity.FridgeStatus;
import com.myfridge.service.FridgeStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/fridge-status")
public class FridgeStatusController {

    @Autowired
    private FridgeStatusService fridgeStatusService;

    @GetMapping
    public ResponseEntity<FridgeStatus> getStatus() {
        FridgeStatus status = fridgeStatusService.getLatestStatus();
        return ResponseEntity.ok(status);
    }

    @PutMapping
    public ResponseEntity<Map<String, Object>> updateStatus(@RequestBody FridgeStatus status) {
        FridgeStatus updated = fridgeStatusService.updateStatus(status);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", updated);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/initialize")
    public ResponseEntity<FridgeStatus> initialize() {
        FridgeStatus status = fridgeStatusService.initializeStatus();
        return ResponseEntity.ok(status);
    }
}
