package com.skhu.gdgocteambuildingproject.admin.service;

import com.skhu.gdgocteambuildingproject.admin.dto.activity.*;
import com.skhu.gdgocteambuildingproject.admin.exception.ActivityPostNotFoundException;
import com.skhu.gdgocteambuildingproject.admin.model.ActivityCategoryInfoMapper;
import com.skhu.gdgocteambuildingproject.admin.model.ActivityMapper;
import com.skhu.gdgocteambuildingproject.admin.model.PostInfoMapper;
import com.skhu.gdgocteambuildingproject.activity.domain.Activity;
import com.skhu.gdgocteambuildingproject.activity.domain.ActivityCategory;
import com.skhu.gdgocteambuildingproject.activity.repository.ActivityCategoryRepository;
import com.skhu.gdgocteambuildingproject.activity.repository.ActivityRepository;
import com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.Generation;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage.CATEGORY_NOT_FOUND;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminActivityServiceImpl implements AdminActivityService {

    private final ActivityCategoryRepository activityCategoryRepository;
    private final ActivityRepository activityRepository;

    private final PostInfoMapper postInfoMapper;
    private final ActivityCategoryInfoMapper activityCategoryInfoMapper;
    private final ActivityMapper activityMapper;

    @Override
    @Transactional
    public void createActivity(ActivitySaveRequestDto requestDto) {
        ActivityCategory category = getOrCreateCategory(requestDto);

        createPosts(requestDto.posts(), category);
    }

    @Override
    @Transactional
    public PostResponseDto updateActivityPost(Long postId, PostSaveDto requestDto) {
        Activity activity = getActivityPostOrThrow(postId);

        activity.update(
                requestDto.title(),
                requestDto.speaker(),
                Generation.fromLabel(requestDto.generation()),
                requestDto.thumbnailUrl(),
                requestDto.videoUrl()
        );

        return postInfoMapper.toPostResponseDto(activity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityCategoryInfoResponseDto> getCategoryInfos() {
        List<ActivityCategory> categories = activityCategoryRepository.findAll();

        return categories.stream()
                .map(category -> {
                    long count = activityRepository.countByActivityCategory(category);

                    return activityCategoryInfoMapper.toDto(category, count);
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivityResponseDto> getActivitiesByCategory(Long categoryId) {
        ActivityCategory category = getCategoryOrThrow(categoryId);

        List<Activity> activities = activityRepository.findByActivityCategory(category);

        return activities.stream()
                .map(activityMapper::toActivityResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteActivityPost(Long postId) {
        Activity activity = getActivityPostOrThrow(postId);
        activityRepository.delete(activity);
    }

    @Override
    @Transactional
    public void deleteCategory(Long categoryId) {
        ActivityCategory category = getCategoryOrThrow(categoryId);

        activityRepository.deleteAllByActivityCategory(category);

        activityCategoryRepository.delete(category);
    }

    @Override
    @Transactional
    public void updateCategoryTitleAndStatus(Long categoryId, ActivityUpdateRequestDto dto) {
        ActivityCategory category = getCategoryOrThrow(categoryId);
        category.changeCategoryInfo(dto.categoryName(), dto.published());
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
                    .generation(Generation.fromLabel(postDto.generation()))
                    .speaker(postDto.speaker())
                    .videoUrl(postDto.videoUrl())
                    .thumbnailUrl(postDto.thumbnailUrl())
                    .build();

            activityRepository.save(activity);
        }
    }

    private Activity getActivityPostOrThrow(Long postId) {
        return activityRepository.findById(postId)
                .orElseThrow(() -> new ActivityPostNotFoundException(ExceptionMessage.ACTIVITY_POST_NOT_FOUND));
    }

    private ActivityCategory getCategoryOrThrow(Long categoryId) {
        return activityCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException(CATEGORY_NOT_FOUND.getMessage()));
    }
}
