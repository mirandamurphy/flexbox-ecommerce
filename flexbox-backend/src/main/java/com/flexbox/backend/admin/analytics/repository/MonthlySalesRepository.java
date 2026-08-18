package com.flexbox.backend.admin.analytics.repository;

import com.flexbox.backend.admin.analytics.model.MonthlySales;
import com.flexbox.backend.admin.analytics.model.MonthlySalesId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonthlySalesRepository extends JpaRepository<MonthlySales, MonthlySalesId> {



}