package com.paccy.templates.springboot.repository;

import com.paccy.templates.springboot.entities.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<File, UUID> {
    File findByName(String fileName);
}
