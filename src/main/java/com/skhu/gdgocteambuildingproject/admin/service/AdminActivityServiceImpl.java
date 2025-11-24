package com.skhu.gdgocteambuildingproject.admin.service;

import com.skhu.gdgocteambuildingproject.admin.dto.activity.ActivitySaveRequestDto;
import com.skhu.gdgocteambuildingproject.admin.dto.activity.PostResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.activity.PostSaveDto;
import com.skhu.gdgocteambuildingproject.admin.exception.ActivityPostNotFoundException;
import com.skhu.gdgocteambuildingproject.admin.model.PostInfoMapper;
import com.skhu.gdgocteambuildingproject.community.domain.Activity;
import com.skhu.gdgocteambuildingproject.community.domain.ActivityCategory;
import com.skhu.gdgocteambuildingproject.community.repository.ActivityCategoryRepository;
import com.skhu.gdgocteambuildingproject.community.repository.ActivityRepository;
import com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminActivityServiceImpl implements AdminActivityService {

    private final ActivityCategoryRepository activityCategoryRepository;
    private final ActivityRepository activityRepository;

    private final PostInfoMapper postInfoMapper;

    @Override
    @Transactional
    public void createActivity(ActivitySaveRequestDto requestDto) {
        ActivityCategory category = getOrCreateCategory(requestDto);

        createPosts(requestDto.posts(), category);
    }

    @Override
    @Transactional
    public PostResponseDto updateActivityPost(Long postId, PostSaveDto requestDto) {
        Activity activity = activityRepository.findById(postId)
                .orElseThrow(() -> new ActivityPostNotFoundException(ExceptionMessage.ACTIVITY_POST_NOT_FOUND));

        activity.update(
                requestDto.title(),
                requestDto.speaker(),
                requestDto.generation(),
                requestDto.thumbnailUrl(),
                requestDto.videoUrl()
        );

        return postInfoMapper.toPostResponseDto(activity);
    }

    private ActivityCategory getOrCreateCategory(ActivitySaveRequestDto requestDto) {
        return activityCategoryRepository.findByName(requestDto.categoryName())
                .orElseGet(() -> createCategory(requestDto));
    }

    private ActivityCategory createCategory(ActivitySaveRequestDto requestDto) {
        ActivityCategory newCategory = ActivityCategory.builder()
                .name(requestDto.categoryName())
                .isPublished(requestDto.published())
                .build();

        return activityCategoryRepository.save(newCategory);
    }

    private void createPosts(List<PostSaveDto> posts, ActivityCategory category) {
        if (posts == null || posts.isEmpty()) return;

        for (PostSaveDto postDto : posts) {
            Activity activity = Activity.builder()
                    .activityCategory(category)
                    .title(postDto.title())
                    .generation(postDto.generation())
                    .speaker(postDto.speaker())
                    .videoUrl(postDto.videoUrl())
                    .thumbnailUrl(postDto.thumbnailUrl())
                    .build();

            activityRepository.save(activity);
        }
    }
}
