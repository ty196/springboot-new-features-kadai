package com.example.samuraitravel.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReviewEditForm;
import com.example.samuraitravel.form.ReviewRegisterForm;
import com.example.samuraitravel.repository.ReviewRepository;


@Service
public class ReviewService {
	private final ReviewRepository reviewRepository;
	
	public ReviewService(ReviewRepository reviewRepository) {
		this.reviewRepository = reviewRepository;
		
	}
	
	// レビューをデータベースに保存
	@Transactional
	public void create(House house, User user, ReviewRegisterForm reviewRegisterForm) {
		Review review = new Review();
		
		review.setHouse(house);
		review.setUser(user);
		review.setScore(reviewRegisterForm.getScore());
		review.setComment(reviewRegisterForm.getComment());
		
		reviewRepository.save(review);
	}
	
	// レビューの更新を保存
	@Transactional
	public void update(ReviewEditForm reviewEditForm) {
		Review review = reviewRepository.getReferenceById(reviewEditForm.getId());
		
		review.setScore(reviewEditForm.getScore());
		review.setComment(reviewEditForm.getComment());
		
		reviewRepository.save(review);
	}
	
	public boolean hasUserAlreadyReviewed(House house, User user) {
        return reviewRepository.findByHouseAndUser(house, user) != null;
	}

}
