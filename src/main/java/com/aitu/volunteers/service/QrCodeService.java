package com.aitu.volunteers.service;

import com.aitu.volunteers.model.QrCode;
import com.aitu.volunteers.repository.QrCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QrCodeService {
    private final QrCodeRepository qrCodeRepository;

    public QrCode createQrCode() {
        QrCode qrCode = QrCode.builder()
                .code(UUID.randomUUID().toString())
                .scanDate(null)
                .build();
        return qrCode;
    }
}
