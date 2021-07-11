package com.instructionnetwork.korisnik.services;

import com.instructionnetwork.korisnik.exceptions.InstructorNotFoundException;
import com.instructionnetwork.korisnik.exceptions.NoRatingsDefinedException;
import com.instructionnetwork.korisnik.exceptions.RatingNotFoundException;
import com.instructionnetwork.korisnik.model.InstructorsRatings;
import com.instructionnetwork.korisnik.repositories.InstructorsRatingsRepository;
import com.instructionnetwork.korisnik.repositories.InstructorsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service("InstructorsRatingsServices")
public class InstructorsRatingsServices {

    @Autowired
    InstructorsRatingsRepository instructorsRatingsRepository;
    @Autowired
    InstructorsRepository instructorsRepository;

    public ArrayList<InstructorsRatings> getRatingsByInstructorService(Integer instructor_id) {
        ArrayList<InstructorsRatings> instruktori = instructorsRatingsRepository.findRatingsByInstructor(instructor_id);
        if (instruktori.size() == 0) throw new RatingNotFoundException("Instructor with ID '" + instructor_id + "' doesn't have ratings!");
        return instruktori;
    }

    public ArrayList<InstructorsRatings> getAllRatingsService() {
        ArrayList<InstructorsRatings> ocjene = instructorsRatingsRepository.findAll();
        if (ocjene.size() == 0) throw new NoRatingsDefinedException("No ratings defined!");
        return ocjene;
    }

    public void updateRatingService(Integer id, InstructorsRatings instructorsRatings) {
        InstructorsRatings old = instructorsRatingsRepository.findById(id).get();
        old.setRating(instructorsRatings.getRating());
        old.setInstructors(instructorsRatings.getInstructors());
        old.setStudents(instructorsRatings.getStudents());
        instructorsRatingsRepository.save(old);
    }

    public void addRatingService(InstructorsRatings instructorsRatings) {
        instructorsRatingsRepository.save(instructorsRatings);
    }

    public void deleteRatingService(Integer id) {
        boolean rating = instructorsRatingsRepository.existsById(id);
        if (!rating) throw new RatingNotFoundException("No rating with the ID '" + id + "' defined!");
        instructorsRatingsRepository.removeRatingById(id);
    }

    public Double getInstructorsAverageRatingService(Integer id) {
        if (!instructorsRepository.findById(id).isPresent())
            throw new InstructorNotFoundException("Instructor with ID '" + id + "' doesn't exist!");
        return instructorsRatingsRepository.averageRatingForInstructor(id);
    }

}