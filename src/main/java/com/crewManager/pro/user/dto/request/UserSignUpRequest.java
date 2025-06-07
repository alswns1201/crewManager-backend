package com.crewManager.pro.user.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpRequest {

//    @NotBlank(message = "이메일은 필수 입력 값입니다.")
//    @Email(message = "이메일 형식이 올바르지 않습니다.")
//    private String email;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Size(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이하로 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]*$", message = "닉네임은 한글, 영문, 숫자만 가능합니다.")
    private String name;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
    private String password;

    @NotBlank(message = "핸드폰 번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^\\d{10,11}$", message = "올바른 핸드폰 번호 형식이 아닙니다.")
    private String phoneNumber;

    // 가입 유형: "ROLE_USER" 또는 "ROLE_CREW_ADMIN"
    @NotBlank(message = "가입 유형은 필수입니다.(일반,크루 생성)")
    @Pattern(regexp = "ROLE_USER|ROLE_CREW_ADMIN", message = "유효하지 않은 가입 유형입니다. (ROLE_USER, ROLE_CREW_ADMIN 중 하나)")
    private String userRole; // `User` 엔티티의 `Role` enum과 매핑될 String 값

}
