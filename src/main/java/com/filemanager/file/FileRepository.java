package com.filemanager.file;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface FileRepository extends JpaRepository<FileModel, Long> {
    FileModel getFileModelById(Long id);

    @Query("SELECT f FROM FileModel f " +
            "WHERE (:mainFieldOfInterest IS NULL OR f.mainFieldOfInterest LIKE %:mainFieldOfInterest%) " +
            "AND (:secondaryFieldOfInterest IS NULL OR f.secondaryFieldOfInterest LIKE %:secondaryFieldOfInterest%)" +
            "AND (:registrationNumber IS NULL OR  f.registrationNumber LIKE %:registrationNumber%)"
    )
    List<FileModel> searchByFields(String mainFieldOfInterest, String secondaryFieldOfInterest, String registrationNumber);
}
