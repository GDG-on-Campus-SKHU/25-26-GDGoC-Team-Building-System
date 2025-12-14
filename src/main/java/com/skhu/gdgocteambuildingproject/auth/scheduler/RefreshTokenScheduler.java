package com.skhu.gdgocteambuildingproject.auth.scheduler;

import com.skhu.gdgocteambuildingproject.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshTokenScheduler {
    private final RefreshTokenRepository refreshTokenRepository;

    @Scheduled(fixedDelay = 60 * 60 * 1000)
    @Transactional
    public void deleteExpiredRefreshTokens() {
        int deleted =
                refreshTokenRepository.deleteAllByExpiredAtBefore(LocalDateTime.now());

        if (deleted > 0) {
            log.info("[SCHEDULER] 만료된 RefreshToken {}개 삭제", deleted);
        }
    }
}
