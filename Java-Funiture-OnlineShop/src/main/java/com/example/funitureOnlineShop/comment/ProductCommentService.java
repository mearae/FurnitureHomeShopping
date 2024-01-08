package com.example.funitureOnlineShop.comment;

import com.example.funitureOnlineShop.commentFile.CommentFile;
import com.example.funitureOnlineShop.commentFile.CommentFileDto;
import com.example.funitureOnlineShop.commentFile.CommentFileRepository;
import com.example.funitureOnlineShop.core.error.exception.Exception400;
import com.example.funitureOnlineShop.core.error.exception.Exception401;
import com.example.funitureOnlineShop.core.error.exception.Exception404;
import com.example.funitureOnlineShop.core.error.exception.Exception500;
import com.example.funitureOnlineShop.option.OptionRepository;
import com.example.funitureOnlineShop.orderCheck.OrderCheck;
import com.example.funitureOnlineShop.orderCheck.OrderCheckDto;
import com.example.funitureOnlineShop.orderCheck.OrderCheckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ProductCommentService {

    private final ProductCommentRepository productCommentRepository;
    private final CommentFileRepository commentFileRepository;
    private final OrderCheckRepository orderCheckRepository;

    // 파일 저장 경로
    private String filePath = "C:/shoppingFiles/";
    private final List<String> isImage = new ArrayList<>(Arrays.asList(
            ".tiff", ".jfif", ".bmp", ".gif", ".svg", ".png", ".jpeg",
            ".svgz", ".webp", ".jpg", ".ico", ".xbm", ".dib", ".pjp",
            ".apng", ".tif", ".pjpeg", "avif"));

    // 상품 후기 저장
    @Transactional
    public ProductComment save(ProductCommentRequest.SaveDto saveDto,
                               MultipartFile[] files,
                               Long userId) throws IOException {
        Optional<OrderCheck> optionalOrderCheck = orderCheckRepository.findById(saveDto.getOrderCheckId());
        // 존재하지 않는 주문 내역일 경우
        if (optionalOrderCheck.isEmpty())
            throw new Exception404("해당 주문 내역을 찾을 수 없습니다. : " + saveDto.getOrderCheckId());
        OrderCheck orderCheck = optionalOrderCheck.get();
        // 값 로딩 맞추기
        //Hibernate.initialize(orderCheck);

        if (!orderCheck.getUser().getId().equals(userId))
            throw new Exception401("해당 상품의 후기을 작성할 권한이 없습니다.");

        // 작성 시간 넣기
        saveDto.setCreateTime(LocalDateTime.now());
        // 저장할 엔티티 생성
        ProductComment comment = saveDto.toEntity();
        try {
            ProductComment savedComment = productCommentRepository.save(comment);
            savedComment.updateFromEntity(orderCheck);

            // 파일 추가
            saveFiles(files, savedComment);

            return savedComment;
        } catch (Exception e) {
            throw new Exception500("상품 후기 저장 도중 오류 발생");
        }
    }

    @Transactional
    public void saveFiles(MultipartFile[] files, ProductComment productComment) throws IOException {
        if (!files[0].isEmpty()) {
            Path uploadPath = Paths.get(filePath);

            // 만약 경로가 없다면... 경로 생성
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            for (MultipartFile file : files) {
                // 파일명 추출
                String originalFilename = file.getOriginalFilename();

                // 확장자 추출
                String formatType = originalFilename.substring(
                        originalFilename.lastIndexOf("."));

                if (!isImage.contains(formatType))
                    throw new Exception400("이미지 파일만 가능합니다.");

                // UUID 생성
                String uuid = UUID.randomUUID().toString();

                // 경로 지정
                String path = filePath + uuid + originalFilename;

                // 파일을 물리적으로 저장 (DB에 저장 X)
                file.transferTo( new File(path) );

                CommentFile commentFile = CommentFile.builder()
                        .filePath(filePath)
                        .fileName(originalFilename)
                        .uuid(uuid)
                        .fileType(formatType)
                        .fileSize(file.getSize())
                        .productComment(productComment)
                        .build();

                commentFileRepository.save(commentFile);
            }
        }
    }

    // 상품의 상품 후기들을 탐색
    public List<ProductCommentResponse.CommentDto> commentList(Long pId) {
        try {
            List<ProductCommentResponse.CommentDto> commentDtos = new ArrayList<>();
            List<ProductComment> comments = productCommentRepository.findAll();
            // 상품 후기가 하나도 없을 경우
            if (comments.isEmpty())
                return null;
            // 각 옵션의 후기들을 수집
            for (ProductComment comment : comments) {
                List<CommentFile> commentFile = commentFileRepository.findAllByProductCommentId(comment.getId());
                if (commentFile.isEmpty())
                    commentFile.add(new CommentFile());
                // 상품 후기를 dto로 변환
                ProductCommentResponse.CommentDto commentDto = ProductCommentResponse.CommentDto.toDto(comment, commentFile);
                // 상품에 대한 후기일 경우 추가
                if (commentDto.getProductId().equals(pId)) {
                    commentDtos.add(commentDto);
                }
            }

            // 작성일 기준 최신순으로 정렬
            ProductCommentResponse.CommentDto.sortByCreateDate(commentDtos);

            return commentDtos;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception500("상품 후기 탐색 중 오류 발생 : " + pId);
        }
    }

    // 상품 후기 삭제
    @Transactional
    public void isDeletable(Long id, Long userId) {
        // 삭제할 상품 후기 탐색
        Optional<ProductComment> optionalProductComment = productCommentRepository.findById(id);
        // 상품 후기 존재 x
        if (optionalProductComment.isEmpty())
            throw new Exception404("해당 상품 후기를 찾을 수 없습니다. : " + id);
        ProductComment productComment = optionalProductComment.get();

        // 상품 후기 삭제 권한 확인 (작성자만 삭제 가능)
        if (!productComment.getOrderCheck().getUser().getId().equals(userId))
            throw new Exception401("해당 상품 후기을 삭제할 권한이 없습니다.");
    }

    @Transactional
    public void delete(Long id) {
        try {
            productCommentRepository.deleteById(id);
        } catch (Exception e) {
            throw new Exception500("상품 후기 삭제 도중 이상이 생겼습니다." + id);
        }
    }

    // 상품 후기 수정
    @Transactional
    public ProductComment update(ProductCommentRequest.UpdateDto updateDto, MultipartFile[] files, Long userId) {
        // 수정할 상품 후기 탐색
        Optional<ProductComment> optionalProductComment =
                productCommentRepository.findById(updateDto.getId());
        // 상품 후기 존재 x
        if (optionalProductComment.isEmpty())
            throw new Exception404("해당 상품 후기를 찾을 수 없습니다. : " + updateDto.getId());
        ProductComment productComment = optionalProductComment.get();

        // 상품 후기 삭제 권한 확인 (작성자만 수정 가능)
        if (!productComment.getOrderCheck().getUser().getId().equals(userId))
            throw new Exception401("해당 상품 후기을 수정할 권한이 없습니다.");

        try {
            // 내용, 별점, 수정일 수정
            productComment.updateFromDto(updateDto);
            productCommentRepository.save(productComment);

            // 파일 재설정
            commentFileRepository.deleteByProductComment_id(productComment.getId());
            saveFiles(files, productComment);
            return productComment;
        } catch (Exception e) {
            throw new Exception500("상품 후기 수정 도중 이상이 생겼습니다." + updateDto.getId());
        }
    }

    // 상품 후기 단일 탐색
    public ProductCommentResponse.CommentDto findById(Long id) {
        // 상품 후기 존재?
        Optional<ProductComment> optionalProductComment =
                productCommentRepository.findById(id);
        if (optionalProductComment.isEmpty())
            throw new Exception404("해당 상품 후기를 찾을 수 없습니다. : " + id);
        ProductComment comment = optionalProductComment.get();
        List<CommentFile> files = commentFileRepository.findAllByProductCommentId(id);

        return ProductCommentResponse.CommentDto.toDto(comment, files);
    }

    // 주문 내역 단체 탐색
    public List<OrderCheckDto> findOrderChecks(Long userId) {
        List<OrderCheck> orderCheckList = orderCheckRepository.findAllByUserId(userId);
        if (orderCheckList.isEmpty())
            return null;
        OrderCheck.sortByCreateDate(orderCheckList);

        List<OrderCheckDto> orderCheckDtos = new ArrayList<>();
        if (orderCheckList.isEmpty()) {
            orderCheckDtos.add(new OrderCheckDto());
            return orderCheckDtos;
        }
        for (OrderCheck orderCheck : orderCheckList) {
            Optional<ProductComment> optionalComment = productCommentRepository.findByOrderCheckId(orderCheck.getId());
            if (orderCheck.getOrderDate().plusYears(3L).isAfter(LocalDateTime.now())) {
                if (optionalComment.isEmpty())
                    orderCheckDtos.add(OrderCheckDto.toOrderCheckDto(orderCheck, null));
                else
                    orderCheckDtos.add(OrderCheckDto.toOrderCheckDto(orderCheck, optionalComment.get().getId()));
            }
        }

        return orderCheckDtos;
    }

    // 주문 내역 단일 탐색
    public OrderCheckDto findOrderCheck(Long id) {
        Optional<OrderCheck> optionalOrderCheck = orderCheckRepository.findById(id);
        if (optionalOrderCheck.isEmpty())
            throw new Exception404("해당 주문 내역을 찾을 수 없습니다.");

        return OrderCheckDto.toOrderCheckDto(optionalOrderCheck.get(), null);
    }

    public CommentFileDto findByIdFile(Long id) {

        Optional<CommentFile> optionalFile = commentFileRepository.findById(id);

        if (optionalFile.isEmpty())
            throw new Exception404("해당 파일을 찾을 수 없습니다." + id);

        CommentFile file = optionalFile.get();

        return CommentFileDto.toFileDto(file);
    }
}
