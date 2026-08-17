package com.flexbox.backend.catalog.box.repository;

import com.flexbox.backend.catalog.box.model.SubscriptionBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubscriptionBoxRepository extends JpaRepository<SubscriptionBox, Long> {


    List<SubscriptionBox> findAllByIsActiveTrueOrderByIdAsc();


    boolean existsByNameIgnoreCase(String name);
    Long id(Long id);


    @Modifying
    @Query("""
           update SubscriptionBox s set s.isActive = :isActive where s.id = :id
           """)
    int updateIsActiveById(
            @Param("isActive") boolean isActive,
            @Param("id") Long id
    );

}