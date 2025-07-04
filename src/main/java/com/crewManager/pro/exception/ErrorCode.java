package com.crewManager.pro.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 회원을 찾을 수 없습니다."),
    CREW_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 크루를 찾을 수 없습니다."),
    CREW_MEMBER_DUPLICATED(HttpStatus.CONFLICT, "이미 가입한 멤버 입니다."),
    CREW_NAME_DUPLICATED(HttpStatus.CONFLICT, "이미 존재하는 크루 이름입니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다.");

    private final HttpStatus status;
    private final String message;
}
