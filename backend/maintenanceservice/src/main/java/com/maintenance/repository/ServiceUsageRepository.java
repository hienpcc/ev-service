package com.maintenance.repository;

import com.maintenance.entity.ServiceUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceUsageRepository extends JpaRepository<ServiceUsage, Long> {
}
