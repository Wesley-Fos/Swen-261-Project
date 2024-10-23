import { Component } from '@angular/core';
import { CartService } from '../cart.service';
import { LoginService } from '../login.service';
import { Jersey } from '../jersey';
import { Router } from '@angular/router';
import { JerseyService } from '../jersey.service';
import { Cart } from '../cart';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})

export class CartComponent {
  _jerseys: Jersey[] = [];
  _user : string = "";
  _items : number[][] = [[0,0]];
  _cart : Cart= {id:0,customerUsername:"", Items:[[0,0]]};
  _currentJersey : Jersey = {id: 0, playerName: "",playerNumber: 0, color: "", quantity: 0,
    size: "", imagePath: "", price:-1, reviews: [], avgReview:0};
  _display : String = ``;
  _price : number=0;

  constructor(private cartService: CartService,
    private login: LoginService,
    private router: Router,
    public jerseyService: JerseyService){
      this._user = this.login.latestLogin;
      this.getCartInfo();
    }

  getCartInfo(): void {
    if(this._user == null || this._user == "") this.router.navigateByUrl("/login");

    this.cartService.searchCarts(this._user).subscribe(item => this._cart = item);
    this.cartService.searchCarts(this._user).subscribe(item => this._items = item.Items);

    this.cartService.searchCarts(this._user).subscribe(item => {for(let element of item.Items){
      let quantityInCart = element[1];
      this.jerseyService.getJersey(element[0])
        .subscribe(val => {this._jerseys.push(val);
          this._price += quantityInCart * val.price});
    }});
  }

  get jerseyNums()
  {
    return this._items;
  }

  get user()
  {
    return this._user;
  }

  get jerseysInfo()
  {
    return this._jerseys;
  }

  get display()
  {
    return this._display;
  }

