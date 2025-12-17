package com.github.jimtrung.theater.service;

import com.github.jimtrung.theater.dto.BillRequest;
import com.github.jimtrung.theater.dto.BookingRequest;
import com.github.jimtrung.theater.model.*;
import com.github.jimtrung.theater.repository.*;
import com.github.jimtrung.theater.util.EmailValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.jimtrung.theater.dto.MovieRevenueDTO;
import com.github.jimtrung.theater.dto.ShowtimeRevenueDTO;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service

public class BillService {
    private final EmailValidator emailValidator;
    public BillService(EmailValidator emailValidator) {
        this.emailValidator = emailValidator;
    }
    public void bill(UUID userId, BillRequest request) throws Exception {

                 emailValidator.sendBillEmail(
                         request.to(),
                         request.movieTitle(),
                         request.movieRating(),
                         request.showDate(),
                         request.showTime(),
                         request.cinemaName(),
                         request.seats(),
                         request.screenRoom(),
                         request.concessions(),
                         request.ticketCode(),
                         request.totalPrice(),
                         "Thanh toán online"

                 );



    }


}
