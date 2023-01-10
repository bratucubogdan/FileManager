package com.filemanager.file;

import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface FileRepository extends JpaRepository<FileModel, Long> {
    FileModel getFileModelById(Long id);
}
