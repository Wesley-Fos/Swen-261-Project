import {Component, Input} from '@angular/core';
import {Jersey} from "../jersey";
import {ActivatedRoute, Router} from "@angular/router";
import {JerseyService} from "../jersey.service";
import {Location} from "@angular/common";
import { LoginService } from '../login.service';
import {CartService} from "../cart.service";
import {Cart} from "../cart";
import { ValidatorService } from '../validator.service';

@Component({
  selector: 'app-jersey-detail',
  templateUrl: './jersey-detail.component.html',
  styleUrls: ['./jersey-detail.component.css']
})

// Component that shows details about a specific jersey. Allows admin to edit values here.
export class JerseyDetailComponent {
  @Input() jersey?: Jersey;
  shoppingCart?: Cart;
  currentUser: string = "";
  isAdmin?: boolean

  constructor(
    private route: ActivatedRoute,
    private jerseyService: JerseyService,
    private location: Location,
    private loginService: LoginService,
    private cartService: CartService,
    private validator: ValidatorService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.currentUser = this.loginService.latestLogin;
    this.getJersey();
    this.currentUser = this.loginService.latestLogin;
    this.getShoppingCart();
    this.isAdmin = this.loginService.isAdmin;
  }

  getJersey(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.jerseyService.getJersey(id).subscribe(jersey => this.jersey = jersey)
  }

  getShoppingCart(): void {
    this.cartService.searchCarts(this.currentUser).subscribe(cart => this.shoppingCart = cart)
  }

  deleteJersey(): void {
    if(confirm(`DELETING ${this.jersey?.playerName.toUpperCase()} JERSEY\nCLICK OK TO CONTINUE`)){
      const id = Number(this.route.snapshot.paramMap.get('id'))
      this.jerseyService.deleteJersey(id).subscribe()
      this.router.navigateByUrl("/home")
    }
  }

  goBack(): void {
    this.location.back();
  }

  save(): void{
    if(this.jersey && this.validator.validateJersey(this.jersey)){
      if(confirm("Save changes?")){
        this.jerseyService.updateJersey(this.jersey).subscribe()
      }
    }
  }

  deleteJerseyReview(jersey: Jersey, username : string, reviewRating : string, reviewText : string): void{
    if(confirm("Are you sure you want to delete this review?")) {
      jersey.reviews = jersey.reviews.filter(function(review){
        if(review[0] == username && review[1] == reviewRating && review[2] == reviewText){
          return false;
        }else{
          return true;
        }
      });

      jersey.avgReview = this.getAvg(jersey.reviews);

      this.jerseyService.updateJersey(jersey).subscribe(jersey => this.jersey = jersey);
    }
  }

  add(jersey: Jersey, username : string, reviewRating : string, reviewText : string): void{
    if(reviewText == null || reviewText == ''){
      alert("Error! To send a review you must fill in all the fields!");
    }
    else{
      if(confirm("Are you sure to create the review: \nRating: " + reviewRating + "\nReview: " + reviewText)) {
        jersey.reviews.push([username, reviewRating, reviewText]);
        jersey.avgReview = this.getAvg(jersey.reviews);

        this.jerseyService.updateJersey(jersey).subscribe(jersey => this.jersey = jersey);
      }
    }
  }

  getAvg(reviews: string[][]){
    var sum = 0;
      reviews.forEach( (element) => {
        sum += Number(element[1]);
      });

      return Math.round(sum / reviews.length * 2) / 2;
  }

  addToShoppingCart(): void {
    // get stuff from current cart
      // @ts-ignore
    let cartId = this.shoppingCart?.id;
      // @ts-ignore
    let cartList = this.shoppingCart?.Items;

    // get jersey id
    const jerseyId = Number(this.route.snapshot.paramMap.get('id'));

    // get jersey if jersey already exists in user's shopping cart
    let jerseyInCart: number[];
    let itemIndex: number = 0;
      // @ts-ignore
    for (let cartItem of cartList) {
      let jerseyIdInCart = cartItem[0];
      if (jerseyIdInCart === jerseyId) {
        jerseyInCart = cartItem;
        break;
      }
      itemIndex++;
    }

    // prompt for quantity
    let jerseyStock = this.jersey?.quantity;
    let intQuantity: number;
    let quantityAlreadyInCart: number;
      // @ts-ignore
    if (jerseyInCart) {
      quantityAlreadyInCart = jerseyInCart[1];
    }

    let quantityAcquired = false;
    // @ts-ignore
    while (!quantityAcquired) {
      let quantity = prompt("Enter in the amount of jerseys you wish to add to cart.", "1");

      // if user clicks cancel
      if (quantity === null) {
        return;

      } else {
        // @ts-ignore // checks if invalid value is entered
        intQuantity = parseInt(quantity);
        if (isNaN(intQuantity) || intQuantity === 0) {
          alert("Invalid value entered. Please enter in a valid amount.");
        }

        // @ts-ignore // checks if user inputs number greater than stock listed
        else if (intQuantity > jerseyStock) {
          alert(`Not enough stock! Please enter in a valid amount.`)
        }

        // @ts-ignore // checks if user already has entire stock in shopping cart
        else if (quantityAlreadyInCart !== undefined && intQuantity + quantityAlreadyInCart > jerseyStock) {
          alert("You can not add any more of this jersey to your shopping cart because you already put the entire stock in there.")
        }

        else {
          quantityAcquired = true;
        }
      }
    }

    // add jersey id to that cartList
    if (cartList) {
        // @ts-ignore
      if (jerseyInCart !== undefined) {
          // @ts-ignore
        jerseyInCart[1] += intQuantity;
          // @ts-ignore
        cartList[itemIndex] = jerseyInCart;

      } else {
          // @ts-ignore
        cartList.push([jerseyId, intQuantity]);
      }
    }

    else { // if cart is null/undefined
        // @ts-ignore
      cartList = [[jerseyId, intQuantity]]
    }

    // add updatedCartList to cart in server
    this.cartService.updateCart({id: cartId, customerUsername: this.currentUser, Items: cartList} as Cart).subscribe(cart => this.shoppingCart = cart);
  }
}
