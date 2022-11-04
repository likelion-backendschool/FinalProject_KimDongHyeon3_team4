package com.example.finalproject4.cash.repository;

import com.example.finalproject4.cash.entity.CashLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CashLogRepository extends JpaRepository<CashLog, Long> {
}
