package com.example.funitureOnlineShop.user;

import com.example.funitureOnlineShop.core.security.CustomUserDetails;
import com.example.funitureOnlineShop.core.security.JwtTokenProvider;
import com.example.funitureOnlineShop.core.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    // 유저 회원가입
    @PostMapping("/join")
    public ResponseEntity<Object> join(@RequestBody @Valid UserRequest.JoinDto joinDto, Error error){
        userService.join(joinDto);

        return ResponseEntity.ok(ApiUtils.success(null));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid UserRequest.LoginDto loginDto, HttpServletResponse res, Error error){
        String jwt = userService.login(loginDto, res);
        return ResponseEntity.ok().header(JwtTokenProvider.HEADER, jwt)
                .body(ApiUtils.success(null));
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Object> logout(@AuthenticationPrincipal CustomUserDetails customUserDetails, HttpServletResponse res, Error error){
        userService.logout(customUserDetails.getUser().getId(), res);
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    // 회원정보확인
    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        UserResponse.UserDTO userDTO = userService.getUserInfo(customUserDetails.getUser().getId());
        return ResponseEntity.ok(ApiUtils.success(userDTO));
    }

    // 회원탈퇴
    @PostMapping("/delete")
    public ResponseEntity<?> deleteById(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        userService.deleteUserById(customUserDetails.getUser().getId());
        return ResponseEntity.ok(ApiUtils.success(null));
    }
}
