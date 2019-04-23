package com.softserve.actent.controller;

import com.softserve.actent.constant.ExceptionMessages;
import com.softserve.actent.constant.UrlConstants;
import com.softserve.actent.exceptions.security.NotAuthorizedException;
import com.softserve.actent.model.dto.review.CreateReviewDto;
import com.softserve.actent.model.dto.review.ReviewDto;
import com.softserve.actent.model.dto.IdDto;
import com.softserve.actent.model.entity.Review;
import com.softserve.actent.security.annotation.CurrentUser;
import com.softserve.actent.security.model.UserPrincipal;
import com.softserve.actent.service.ReviewService;
import com.softserve.actent.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping(UrlConstants.API_V1)
@PreAuthorize("permitAll()")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public ReviewController(ReviewService reviewService, UserService userService, ModelMapper modelMapper) {
        this.reviewService = reviewService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping(value = "/reviews")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    public IdDto addReview(@ApiIgnore @CurrentUser UserPrincipal currentUser,
                           @Validated @RequestBody CreateReviewDto addReviewDto) {

        Review review = modelMapper.map(addReviewDto, Review.class);
        review.setAuthor(userService.get(currentUser.getId()));
        review = reviewService.add(review);

        return new IdDto(review.getId());
    }

    @GetMapping(value = "/reviews/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReviewDto getReview(@PathVariable @NotNull(message = ExceptionMessages.REVIEW_NO_ID)
                                   @Positive(message = ExceptionMessages.REVIEW_INAPPROPRIATE_ID) Long id) {

        Review review = reviewService.get(id);
        return modelMapper.map(review, ReviewDto.class);
    }

    @GetMapping(value = "/reviews")
    @ResponseStatus(HttpStatus.OK)
    public List<ReviewDto> getAllReviews() {

        List<Review> reviews = reviewService.getAll();
        return reviews.stream()
                .map(review -> modelMapper.map(review, ReviewDto.class))
                .collect(Collectors.toList());
    }

    @PutMapping(value = "/reviews/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public ReviewDto updateReview(@ApiIgnore @CurrentUser UserPrincipal currentUser,
                                  @Validated @RequestBody CreateReviewDto updateReviewDto,
                                  @PathVariable @NotNull(message = ExceptionMessages.REVIEW_NO_ID)
                                  @Positive(message = ExceptionMessages.REVIEW_INAPPROPRIATE_ID) Long id) {

        if(currentUser.getId().equals(reviewService.get(id).getAuthor().getId())) {
            Review review = reviewService.update(modelMapper.map(updateReviewDto, Review.class), id);
            return modelMapper.map(review, ReviewDto.class);
        } else {
            // TODO: throw forbidden exception
            return null;
        }
    }

    @DeleteMapping(value = "/reviews/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@ApiIgnore @CurrentUser UserPrincipal currentUser,
                               @PathVariable
                               @NotNull(message = ExceptionMessages.REVIEW_NO_ID)
                               @Positive(message = ExceptionMessages.REVIEW_INAPPROPRIATE_ID) Long id) {

        if(currentUser.getId().equals(reviewService.get(id).getAuthor().getId())) {
            reviewService.delete(id);
        } else {
            // TODO: throw forbidden exception
        }
    }
}