  updateItemQuantity(jersey: Jersey): void {

    // get stuff from current cart
      // @ts-ignore
    let cartId = this._cart.id;
      // @ts-ignore
    let cartList = this._cart.Items;

    // get jersey if jersey already exists in user's shopping cart
    let jerseyInCart: number[];
    let itemIndex: number = 0;
      // @ts-ignore
    for (let cartItem of cartList) {
      let jerseyIdInCart = cartItem[0];
      if (jerseyIdInCart === jersey.id) {
        jerseyInCart = cartItem;
        break;
      }
      itemIndex++;
    }

    // prompt for quantity
      // @ts-ignore
    let jerseyStock = jersey.quantity;
    let intQuantity: number;
    let quantityAlreadyInCart: number;
    // @ts-ignore
    if (jerseyInCart) {
      quantityAlreadyInCart = jerseyInCart[1];
    }

    let quantityAcquired = false;
      // @ts-ignore
    while (!quantityAcquired) {
        // @ts-ignore
      let quantity = prompt("Change the amount of jerseys you wish to have in your cart.", `${quantityAlreadyInCart}`);

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
        else if (intQuantity > quantityAlreadyInCart && intQuantity > jerseyStock) {
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
        jerseyInCart[1] = intQuantity;
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
    this.cartService.updateCart({id: cartId, customerUsername: this._user, Items: cartList} as Cart).subscribe(cart => this._cart = cart);

    // update front end
    this._items = cartList;
    this._price = 0
    for (let jersey of this._jerseys) {
      let jerseyPrice = jersey.price;
      let cartQuantity: number;
      for (let cartItem of this._items) {
        if (cartItem[0] === jersey.id) {
          cartQuantity = cartItem[1];
        }
      }

        // @ts-ignore
      this._price += cartQuantity * jerseyPrice;
    }
  }

  removeFromCart(jerseyId: number): void {
      // get verification from user
      if (confirm("Are you sure you want to remove this item from your shopping cart?")) {
        // get copy of cart and items in cart
        let userCart = this._cart;
        let itemsInCart = userCart.Items;

        // iterate over items in cart to find jersey id and splice it from array
        let index = 0;
        for (let item of itemsInCart) {
          if (item[0] === jerseyId) {
            itemsInCart.splice(index, 1);
            break;

          } else {
            index++;
          }
        }

        // update backend and component
        this._price = 0;
        let newJerseyInfo: Jersey[] = [];
        userCart.Items = itemsInCart;
        this.cartService.updateCart(userCart).subscribe(cart => this._cart = cart);

        for (let item of itemsInCart) {
          this.jerseyService.getJersey(item[0]).subscribe(jersey => {newJerseyInfo.push(jersey); this._price += item[1] * jersey.price});
        }

        this._jerseys = newJerseyInfo;

        // let jerseysInfoIndex = this.jerseysInfo.findIndex(jersey => this.jerseyService.getJersey(jerseyId).subscribe(jersey2 => jersey = jersey2));
        // this.jerseysInfo.splice(jerseysInfoIndex, 1);
      }

      else {
        return;
      }
  }


  submit:boolean=false;

  _fName:string ="";
  set fName(val:string)
  {
    this._fName = val;
    this.submit=this.validateInputs()
  }
  _email:string ="";
  set email(val:string)
  {
    this._email = val;
    this.submit=this.validateInputs()
  }
  _adress:string ="";
  set adress(val:string)
  {
    this._adress = val;
    this.submit=this.validateInputs()
  }
  _city:string ="";
  set city(val:string)
  {
    this._city = val;
    this.submit=this.validateInputs()
  }
  _state:string ="";
  set state(val:string)
  {
    this._state = val;
    this.submit=this.validateInputs()
  }
  _zip:string ="";
  set zip(val:string)
  {
    this._zip = val;
    this.submit=this.validateInputs()
  }
  _cName:string ="";
  set cName(val:string)
  {
    this._cName = val;
    this.submit=this.validateInputs()
  }
  _cNum:string ="";
  set cNum(val:string)
  {
    this._cNum = val;
    this.submit=this.validateInputs()
  }
  _expMonth:string ="";
  set expMonth(val:string)
  {
    this._expMonth = val;
    this.submit=this.validateInputs()
  }
  _expYear:string ="";
  set expYear(val:string)
  {
    this._expYear = val;
    this.submit=this.validateInputs()
  }
  _cvv:string ="";
  set cvv(val:string)
  {
    this._cvv = val;
    this.submit=this.validateInputs()
  }


  warning : string =""
  validateInputs()
  {
    if(this._fName.length < 1){
      this.warning = "please enter your name"
      return false;
    }else if(this._email.length < 1){
      this.warning = "please enter an email";
      return false;
    }else if(this._adress.length < 1){
      this.warning = "please enter an adress";
      return false;
    }else if(this._city.length < 1){
      this.warning = "please enter a city";
      return false;
    }else if(this._state.length < 1){
      this.warning = "please enter a state";
      return false;
    }else if(this._zip.length != 5){
      this.warning = "please enter a valid 5 digit zipcode";
      return false;
    }else if(this._cName.length < 1){
      this.warning = "please enter a valid card name";
      return false;
    }else if(this._cNum.length < 15 || this._cNum.length > 16){
      this.warning = "please enter a valid credit card number";
      return false;
    }else if(this._expMonth.length < 1){
      this.warning = "please enter a valid experation month";
      return false;
    }else if(this._expYear.length < 1){
      this.warning = "please enter a valid experation year";
      return false;
    }else if(this._cvv.length < 3 || this._cvv.length > 3){
      this.warning = "please enter a valid 3 digit cvv number";
      return false;
    }


    this.warning = "";
    return true;
  }



  update(){
    this._jerseys.forEach(element =>
      {
        this.jerseyService.updateJersey({
          id:element.id, imagePath:element.imagePath, color: element.color, playerName:element.playerName,
          playerNumber:element.playerNumber, reviews:element.reviews,size:element.size,price:element.price,
          quantity:element.quantity-this._items[this._jerseys.indexOf(element)][1], avgReview:element.avgReview
        }).subscribe(item => element = item);
      });
  }
}
