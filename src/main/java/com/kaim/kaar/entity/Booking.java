package com.kaim.kaar.entity;

import com.kaim.kaar.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User customer;
    @ManyToOne
    private ProviderCategory providerCategory;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    private LocalDateTime bookingDate;
    private String address;
    private Long phoneNumber;

}
