package com.skhu.gdgocteambuildingproject.teambuilding.model.mapper;

import com.skhu.gdgocteambuildingproject.teambuilding.dto.project.ProjectAvailablePartResponseDto;
import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ProjectAvailablePartMapper {
    public List<ProjectAvailablePartResponseDto> map(TeamBuildingProject project) {
        List<ProjectAvailablePartResponseDto> dtos = new ArrayList<>();

        for (Part part : Part.values()) {
            ProjectAvailablePartResponseDto dto = ProjectAvailablePartResponseDto.builder()
                    .part(part)
                    .available(project.isAvailable(part))
                    .build();

            dtos.add(dto);
        }

        return Collections.unmodifiableList(dtos);
    }
}
