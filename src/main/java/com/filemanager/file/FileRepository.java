package com.filemanager.file;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
@EnableJpaRepositories
public interface FileRepository extends JpaRepository<FileModel, Long> {
    FileModel getFileModelById(Long id);

    @Query("SELECT f FROM FileModel f " +
            "WHERE (:mainFieldOfInterest IS NULL OR f.mainFieldOfInterest = :mainFieldOfInterest) " +
            "AND (:secondaryFieldOfInterest IS NULL OR f.secondaryFieldOfInterest = :secondaryFieldOfInterest)" +
            "AND (:numberDate IS NULL OR f.numberDate = :numberDate)"
    )
    List<FileModel> searchByFields(String mainFieldOfInterest, String secondaryFieldOfInterest, Date numberDate);
}
