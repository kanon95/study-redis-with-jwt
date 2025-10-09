package com.example.studyrediswithjwt.controller;

import com.example.studyrediswithjwt.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/redis")
@RequiredArgsConstructor
public class RedisController {
    private final RedisService redisService;
    @PostMapping("/set")
    public String set(@RequestParam String key, @RequestParam  String value) {
        redisService.set(key, value);
        return "set: " + key + " -> " + value;
    }
    @RequestMapping("/get")
    public Object get(@RequestParam String key) {
        return redisService.get(key);
    }
    @DeleteMapping("/delete")
    public String delete(@RequestParam String  key) {
        redisService.delete(key);
        return "deleted: " + key;
    }
}
