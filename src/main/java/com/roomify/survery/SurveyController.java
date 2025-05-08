package com.roomify.survery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.roomify.survery.SurveyDTO.SumbissionSurveryDTO;

@RestController
@RequestMapping(path = "api/v1/student/{studentId}/survey")
public class SurveyController {
    private final SurveyService surveryService;

    @Autowired
    public SurveyController(SurveyService surveryService){
        this.surveryService = surveryService;
    }

    @PostMapping
    public ResponseEntity<?> submitSurvey(@PathVariable Long studentId, @RequestBody SumbissionSurveryDTO sumbissionSurveryDTO) {
        try {
            surveryService.submitSurvey(studentId, sumbissionSurveryDTO);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

}
