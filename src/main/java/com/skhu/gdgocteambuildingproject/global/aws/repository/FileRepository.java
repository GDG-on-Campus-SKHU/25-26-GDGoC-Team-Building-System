package com.skhu.gdgocteambuildingproject.global.aws.repository;

import com.skhu.gdgocteambuildingproject.global.aws.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
