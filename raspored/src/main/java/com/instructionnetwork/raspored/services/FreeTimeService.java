package com.instructionnetwork.raspored.services;

import com.instructionnetwork.raspored.exceptions.FreeTimeNotFoundException;
import com.instructionnetwork.raspored.exceptions.NoFreeTimesDefinedException;
import com.instructionnetwork.raspored.exceptions.NoFreeTimesForStudentException;
import com.instructionnetwork.raspored.model.FreeTime;
import com.instructionnetwork.raspored.repositories.FreeTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("FreeTimeService")
public class FreeTimeService {
    
    @Autowired
    FreeTimeRepository freeTimeRepository;


    public List<FreeTime> getFreeTimesByStudentService(Integer studentId)  throws NoFreeTimesForStudentException {
        List<FreeTime> freeTimes = freeTimeRepository.getFreeTimesByStudent(studentId);
        if (freeTimes.size() == 0)
            throw new NoFreeTimesForStudentException("No freetime for the student with ID " + studentId + "!");
        return freeTimes;
    }

    public FreeTime getFreeTimeService(Integer id) throws FreeTimeNotFoundException {
        FreeTime freeTime = freeTimeRepository.findFreeTimeById(id);
        if (freeTime == null) throw new FreeTimeNotFoundException("Freetime with ID '" + id + "' doesn't exist!");
        return freeTime;
    }

    public List<FreeTime> getAllFreeTimesService() throws NoFreeTimesDefinedException {
        List<FreeTime> freeTimes = freeTimeRepository.findAll();
        if (freeTimes.size() == 0) throw new NoFreeTimesDefinedException("No freetimes defined!");
        return freeTimes;
    }

    public void deleteFreeTimeService(Integer id) throws FreeTimeNotFoundException {
        boolean i = freeTimeRepository.existsById(id);
        if (!i) throw new FreeTimeNotFoundException("Freetime with ID '" + id + "' doesn't exist!");
        freeTimeRepository.removeFreeTimeById(id);
    }

    public void updateFreeTimeService(FreeTime freeTime, Integer id) {
        FreeTime oldFT = freeTimeRepository.findById(id).get();
        oldFT.setDate(freeTime.getDate());
        oldFT.setStart_time(freeTime.getStart_time());
        oldFT.setEnd_time(freeTime.getEnd_time());
        oldFT.setStudent_id(freeTime.getStudent_id());
        freeTimeRepository.save(oldFT);
    }

    public void addFreeTimeService(FreeTime freeTime) {
        freeTimeRepository.save(freeTime);
    }

}
