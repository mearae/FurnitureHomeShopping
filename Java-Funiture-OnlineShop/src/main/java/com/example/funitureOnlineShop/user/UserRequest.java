package com.example.funitureOnlineShop.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collections;

public class UserRequest {

    @Setter
    @Getter
    public static class JoinDto {

        // 이메일
        // 영어+숫자 '@' 영어+숫자
        @NotEmpty
        @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "유효한 이메일 주소를 입력해주세요.")
        private String email;

        // 비밀번호
        // 영문자, 숫자, 특수문자를 조합한 8 ~ 20자
        @NotEmpty(message = "비밀번호를 입력해주세요.")
        @Size(min = 8, max = 20, message = "8자 이상 20자 이내로 작성 가능합니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#$%^&+=!@#$%^&*()_~+=\\[\\]|\\\\;:'\"<>,.?/-])[A-Za-z\\d@#$%^&+=!@#$%^&*()_~+=\\[\\]|\\\\;:'\"<>,.?/-]{8,20}$", message = "영문자, 숫자, 특수문자를 혼합하여 입력해주세요.")
        private String password;

        // 이름
        @NotEmpty
        private String username;

        // 전화번호
        // 10 ~ 11자리 숫자
        @NotEmpty
        @Pattern(regexp = "^[0-9]{10,11}$", message = "휴대폰 번호는 숫자 10~11자리만 가능합니다.")
        private String phoneNumber;

        // 배송 주소
        @NotEmpty
        private String address;

        // DTO를 Entity로
        public User toEntity(){
            return User.builder()
                    .email(email)
                    .password(password)
                    .username(username)
                    .phoneNumber(phoneNumber)
                    .address(address)
                    .roles(Collections.singletonList("ROLE_USER"))
                    .build();
        }
    }

    @Getter
    @Setter
    public static class LoginDto{
        // 이메일
        // 영어+숫자 '@' 영어+숫자
        @NotEmpty
        @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "유효한 이메일 주소를 입력해주세요.")
        private String email;

        // 비밀번호
        // 영문자, 숫자, 특수문자를 조합한 8 ~ 20자
        @NotEmpty(message = "비밀번호를 입력해주세요.")
        @Size(min = 8, max = 20, message = "8자 이상 20자 이내로 작성 가능합니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#$%^&+=!@#$%^&*()_~+=\\[\\]|\\\\;:'\"<>,.?/-])[A-Za-z\\d@#$%^&+=!@#$%^&*()_~+=\\[\\]|\\\\;:'\"<>,.?/-]{8,20}$", message = "영문자, 숫자, 특수문자를 혼합하여 입력해주세요.")
        private String password;

        // DTO를 Entity로
        public User toEntity(){
            return User.builder()
                    .email(email)
                    .password(password)
                    .build();
        }
    }
}
