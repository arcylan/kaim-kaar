package com.kaim.kaar.service;

import com.kaim.kaar.DTOs.BookingResponseDTO;
import com.kaim.kaar.entity.Booking;
import com.kaim.kaar.entity.ProviderCategory;
import com.kaim.kaar.entity.User;
import com.kaim.kaar.enums.BookingStatus;
import com.kaim.kaar.repository.BookingRepository;
import com.kaim.kaar.repository.ProviderCategoryRepository;
import com.kaim.kaar.repository.UserRepositoy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private UserRepositoy userRepositoy;
    @Autowired
    private ProviderCategoryRepository providerCategoryRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private EmailService emailService;

//    public void bookService(Booking booking){
//        if(booking==null){
//            throw new RuntimeException("No bookings found");
//        }
//        if(booking.getCustomer()==null){
//            throw new RuntimeException("No customer found");
//        }
//        if(booking.getProviderCategory()==null){
//            throw new RuntimeException("No provider found");
//        }
//
//        User customer = userRepositoy.findById(booking.getCustomer().getId()).orElseThrow(() -> new RuntimeException("Customer is NUll"));
//        ProviderCategory provider = providerCategoryRepository.findById(booking.getProviderCategory().getId()).orElseThrow(() -> new RuntimeException("Service is null"));
//
//        Booking newBooking = new Booking();
//        newBooking.setCustomer(customer);
//        newBooking.setProviderCategory(provider);
//        newBooking.setBookingDate(LocalDateTime.now());
//        newBooking.setAddress(booking.getAddress());
//        newBooking.setStatus(BookingStatus.PENDING);
//        newBooking.setPhoneNumber(booking.getPhoneNumber());
//
//        bookingRepository.save(newBooking);
//    }
public void bookService(Booking booking){

    Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

    String email = authentication.getName();

    User customer = userRepositoy.findByEmail(email);

    ProviderCategory provider =
            providerCategoryRepository.findById(
                    booking.getProviderCategory().getId()
            ).orElseThrow(() -> new RuntimeException("Service not found"));

    Booking newBooking = new Booking();

    newBooking.setCustomer(customer);

    newBooking.setProviderCategory(provider);

    newBooking.setBookingDate(LocalDateTime.now());

    newBooking.setAddress(booking.getAddress());

    newBooking.setStatus(BookingStatus.PENDING);

    newBooking.setPhoneNumber(booking.getPhoneNumber());

    bookingRepository.save(newBooking);

    // Email to provider
    emailService.sendMail(
            provider.getProvider().getEmail(),
            "New Booking Received - KaimKaar",
            "Hello " + provider.getProvider().getUserName() + ",\n\n"
                    + "New booking from: " + customer.getUserName() + "\n"
                    + "Address: " + booking.getAddress() + "\n"
                    + "Phone: " + booking.getPhoneNumber()
    );

// Email to customer
    emailService.sendMail(
            customer.getEmail(),
            "Booking Confirmed - KaimKaar",
            "Hello " + customer.getUserName() + ",\n\n"
                    + "Your booking has been placed successfully!\n"
                    + "Service: " + provider.getServiceCategory().getName() + "\n"
                    + "Status: PENDING\n\n"
                    + "- KaimKaar Team"
    );
}

    public List<BookingResponseDTO> getProviderBooking() {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User provider = userRepositoy.findByEmail(email);

        if(provider == null) {
            throw new RuntimeException("Provider not found");
        }

        List<Booking> bookings =
                bookingRepository
                        .findByProviderCategory_Provider_Id(
                                provider.getId()
                        );

        List<BookingResponseDTO> response =
                new ArrayList<>();

        for(Booking booking : bookings) {

            BookingResponseDTO dto =
                    new BookingResponseDTO();

            dto.setBookingId(booking.getId());

            dto.setCustomerName(
                    booking.getCustomer()
                            .getUserName()
            );

            dto.setProviderName(
                    booking.getProviderCategory()
                            .getProvider()
                            .getUserName()
            );

            dto.setServiceName(
                    booking.getProviderCategory()
                            .getServiceCategory()
                            .getName()
            );

            dto.setPrice(
                    booking.getProviderCategory()
                            .getPrice()
            );

            dto.setStatus(
                    booking.getStatus()
            );

            dto.setBookingDate(
                    booking.getBookingDate()
            );

            dto.setAddress(
                    booking.getAddress()
            );

            dto.setPhoneNumber(
                    booking.getPhoneNumber()
            );

            response.add(dto);
        }

        return response;
    }

    public void acceptBooking(Long bookingId){
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("bookings not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User provider = userRepositoy.findByEmail(email);
        if(provider==null){
            throw new RuntimeException("provier not found");
        }
        Long bookingProviderId = booking.getProviderCategory().getProvider().getId();
        if(!bookingProviderId.equals(provider.getId())){
            throw new RuntimeException(
                    "You cannot manage this booking"
            );
        }
        if(booking.getStatus() != BookingStatus.PENDING) {
            throw new RuntimeException(
                    "Booking already processed"
            );
        }
        booking.setStatus(BookingStatus.ACCEPTED);
        bookingRepository.save(booking);
        emailService.sendMail(booking.getCustomer().getEmail(),
                "Booking Accepted",
                "Your booking for "
                        + booking.getProviderCategory()
                        .getServiceCategory()
                        .getName()
                        + " has been accepted."
        + " you can contact provider here " + provider.getEmail());

    }
    public void rejectBooking(Long bookingId){
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("bookings not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User provider = userRepositoy.findByEmail(email);
        if(provider==null){
            throw new RuntimeException("provier not found");
        }
        Long bookingProviderId = booking.getProviderCategory().getProvider().getId();
        if(!bookingProviderId.equals(provider.getId())){
            throw new RuntimeException(
                    "You cannot manage this booking"
            );
        }
        if(booking.getStatus() != BookingStatus.PENDING) {
            throw new RuntimeException(
                    "Booking already processed"
            );
        }
        booking.setStatus(BookingStatus.REJECTED);
        bookingRepository.save(booking);
        emailService.sendMail(booking.getCustomer().getEmail(),
                "Booking Rejected",
                "Your booking for "
                        + booking.getProviderCategory()
                        .getServiceCategory()
                        .getName()
                        + " has been rejected."
                        + " you can contact provider here " + provider.getEmail());

    }

    }


