package br.com.nvnk.SleepTracking.controller;

import br.com.nvnk.SleepTracking.mapper.UserMapper;
import br.com.nvnk.SleepTracking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping
    public ResponseEntity<Map<String, Object>> describeUser (){
        Map<String, Object> response = new HashMap<>();
        response.put("User: ", UserMapper.toResponse(service.describeUser()));

        return ResponseEntity.ok(response);
    }

    @GetMapping("calculate/ideal-bedtime")
    public ResponseEntity<Map<String, Object>> calculateIdealBedtime (){
        Map<String, Object> response = new HashMap<>();
        response.put("Ideal bedtime: ", service.calculateIdealBedtime());

        return ResponseEntity.ok(response);
    }

}
