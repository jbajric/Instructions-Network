package com.instructionnetwork.termini.services;

import com.instructionnetwork.termini.exceptions.AppointmentNotFoundException;
import com.instructionnetwork.termini.exceptions.NoAppointmentsDefinedException;
import com.instructionnetwork.termini.exceptions.NoAppointmentsForInstructorException;
import com.instructionnetwork.termini.model.Appointment;
import com.instructionnetwork.termini.repositories.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service("AppointmentService")
public class AppointmentService {

    @Autowired
    AppointmentRepository appointmentRepository;

    public List<Appointment> getAppointmentsByInstructorService(Integer instructorId) {
        List<Appointment> appointments = appointmentRepository.getAppointmentsByInstructor(instructorId);
        if (appointments.size() == 0)
            throw new NoAppointmentsForInstructorException("No appointments for instructor with ID " + instructorId + " defined!");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        appointments.sort(Comparator.comparing(s -> LocalDate.parse(s.getDate(), formatter)));
        return appointments;
    }

    public Appointment getAppointmentByIdService(Integer id) {
        if (!appointmentRepository.existsById(id))
            throw new AppointmentNotFoundException ("Appointment with ID '" + id + "' doesn't exist!");
        return appointmentRepository.findAppointmentById(id);
    }

    public void deleteAppointmentsByInstructorService(Integer instructorId) {
        List<Appointment> appointments = appointmentRepository.findAll();
        List<Appointment> appointmentsByInstructor = appointmentRepository.getAppointmentsByInstructor(instructorId);
        if (appointmentsByInstructor.size() == 0)
            throw new NoAppointmentsForInstructorException("No appointments for instructor with ID " + instructorId + " defined!");
        for (Appointment app : appointments)
            if (app.getInstructor_id().equals(instructorId))
                appointmentRepository.removeAppointmentById(app.getId());
    }

    public Appointment getAppointmentService(String date) {
        Appointment t = appointmentRepository.findByDate(date);
        if (t == null)
            throw new AppointmentNotFoundException("Appointment with date '" + date + "' doesn't exist!");
        return t;
    }

    public List<Appointment> getAllAppointmentsService() {
        List<Appointment> t = appointmentRepository.findAll();
        if (t.size() == 0) throw new NoAppointmentsDefinedException("No appointments defined!");
        List<Appointment> available = new ArrayList<>();
        for (Appointment app: t)
            if(app.getAvailable())
                available.add(app);
        if (available.size() == 0) throw new NoAppointmentsDefinedException("No appointments defined!");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        available.sort(Comparator.comparing(s -> LocalDate.parse(s.getDate(), formatter)));
        return available;
    }

    public void deleteAppointmentService(Integer id) {
        boolean i = appointmentRepository.existsById(id);
        if (!i) throw new AppointmentNotFoundException ("Appointment with ID '" + id + "' doesn't exist!");
        appointmentRepository.removeAppointmentById(id);
    }

    public void addAppointmentService(Appointment appointment) {
        appointmentRepository.save(appointment);
    }

    public void updateAppointmentService(Appointment appointment, Integer id) {
        Appointment oldApp = appointmentRepository.findById(id).get();
        oldApp.setDate(appointment.getDate());
        oldApp.setStart_time(appointment.getStart_time());
        oldApp.setEnd_time(appointment.getEnd_time());
        oldApp.setLocation(appointment.getLocation());
        oldApp.setPrice(appointment.getPrice());
        oldApp.setInstructor_id(appointment.getInstructor_id());
        oldApp.setSubject_id(appointment.getSubject_id());
        oldApp.setAvailable(appointment.getAvailable());
        appointmentRepository.save(oldApp);
    }

}
