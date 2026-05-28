package com.myfridge.repository;

import com.myfridge.entity.FridgeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FridgeStatusRepository extends JpaRepository<FridgeStatus, Long> {
}
