package com.stark.webbanhang.api.user.repository;

import com.stark.webbanhang.api.user.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NewsRepository extends JpaRepository<News, UUID> {
}
