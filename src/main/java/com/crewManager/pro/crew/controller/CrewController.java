package com.crewManager.pro.crew.controller;

import com.crewManager.pro.crew.domain.Crew;
import com.crewManager.pro.crew.dto.CrewResponseDto;
import com.crewManager.pro.crew.service.CrewService; // CrewService를 import 합니다.
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity; // ResponseEntity를 사용합니다.
import org.springframework.web.bind.annotation.GetMapping; // @GetMapping을 사용합니다.
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/crew")
public class CrewController {

    // final 키워드와 @RequiredArgsConstructor를 통해 CrewService를 의존성 주입받습니다.
    private final CrewService crewService;

    /**
     * 모든 크루 목록을 조회하는 API
     * HTTP Method: GET
     * Endpoint: /api/crew/all
     * @return 성공 시 HTTP 200 OK 상태와 함께 크루 목록(List<Crew>)을 반환합니다.
     */
    @GetMapping("/all")
    public ResponseEntity<List<CrewResponseDto>> findAllCrews() {
        // 실제 데이터 조회 로직은 Service 계층에 위임합니다.
        List<CrewResponseDto> allCrews = crewService.findAll();

        // 조회된 데이터를 ResponseEntity.ok()로 감싸서 반환합니다.
        // 이는 클라이언트에게 성공(200 OK) 상태와 데이터를 함께 전달하는 좋은 방법입니다.
        return ResponseEntity.ok(allCrews);
    }
}