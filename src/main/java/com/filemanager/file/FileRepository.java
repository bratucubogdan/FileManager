package com.filemanager.file;

import jakarta.persistence.Id;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface FileRepository extends JpaRepository<FileModel, Long> {
    FileModel getFileModelById(Long id);


    List<FileModel>  getFileModelByMainFieldOfInterestOrSecondaryFieldOfInterestOrRegistrationNumber(@Param("mainField") String mainFieldOfInterest, @Param("secondaryField") String secondaryFieldOfInterest, @Param("registrationNumber") String registrationNumber);


}
