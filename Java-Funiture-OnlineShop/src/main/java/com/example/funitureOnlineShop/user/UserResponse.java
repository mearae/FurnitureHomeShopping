package com.example.funitureOnlineShop.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class UserResponse {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class UserDTO {
        // 이메일 (아이디)
        private String email;
        // 비밀번호
        private String password;
        // 회원명
        private String username;
        // 전화번호
        private String phoneNumber;
        // 주소
        private String address;
        // 권한
        private List<String> roles;

        public static UserDTO toUserDto(User user) {
            return new UserDTO(user.getEmail(),
                    user.getPassword(),
                    user.getUsername(),
                    user.getPhoneNumber(),
                    user.getAddress(),
                    user.getRoles());
        }
    }
}
