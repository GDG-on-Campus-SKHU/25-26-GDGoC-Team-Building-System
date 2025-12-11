package com.skhu.gdgocteambuildingproject.global.aws.repository;

import com.skhu.gdgocteambuildingproject.global.aws.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {
    Optional<File> findByUrl(String url);
    void deleteByUrl(String url);
}
