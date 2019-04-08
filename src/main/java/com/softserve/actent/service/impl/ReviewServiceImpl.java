package com.softserve.actent.service.impl;

import com.softserve.actent.constant.ExceptionMessages;
import com.softserve.actent.exceptions.DataNotFoundException;
import com.softserve.actent.exceptions.codes.ExceptionCode;
import com.softserve.actent.model.entity.Review;
import com.softserve.actent.repository.ReviewRepository;
import com.softserve.actent.service.ReviewService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    @Transactional
    public Review add(Review review) {

        return reviewRepository.save(review);
    }

    @Override
    public Review get(Long reviewId) {

        Optional<Review> optionalReview = reviewRepository.findById(reviewId);

        return optionalReview.orElseThrow(() -> {
            log.error(ExceptionMessages.REVIEW_NOT_FOUND_WITH_ID + " Id: " + reviewId);
            return new DataNotFoundException(ExceptionMessages.REVIEW_NOT_FOUND_WITH_ID, ExceptionCode.NOT_FOUND);
        });
    }

    @Override
    public List<Review> getAll() {

        return reviewRepository.findAll();
    }

    @Override
    @Transactional
    public Review update(Review review, Long reviewId) {

        Optional<Review> optionalReview = reviewRepository.findById(reviewId);

        if (optionalReview.isPresent()) {
            review.setId(reviewId);
            return reviewRepository.save(review);
        } else {
            log.error(ExceptionMessages.REVIEW_NOT_FOUND_WITH_ID + " Id: " + reviewId);
            throw new DataNotFoundException(ExceptionMessages.REVIEW_NOT_FOUND_WITH_ID, ExceptionCode.NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public void delete(Long reviewId) {

        Optional<Review> optionalReview = reviewRepository.findById(reviewId);

        if (optionalReview.isPresent()) {
            reviewRepository.deleteById(reviewId);
        } else {
            log.error(ExceptionMessages.REVIEW_NOT_FOUND_WITH_ID + " Id: " + reviewId);
            throw new DataNotFoundException(ExceptionMessages.REVIEW_NOT_FOUND_WITH_ID, ExceptionCode.NOT_FOUND);
        }
    }
}
