package ru.cinimex.arch.accountservice;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.NoSuchElementException;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;


@RestController
@RequestMapping("/account")
public class AccountService {
    //логгер
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private final AccountRepo repo;

    AccountService(AccountRepo repo){
        this.repo = repo;
    }


    @GetMapping("/{id}")
    public Account getAccount(@PathVariable Long id){
        return repo.findById(id)
                .orElseThrow(()->new NoSuchElementException());
    }

    @PostMapping
    public Account createAccount(@RequestBody Account account){
        Account saved = repo.save(account);
        logger.info("{\"event\":\"createAccount\",\"account\": {}", account.toJson());
        account.add(linkTo(methodOn(AccountService.class).getAccount(account.getAccountId())).withSelfRel());
        return saved;
    }

    @PutMapping("/{id}")
    public Account changeAccount(@PathVariable Long id, @RequestBody Account newAccount){

        logger.info("{\"event\":\"changeAccount\",\"account\": {}", newAccount.toJson());
        return repo.findById(id)
                .map(account ->{
                    account.setAccountId(id);
                    account.setCategory(newAccount.getCategory());
                    account.setFirstName(newAccount.getFirstName());
                    account.setLastName(newAccount.getLastName());
                    return repo.save(account);
                })
                .orElseGet(()->{
                    newAccount.setAccountId(id);
                    return repo.save(newAccount);
                });
    }

    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable Long id){
        logger.info("{\"event\":\"deleteAccount\",\"accountId\": {}", id);
        repo.deleteById(id);
    }


    @RequestMapping("/hello")
    public String hello(HttpServletResponse response){

        logger.info("service requested");
        String answer = "";

        //пробуем выполнить бизнес-критичную операцию
        try {
            Thread.sleep((long)(Math.random() * 1000));
            answer = "ok";
            //todo: реализовать синхронный appender для kafka с пробросом exception
  //          syncLogger.info("business critical event");
        }
        catch (Exception e)
        {
            logger.error("fault",e);
            //откат транзакции
            answer="fault. transaction rollback";
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        return answer;

    }
}
