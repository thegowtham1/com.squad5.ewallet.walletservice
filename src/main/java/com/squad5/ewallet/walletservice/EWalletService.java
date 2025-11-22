package com.squad5.ewallet.walletservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EWalletService {

	public static void main(String[] args) {
		SpringApplication.run(EWalletService.class, args);
		System.out.println("Started");
	}

}
