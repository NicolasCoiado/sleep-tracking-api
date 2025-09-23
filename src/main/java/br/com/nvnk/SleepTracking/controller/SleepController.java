package br.com.nvnk.SleepTracking.controller;

import br.com.nvnk.SleepTracking.controller.dto.request.SleepAttemptRequest;
import br.com.nvnk.SleepTracking.controller.dto.response.SleepAttemptResponse;
import br.com.nvnk.SleepTracking.entity.SleepAttempt;
import br.com.nvnk.SleepTracking.mapper.SleepAttemptMapper;
import br.com.nvnk.SleepTracking.service.SleepAttemptService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping ("/sleep")
@RequiredArgsConstructor
public class SleepController {

    private final SleepAttemptService service;
    private final SleepAttemptMapper sleepAttemptMapper;

    @PostMapping
    public ResponseEntity<Map<String, Object>> registerAttempt (@RequestBody @Valid SleepAttemptRequest sleepAttemptRequest){

        SleepAttempt sleepAttempt = sleepAttemptMapper.toEntity(sleepAttemptRequest);

        SleepAttempt sleepAttemptSaved = service.save(sleepAttempt);

        Map<String, Object> response = new HashMap<>();
        response.put("Message", "Successful attempt to save sleep.");
        response.put("Attempt to sleep:", sleepAttemptSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> findAttemptsByUser (){
        List<SleepAttempt> allAttempts = service.findAttemptsByUser();
        List<SleepAttemptResponse> sleepAttemptsDTOResponse = allAttempts.stream().map(sleepAttemptMapper::toResponse).toList();

        Map<String, Object> response = new HashMap<>();
        response.put("Message", "Successful sleep attempts listed.");
        response.put("Attempts to sleep:", sleepAttemptsDTOResponse);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/success")
    public ResponseEntity<Map<String, Object>> findSuccessfulAttemptsByUser() {
        List<SleepAttempt> attempts = service.findSuccessfulAttemptsByUser();
        List<SleepAttemptResponse> responseDTOs =
                attempts.stream().map(sleepAttemptMapper::toResponse).toList();

        Map<String, Object> response = new HashMap<>();
        response.put("Message", "Successful sleep attempts listed.");
        response.put("Attempts", responseDTOs);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/unsuccessfully")
    public ResponseEntity<Map<String, Object>> findUnsuccessfullyAttemptsByUser() {
        List<SleepAttempt> attempts = service.findUnsuccessfulAttemptsByUser();
        List<SleepAttemptResponse> responseDTOs =
                attempts.stream().map(sleepAttemptMapper::toResponse).toList();

        Map<String, Object> response = new HashMap<>();
        response.put("Message", "Successful sleep attempts listed.");
        response.put("Attempts", responseDTOs);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-day")
    public ResponseEntity<Map<String, Object>> findAttemptsByDay(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<SleepAttempt> attempts = service.findAttemptsByDayAndUser(date);
        List<SleepAttemptResponse> responseDTOs = attempts.stream()
                .map(sleepAttemptMapper::toResponse)
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("Message", "Sleep attempts found for " + date);
        response.put("Attempts", responseDTOs);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-month")
    public ResponseEntity<Map<String, Object>> findAttemptsByMonth(
            @RequestParam("year") int year,
            @RequestParam("month") int month) {

        List<SleepAttempt> attempts = service.findAttemptsByMonthAndUser(year, month);
        List<SleepAttemptResponse> responseDTOs = attempts.stream()
                .map(sleepAttemptMapper::toResponse)
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("Message", "Sleep attempts found for " + year + "-" + month);
        response.put("Attempts", responseDTOs);

        return ResponseEntity.ok(response);
    }

}
