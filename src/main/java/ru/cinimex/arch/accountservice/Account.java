package ru.cinimex.arch.accountservice;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Account extends ResourceSupport {
    @Id
    @JsonProperty("accountId")
    private Long accountId;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("category")
    private String category;


    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

 //   @JsonValue
    public String toJson(){
        //return this.accountId +"," + this.firstName + "," + this.lastName + "," + this.category;
        try {
            String result = new ObjectMapper().writeValueAsString(this);
            return  result;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }

    }


}
