package com.yas.rating.service;

import com.yas.rating.RatingApplication;
import com.yas.rating.model.Rating;
import com.yas.rating.repository.RatingRepository;
import com.yas.rating.viewmodel.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = RatingApplication.class)
class RatingServiceTest {
    @Autowired
    private RatingRepository ratingRepository;
    @MockBean
    private CustomerService customerService;
    @MockBean
    private OrderService orderService;
    @Autowired
    private RatingService ratingService;
    private List<Rating> ratingList;
    private String userId = "user1";

    @BeforeEach
    void setUp() {
        ratingList = List.of(
                Rating.builder()
                        .content("comment 1")
                        .ratingStar(4)
                        .productId(1L)
                        .productName("product1")
                        .firstName("Duy")
                        .lastName("Nguyen")
                        .build(),
                Rating.builder()
                        .content("comment 2")
                        .ratingStar(2)
                        .productId(1L)
                        .productName("product1")
                        .firstName("Hai")
                        .lastName("Le")
                        .build(),
                Rating.builder()
                        .content("comment 3")
                        .ratingStar(3)
                        .productId(2L)
                        .productName("product2")
                        .firstName("Cuong")
                        .lastName("Tran")
                        .build()
        );
        ratingRepository.saveAll(ratingList);

    }

    @AfterEach
    void tearDown() {
        ratingRepository.deleteAll();
    }

    @Test
    void getRatingList_ValidProductId_ShouldSuccess() {
        int totalPage = 1;
        int pageNo = 0;
        int pageSize = 10;

        RatingListVm actualResponse = ratingService.getRatingListByProductId(1L, pageNo, pageSize);
        assertEquals(totalPage, actualResponse.totalPages());
        assertEquals(2, actualResponse.totalElements());
        assertEquals(2, actualResponse.ratingList().size());
    }

    @Test
    void getRatingListWithFilter_ValidFilterData_ShouldReturnSuccess() {
        String proName = "product2";
        String firstName = "Cuong";
        String lastName = "Tran";
        String cusName = firstName + " " + lastName;
        String message = "comment 3";
        ZonedDateTime createdFrom = ZonedDateTime.now().minusDays(30);
        ZonedDateTime createdTo = ZonedDateTime.now().plusDays(30);
        int totalPage = 1;
        int pageNo = 0;
        int pageSize = 10;
        RatingListVm actualResponse = ratingService.getRatingListWithFilter(proName, cusName, message, createdFrom, createdTo, pageNo, pageSize);
        assertEquals(totalPage, actualResponse.totalPages());
        assertEquals(1, actualResponse.totalElements());
        assertEquals(proName, actualResponse.ratingList().get(0).productName());
        assertEquals(message, actualResponse.ratingList().get(0).content());
        assertEquals(firstName, actualResponse.ratingList().get(0).firstName());
        assertEquals(lastName, actualResponse.ratingList().get(0).lastName());
    }

    @Test
    void createRating_ValidRatingData_ShouldSuccess() {
        Jwt jwt = mock(Jwt.class);
        JwtAuthenticationToken authentication = mock(JwtAuthenticationToken.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getToken()).thenReturn(jwt);
        when(authentication.getName()).thenReturn(userId);
        when(jwt.getSubject()).thenReturn(userId);
        when(orderService.checkOrderExistsByProductAndUserWithStatus(anyLong())).thenReturn(new OrderExistsByProductAndUserGetVm(true));
        when(customerService.getCustomer()).thenReturn(new CustomerVm(userId, null, "Cuong", "Tran"));

        RatingPostVm ratingPostVm = RatingPostVm.builder().content("comment 4").productName("product3").star(4).productId(3L).build();
        RatingVm ratingVm = ratingService.createRating(ratingPostVm);
        assertEquals(ratingPostVm.productName(), ratingVm.productName());
        assertEquals(ratingPostVm.content(), ratingVm.content());
        assertEquals(ratingPostVm.star(), ratingVm.star());
    }

    @Test
    void deleteRating_ValidRatingId_ShouldSuccess() {
        Long id = ratingRepository.findAll().get(0).getId();
        ratingService.deleteRating(id);
        Optional<Rating> rating = ratingRepository.findById(id);
        assertFalse(rating.isPresent());
    }

    @Test
    void calculateAverageStar_ValidProductId_ShouldSuccess() {
        Double averageStar = ratingService.calculateAverageStar(1L);
        assertEquals(3, averageStar);
    }
}
