package com.example.samuraitravel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReviewEditForm;
import com.example.samuraitravel.form.ReviewRegisterForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReviewRepository;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.ReviewService;

@Controller
@RequestMapping("/houses/{houseId}/review")
public class ReviewController {
	private final ReviewRepository reviewRepository;
	private final HouseRepository houseRepository;
	private final ReviewService reviewService;
	
	public ReviewController(ReviewRepository reviewRepository, HouseRepository houseRepository, ReviewService reviewService) {
		this.reviewRepository = reviewRepository;
		this.houseRepository = houseRepository;
		this.reviewService = reviewService;
	}
	
//	@GetMapping
//	public String index(Model model, @PageableDefault(page = 0, size = 6, sort = "id", direction = Direction.ASC)Pageable pageable) {
//		Page<Review> reviewPage = reviewRepository.findAll(pageable);
//		model.addAttribute("reviewPage",reviewPage);
//		return "houses/show";
//	}
	
	//レビュー一覧を表示
	@GetMapping("/table")
	public String table(@PathVariable(name = "houseId") Integer houseId, Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC)Pageable pageable) {
		 House house = houseRepository.getReferenceById(houseId);
         Page<Review> reviewPage = reviewRepository.findByHouseOrderByCreatedAtDesc(house, pageable); 
         
		model.addAttribute("house", house);
		model.addAttribute("reviewPage", reviewPage);
		return "review/table";
	}
	
	//レビュー投稿
	@GetMapping("/register")
	public String register(@PathVariable(name = "houseId") Integer houseId,Model model) {
		House house = houseRepository.getReferenceById(houseId);
		
		model.addAttribute("house", house);
		model.addAttribute("reviewRegisterForm", new ReviewRegisterForm());
		return "review/register";
	}
	
	//レビュー作成
	@PostMapping("/create")
	public String create(@ModelAttribute @Validated ReviewRegisterForm reviewRegisterForm, 
			             BindingResult bindingResult,
                         RedirectAttributes redirectAttributes, 
                         @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                         @PathVariable(name = "houseId") Integer houseId, Model model) {
		House house = houseRepository.getReferenceById(houseId);
        User user = userDetailsImpl.getUser();
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("house", house);
            return "reviews/register";
        }        
        
        reviewService.create(house, user, reviewRegisterForm);
        redirectAttributes.addFlashAttribute("successMessage", "レビューを投稿しました。");    
        
        return "redirect:/houses/{houseId}";
	}
	
	//レビュー編集
	@GetMapping("/{id}/edit")
	public String edit(@PathVariable(name = "id") Integer id, @PathVariable(name = "houseId") Integer houseId, Model model) {
		House house = houseRepository.getReferenceById(houseId);
		Review review = reviewRepository.getReferenceById(id);
		ReviewEditForm reviewEditForm = new ReviewEditForm(review.getId(),  review.getScore(), review.getComment());
		
		model.addAttribute("house", house);
		model.addAttribute("review", review);
		model.addAttribute("reviewEditForm", reviewEditForm);
		
		return "review/edit";
	}
	
	//レビュー更新
	@PostMapping("/{id}/update")
	public String update(@ModelAttribute @Validated ReviewEditForm reviewEditForm,
			             BindingResult bindingResult, 
			             RedirectAttributes redirectAttributes,
			             @PathVariable(name = "houseId") Integer houseId,
			             @PathVariable(name = "id") Integer id, Model model) {
		House house = houseRepository.getReferenceById(houseId);
        Review review = reviewRepository.getReferenceById(id);
        
		if(bindingResult.hasErrors()) {
			model.addAttribute("house", house);
            model.addAttribute("review", review);
			return "review/edit";
		}
		
		reviewService.update(reviewEditForm);
		redirectAttributes.addFlashAttribute("successMessage", "レビューを編集しました。");
		
		return "redirect:/houses/{houseId}";
	}
	
	//レビュー削除
	@PostMapping("/{id}/delete")
	public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
		reviewRepository.deleteById(id);
		
		redirectAttributes.addFlashAttribute("successMessage", "レビューを削除しました。");
		
		return "redirect:/houses/{houseId}";
	}

}
