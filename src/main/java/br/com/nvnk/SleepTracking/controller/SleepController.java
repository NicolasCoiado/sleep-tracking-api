package br.com.nvnk.SleepTracking.controller;

import br.com.nvnk.SleepTracking.controller.dto.request.SleepAttemptRequest;
import br.com.nvnk.SleepTracking.entity.SleepAttempt;
import br.com.nvnk.SleepTracking.mapper.SleepAttemptMapper;
import br.com.nvnk.SleepTracking.service.SleepAttemptService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping ("/sleep")
@RequiredArgsConstructor
public class SleepController {

    private final SleepAttemptService service;
    private final SleepAttemptMapper sleepAttemptMapper;

    @PostMapping("/attempt")
    public ResponseEntity<Map<String, Object>> registerAttempt (@RequestBody @Valid SleepAttemptRequest sleepAttemptRequest){

        SleepAttempt sleepAttempt = sleepAttemptMapper.toEntity(sleepAttemptRequest);

        SleepAttempt sleepAttemptSaved = service.save(sleepAttempt);

        Map<String, Object> response = new HashMap<>();
        response.put("Message", "Successful attempt to save sleep.");
        response.put("Sleep Attempt", sleepAttemptSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
