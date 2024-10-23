import { Component } from '@angular/core';
import { LoginService } from '../login.service';
import { CartService } from '../cart.service';
import { Router } from '@angular/router';
import { Cart } from '../cart';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})

// This component is the page that handles a user interacting with the system to log in.
export class LoginComponent{
    carts: Cart[] = [] // local storage of shopping carts to easily match a username to a cart

    constructor(private loginService: LoginService, private cartService: CartService, private router: Router) {
      this.cartService.getCarts().subscribe(shoppingCarts => this.carts = shoppingCarts)
    }
  
    // takes the user input, checks if the cart exists, if not it creates and stores a new cart for the new customer, either way
    // it will log in the user
    onSubmit(userInput: string): void{
      let foundName: string = this.findCart(userInput);
      if(userInput.toLowerCase() == 'admin'){
        foundName = 'admin';
      }
      else if(foundName == ""){
        this.addNewCustomer(userInput);
        foundName = userInput;
      }
      this.loginService.login(foundName);
      this.router.navigateByUrl("/home");
    }

    findCart(username: string): string {
      for(let cart of this.carts){
        if(cart.customerUsername.toLowerCase() == username.toLowerCase()){
          return cart.customerUsername;
        }
      }
      return "";
    }

    addNewCustomer(username: string): void {
      username = username.trim();
      if (!username) {return;}
      this.cartService.createCart({id: 0, customerUsername: username, Items: []} as Cart).subscribe(cart => this.carts.push(cart));
    }
}
