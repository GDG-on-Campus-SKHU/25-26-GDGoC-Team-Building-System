package com.skhu.gdgocteambuildingproject.admin.service;

import com.skhu.gdgocteambuildingproject.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final UserRepository userRepository;

    @Override
    public void getDashboard() {
    }
}
