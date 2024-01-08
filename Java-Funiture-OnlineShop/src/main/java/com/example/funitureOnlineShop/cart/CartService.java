package com.example.funitureOnlineShop.cart;

import com.example.funitureOnlineShop.core.error.exception.Exception401;
import com.example.funitureOnlineShop.core.error.exception.Exception403;
import com.example.funitureOnlineShop.core.error.exception.Exception404;
import com.example.funitureOnlineShop.core.error.exception.Exception500;
import com.example.funitureOnlineShop.option.Option;
import com.example.funitureOnlineShop.option.OptionRepository;
import com.example.funitureOnlineShop.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final OptionRepository optionRepository;

    public CartResponse.FindAllDto findAll() {
        List<Cart> cartList = cartRepository.findAll();
        return new CartResponse.FindAllDto(cartList);
    }


    // ** 카트에 상품 추가
    @Transactional
    public void addCart(CartRequest.SaveDTO saveDTOS, User user) {
        List<Cart> myCarts = cartRepository.findAllByUserId(user.getId());

        // ** 동일한 상품 예외처리 - 상품이 이미 추가 되었는지 확인하고 예외처리
        for (Cart cart : myCarts) {
            if (cart.getOption().getId().equals(saveDTOS.getOptionId()))
                throw new Exception401("이미 동일한 상품 옵션이 있습니다. " + saveDTOS.getOptionId());
        }

        // Option의 id값을 가져와서 비교 - 상품찾기
        Option option = optionRepository.findById(saveDTOS.getOptionId()).orElseThrow(
                () -> new Exception404("해당 상품 옵션을 찾을 수 없습니다. " + saveDTOS.getOptionId()) //없으면 예외처리
        );
        Cart cart = saveDTOS.toEntity(option, user);

        // 카트에 상품 저장
        try {
            cartRepository.save(cart);
        } catch (Exception e) {
            throw new Exception500("카트에 담던 중 오류가 발생했습니다." + e.getMessage());
        }
    }

    @Transactional
    public CartResponse.UpdateDTO update(CartRequest.UpdateDTO requestDTO, User user) {
        // 현재 사용자의 ID를 기반으로 카트에서 상품을 조회
        List<Cart> cartList = cartRepository.findAllByUserId(user.getId());

        // 존재하는 카트인지 확인
        Optional<Cart> optionalCart = cartRepository.findById(requestDTO.getCartId());
        if (optionalCart.isEmpty())
            throw new Exception404("존재하지 않는 장바구니 상품입니다. " + requestDTO.getCartId());

        // 카트에 상품이 없을 경우 404 예외
        if (cartList.isEmpty())
            throw new Exception404("수정 가능한 장바구니 상품이 없습니다.");

        Cart cart = null;

        // 내 장바구니에 존재하는 상품인지 확인
        for (Cart cartTemp : cartList) {
            if (cartTemp.getId().equals(requestDTO.getCartId())) {
                cart = optionalCart.get();
                break;
            }
        }
        if (cart == null)
            throw new Exception404("내 장바구니에 없는 상품입니다.");

        // 업데이트 요청에 따라 카트의 각 상품의 수량을 업데이트
        cart.update(requestDTO.getQuantity(),
                (cart.getOption().getProduct().getPrice() + cart.getOption().getPrice())
                        * requestDTO.getQuantity());

        // 이제 응답 받을 차례 - 객체 생성후 사용 가능
        return new CartResponse.UpdateDTO(cart);
    }


    @Transactional
    public void deleteCartList(List<CartResponse.DeleteDTO> deleteDTO, Long userId) {
        Set<Long> ids = new HashSet<>();
        for (CartResponse.DeleteDTO dto : deleteDTO) {
            Optional<Cart> optionalCart = cartRepository.findById(dto.getCartId());
            if (optionalCart.isEmpty())
                throw new Exception404("장바구니에 담겨있는 상품이 아닙니다." + dto.getCartId());
            Cart cart = optionalCart.get();
            if (!cart.getUser().getId().equals(userId))
                throw new Exception403("회원님의 장바구니에 존재하는 상품이 아닙니다." + dto.getCartId());
            ids.add(cart.getId());
        }

        for (Long id : ids) {
            cartRepository.deleteById(id);
        }
    }

    public CartResponse.FindAllDto findAllByUserId(Long id) {
        List<Cart> cartList = cartRepository.findAllByUserId(id);
        return new CartResponse.FindAllDto(cartList);
    }
}