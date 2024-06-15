package com.example.samuraitravel.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.repository.FavoriteRepository;

@Service
public class FavoriteService {
	private final FavoriteRepository favoriteRepository;
	
	public FavoriteService( FavoriteRepository favoriteRepository) {
		this.favoriteRepository = favoriteRepository;
	}
	
	//お気に入りに追加
	@Transactional
	public void create(House house, User user) {
//		if(!favoriteRepository.existsByHouseAndUser(house, user)) {
//		if (!hasFavorite(house, user)) {
			Favorite favorite = new Favorite();
			favorite.setHouse(house);
			favorite.setUser(user);
			favoriteRepository.save(favorite);
		}
//	}
	
	public boolean hasFavorite(House house, User user) {
		return favoriteRepository.findByHouseAndUser(house, user) != null;
	}
	
	//お気に入りを解除(削除)
//	public void delete(House house, User user) {
//		Favorite favorite = favoriteRepository.findByHouseAndUser(house, user);
//		if(favorite != null) {
//			favoriteRepository.delete(favorite);
//		}
//	}
}

	
	
