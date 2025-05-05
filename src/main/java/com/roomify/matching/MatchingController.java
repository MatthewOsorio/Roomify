package com.roomify.matching;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/student/{studentId}/matching")
public class MatchingController {
    private final MatchingService matchingService;

    @Autowired
    public MatchingController(MatchingService matchingService) {
        this.matchingService = matchingService;
    }

    @GetMapping
    public List<MatchDTO> findMatches(@PathVariable Long studentId) {
        List<Match> matches = matchingService.findMatches(studentId);

        return matches.stream()
                .map(match -> new MatchDTO(match))
                .toList();
    }
       

}
