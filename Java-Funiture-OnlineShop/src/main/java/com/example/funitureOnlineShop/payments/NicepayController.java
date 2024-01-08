package com.example.funitureOnlineShop.payments;

import com.example.funitureOnlineShop.core.error.exception.Exception500;
import com.example.funitureOnlineShop.option.OptionService;
import com.example.funitureOnlineShop.order.OrderResponse;
import com.example.funitureOnlineShop.order.OrderService;
import com.example.funitureOnlineShop.user.UserResponse;
import com.example.funitureOnlineShop.user.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/v1")
@RequiredArgsConstructor
@Slf4j
public class NicepayController {
    private final OrderService orderService;
    private final OptionService optionService;
    private final UserService userService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String CLIENT_ID = "S2_302df305816d49c2bbb1156e5a10527a";

    private final String SECRET_KEY = "c88edc6691ae4acfaaf0ad7e739581da";
    private final String uuid = UUID.randomUUID().toString();

    @RequestMapping("/{id}")
    public String indexDemo(@PathVariable Long id, Model model){
        OrderResponse.FindByIdDTO orderDto = orderService.findById(id);
        model.addAttribute("order", orderDto);

        UserResponse.UserDTO userDTO = userService.getUserInfo(orderDto.getUserId());
        model.addAttribute("user", userDTO);

        // UUID uuid = UUID.randomUUID();
        model.addAttribute("orderId", uuid + ":" + orderDto.getId());
        model.addAttribute("clientId", CLIENT_ID);
        return "payIndex";
    }

    @RequestMapping(value="/cancel")
    public String cancelDemo(){
        return "payCancel";
    }

    @PostMapping("/serverAuth")
    @Transactional
    public String requestPayment(
            @RequestParam String tid,
            @RequestParam Long amount,
            @RequestParam String orderId,
            Model model) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((CLIENT_ID + ":" + SECRET_KEY).getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> AuthenticationMap = new HashMap<>();
        AuthenticationMap.put("amount", String.valueOf(amount));
        System.out.println("Order Amount: " + amount);
        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(AuthenticationMap), headers);

        ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(
                "https://sandbox-api.nicepay.co.kr/v1/payments/" + tid, request, JsonNode.class);

        JsonNode responseNode = responseEntity.getBody();
        String resultCode = responseNode.get("resultCode").asText();
        model.addAttribute("resultMsg", responseNode.get("resultMsg").asText());

        System.out.println(responseNode.toPrettyString());

        if (resultCode.equalsIgnoreCase("0000")) {

            optionService.deductStock(orderId);
            orderService.delete(orderId, tid);
            // 기타 결제 성공 비즈니스 로직
            // (예시: 성공한 결제에 대한 로그 기록 등)
            // 결제 성공 시 오더 생성
        } else {
            throw new Exception500("잘못된 계산정보입니다");
        }
        //return "payResponse";
        return "payResponse";
    }

    @PostMapping("/cancelAuth")
    public String requestCancel(
            @RequestParam String tid,
            @RequestParam Long amount,
            @RequestParam String reason,
            @RequestParam String orderId,
            Model model) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((CLIENT_ID + ":" + SECRET_KEY).getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> AuthenticationMap = new HashMap<>();
        AuthenticationMap.put("amount", amount);
        AuthenticationMap.put("reason", reason);
        AuthenticationMap.put("orderId", orderId);

        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(AuthenticationMap), headers);

        ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(
                "https://sandbox-api.nicepay.co.kr/v1/payments/"+ tid +"/cancel", request, JsonNode.class);

        JsonNode responseNode = responseEntity.getBody();
        String resultCode = responseNode.get("resultCode").asText();
        model.addAttribute("resultMsg", responseNode.get("resultMsg").asText());

        System.out.println(responseNode.toPrettyString());

        if (resultCode.equalsIgnoreCase("0000")) {
            optionService.restoreStock(tid);
            orderService.cancelOrder(tid);
        } else {
            // 취소 실패 비즈니스 로직 구현
            throw new Exception500("결제 취소 중 오류 발생");
        }
        return "payResponse";
    }

    @RequestMapping("/hook")
    public ResponseEntity<String> hook(@RequestBody HashMap<String, Object> hookMap) throws Exception {
        String resultCode = hookMap.get("resultCode").toString();

        System.out.println(hookMap);

        if(resultCode.equalsIgnoreCase("0000")){
            return ResponseEntity.status(HttpStatus.OK).body("ok");
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}