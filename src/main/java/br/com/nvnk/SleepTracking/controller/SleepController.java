package br.com.nvnk.SleepTracking.controller;

import br.com.nvnk.SleepTracking.controller.dto.request.SleepAttemptRequest;
import br.com.nvnk.SleepTracking.controller.dto.response.SleepAttemptResponse;
import br.com.nvnk.SleepTracking.entity.SleepAttempt;
import br.com.nvnk.SleepTracking.mapper.SleepAttemptMapper;
import br.com.nvnk.SleepTracking.service.SleepAttemptService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}") // TODO: Verify the owner's ID
    public ResponseEntity<Map<String, Object>> findAttemptsByUser (@PathVariable String id){
        List<SleepAttempt> allAttempts = service.findAttemptsByUser(id);
        List<SleepAttemptResponse> sleepAttemptsDTOResponse = allAttempts.stream().map(sleepAttemptMapper::toResponse).toList();

        Map<String, Object> response = new HashMap<>();
        response.put("Message", "Successful sleep attempts listed.");
        response.put("Attempts to sleep:", sleepAttemptsDTOResponse);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/success") // TODO: Verify the owner's ID
    public ResponseEntity<Map<String, Object>> findSuccessfulAttemptsByUser(@PathVariable String id) {
        List<SleepAttempt> attempts = service.findSuccessfulAttemptsByUser(id);
        List<SleepAttemptResponse> responseDTOs =
                attempts.stream().map(sleepAttemptMapper::toResponse).toList();

        Map<String, Object> response = new HashMap<>();
        response.put("Message", "Successful sleep attempts listed.");
        response.put("Attempts", responseDTOs);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/unsuccessfully") // TODO: Verify the owner's ID
    public ResponseEntity<Map<String, Object>> findUnsuccessfullyAttemptsByUser(@PathVariable String id) {
        List<SleepAttempt> attempts = service.findUnsuccessfulAttemptsByUser(id);
        List<SleepAttemptResponse> responseDTOs =
                attempts.stream().map(sleepAttemptMapper::toResponse).toList();

        Map<String, Object> response = new HashMap<>();
        response.put("Message", "Successful sleep attempts listed.");
        response.put("Attempts", responseDTOs);

        return ResponseEntity.ok(response);
    }
}
