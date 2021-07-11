package com.instructionnetwork.rezervacije.services;

import com.instructionnetwork.rezervacije.exceptions.NoReservationsDefinedException;
import com.instructionnetwork.rezervacije.exceptions.NoReservationsForStudentException;
import com.instructionnetwork.rezervacije.exceptions.ReservationNotFoundException;
import com.instructionnetwork.rezervacije.model.Reservations;
import com.instructionnetwork.rezervacije.repositories.ReservationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Service("ReservationsServices")
public class ReservationsServices {
    @Autowired
    private ReservationsRepository reservationsRepository;

    public Reservations getReservationService(Integer id) {
        if (!reservationsRepository.existsById(id)) throw new NoReservationsDefinedException("Reservation with ID '" + id + "' doesn't exist!");
        return reservationsRepository.findById(id).get();
    }

    public List<Reservations> getAllReservationsService() {
        List<Reservations> t = reservationsRepository.findAll();
        if (t.size() == 0) throw new NoReservationsDefinedException("No reservations defined!");
        return t;
    }

    public Reservations getReservationByIdService(Integer id) {
        if (!reservationsRepository.existsById(id))
            throw new ReservationNotFoundException ("Reservation with ID '" + id + "' doesn't exist!");
        return reservationsRepository.findReservationById(id);
    }

    public Integer addReservationsService(Reservations reservations) {
        return reservationsRepository.save(reservations).getId();
    }

    public void updateReservationService(Reservations reservation, Integer id) {
        Reservations oldRes = reservationsRepository.findById(id).get();
        oldRes.setStudent_id(reservation.getStudent_id());
        oldRes.setAppointment_id(reservation.getAppointment_id());
        reservationsRepository.save(oldRes);
    }

    public void deleteReservationService(Integer id) {
        boolean i = reservationsRepository.existsById(id);
        if (!i) throw new ReservationNotFoundException("Reservation with ID '" + id + "' doesn't exist!");
        reservationsRepository.deleteById(id);
    }

    public List<Reservations> getReservationsForStudentService(Integer studentId) {
        List<Reservations> reservations = reservationsRepository.getReservationsByStudentId(studentId);
        if (reservations.size() == 0) throw new NoReservationsForStudentException("No reservations for student with ID '" + studentId + "' defined!");
        return reservations;
    }

}