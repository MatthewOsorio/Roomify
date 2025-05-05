package com.roomify.survery;

import org.springframework.beans.factory.annotation.Autowired;
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
    public void submitSurvey(@PathVariable Long studentId, @RequestBody SumbissionSurveryDTO sumbissionSurveryDTO) {
        surveryService.submitSurvey(studentId, sumbissionSurveryDTO);
    }

}
