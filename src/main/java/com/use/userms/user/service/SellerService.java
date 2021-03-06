package com.use.userms.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.use.userms.user.dto.LoginDTO;
import com.use.userms.user.dto.SellerDTO;
import com.use.userms.user.entity.Seller;
import com.use.userms.user.exception.InfyMarketException;
import com.use.userms.user.repository.SellerRepository;

@Service
@Transactional
public class SellerService {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	SellerRepository sellerrepo;

	// Seller register
	public void saveSeller(SellerDTO sellerDTO) throws InfyMarketException {
		logger.info("Registration request for seller with data {}", sellerDTO);
		Seller seller = sellerDTO.createSeller();
		sellerrepo.save(seller);
	}

	// Get all sellers
	public List<SellerDTO> getAllSeller() throws InfyMarketException {

		Iterable<Seller> sellers = sellerrepo.findAll();
		List<SellerDTO> sellerDTOs = new ArrayList<>();

		sellers.forEach(seller -> {
			SellerDTO sellerDTO = SellerDTO.valueOf(seller);
			sellerDTOs.add(sellerDTO);
		});
		if (sellerDTOs.isEmpty())
			throw new InfyMarketException("Service.SELLERS_NOT_FOUND");
		logger.info("Seller Details : {}", sellerDTOs);
		return sellerDTOs;
	}

	// Seller Login
	public boolean login(LoginDTO loginDTO) throws InfyMarketException {
		Seller sell = sellerrepo.findByEmail(loginDTO.getEmail());
		if (sell != null && sell.getPassword().equals(loginDTO.getPassword())) {
			return true;
		} else {
			throw new InfyMarketException("Service.DETAILS_NOT_FOUND");
		}
	}

	// Delete Seller
	public void deleteSeller(String sellerid) throws InfyMarketException {
		if(sellerid!=null) {
		Optional<Seller> seller = sellerrepo.findById(sellerid);
		
		sellerrepo.deleteById(sellerid);
		}
		else {
			throw new InfyMarketException("Service.SELLERS_NOT_FOUND");
		}
	}
}
