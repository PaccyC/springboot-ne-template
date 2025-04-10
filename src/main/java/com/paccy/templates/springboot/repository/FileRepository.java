package com.paccy.templates.springboot.repository;

import com.paccy.templates.springboot.entities.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FileRepository extends JpaRepository<File, UUID> {
}
