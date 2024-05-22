package com.ssafy.urturn.history.repository;

import com.ssafy.urturn.history.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, Long>, HistoryCustomRepository {
}
