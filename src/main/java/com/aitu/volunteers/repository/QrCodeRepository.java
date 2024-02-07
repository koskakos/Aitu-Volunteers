package com.aitu.volunteers.repository;

import com.aitu.volunteers.model.QrCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QrCodeRepository extends JpaRepository<QrCode, Long> {
}
