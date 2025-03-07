package ge.nika.springbootdemo.cars.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {

    private String username;
    private Long balanceInCents;
    private String balanceInDolars;
    private int numberOfOwnedCars;


    public UserDTO(String username, Long balanceInCents, int numberOfOwnedCars){
        this.username = username;
        this.balanceInCents = balanceInCents;
        this.numberOfOwnedCars = numberOfOwnedCars;
    }//wrote another constructor so that i can show balance in dollars

    public void setBalanceInDollars(){
        balanceInDolars = (double)(balanceInCents / 100) + "$";
    }
}
