package com.withdraw.proxy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.withdraw.entity.Account;
import com.withdraw.entity.User;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@FeignClient(name="banking-service")
public interface AccountProxy {
	
	@Retry(name = "banking-service")
    @CircuitBreaker(name = "banking-service", fallbackMethod = "fallbackMethodGetAllAccounts")
    @GetMapping("/getAllAccounts")
    List<Account> getAllAccounts();
	
	default List<Account> fallbackMethodGetAllAccounts(Throwable cause) {
        System.out.println("Exception raised with message: " + cause.getMessage());
        return new ArrayList<>();
    }


	
	@Retry(name="banking-service")
    @CircuitBreaker(name="banking-service", fallbackMethod = "fallbackforGetById")
	@GetMapping("get-Account/{id}")
	public Account getById(@PathVariable int id);
	
	
	default Account fallbackforGetById(int id,Throwable cause) {
    	System.out.println("Exception raised due to "+cause.getMessage());
    	User user = new User(1,"Dhoni","Ms","9887665444","dhoni@gmail.com","dhoni12","dhoni12");
	    return new Account(id,"accountype",(new BigDecimal("200")),user);
	}
	
	
	
	@Retry(name="banking-service")
    @CircuitBreaker(name="banking-service", fallbackMethod = "fallbackcreateAccount")
	@PostMapping("createAccount")
	public Account newAccount (@RequestBody Account account );

	default Account fallbackcreateAccount(Account account,Throwable cause) {
    	System.out.println("Exception raised due to "+cause.getMessage());
    	User user = new User(1,"Dhoni","Ms","9887665444","dhoni@gmail.com","dhoni12","dhoni12");
    	return new Account(0,"fallbackcreateAccount",(new BigDecimal("200")),user);
	}
	
	@Retry(name="banking-service")
    @CircuitBreaker(name="banking-service", fallbackMethod = "fallbackWithdraw")
	@GetMapping("withdraw/{id}/{balance}") 
	public Account withdraw(@PathVariable int id, @PathVariable BigDecimal balance);
	
	default Account fallbackWithdraw(int id,BigDecimal balance,Throwable cause) {
    	System.out.println("Exception raised due to "+cause.getMessage()); 
    	User user = new User(1,"Dhoni","Ms","9887665444","dhoni@gmail.com","dhoni12","dhoni12");
    	return new Account(id,"withdraw",(new BigDecimal("200")),user);
	}
}

