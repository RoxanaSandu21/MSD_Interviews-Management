package com.example.interviewmgmt.service;

import com.example.interviewmgmt.model.Interviewer;
import com.example.interviewmgmt.repo.InterviewerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterviewerService {

    private final InterviewerRepository interviewerRepository;

    public InterviewerService(InterviewerRepository interviewerRepository) {
        this.interviewerRepository = interviewerRepository;
    }

    public List<Interviewer> getAllInterviewers() {
        return interviewerRepository.findAll();
    }

    public Interviewer getById(Long id) {
        return interviewerRepository.findById(id).orElse(null);
    }

    public Interviewer save(Interviewer interviewer) {
        return interviewerRepository.save(interviewer);
    }

    public void delete(Long id) {
        interviewerRepository.deleteById(id);
    }
}
