package com.kaim.kaar.DTOs;

import com.kaim.kaar.enums.BookingStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookingResponseDTO {

    private Long bookingId;

    private String customerName;

    private String providerName;

    private String serviceName;

    private Double price;

    private BookingStatus status;

    private LocalDateTime bookingDate;

    private String address;

    private Long phoneNumber;
}