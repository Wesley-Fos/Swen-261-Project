import { Component } from '@angular/core';
import { CartService } from '../cart.service';
import { LoginService } from '../login.service';
import { Router } from '@angular/router';
import { JerseyService } from '../jersey.service';
import { Jersey } from '../jersey';
import { Cart } from '../cart';

@Component({
  selector: 'app-recipt',
  templateUrl: './recipt.component.html',
  styleUrls: ['./recipt.component.css']
})
export class ReciptComponent {
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
      if(this._user == null || this._user == "") this.router.navigateByUrl("/login");
      
      this.cartService.searchCarts(this._user).subscribe(item => this._cart = item);
      this.cartService.searchCarts(this._user).subscribe(item => this._items = item.Items);

      this.cartService.searchCarts(this._user).subscribe(item => {for(let element of item.Items){
        this.jerseyService.getJersey(element[0])
        .subscribe(val => {this._jerseys.push(val); 
          this._price += (val.price * element[1])});
        
      };
      this.cartService.updateCart({id:item.id, customerUsername:item.customerUsername, Items:[]}).subscribe(i=>item=i);
    });
      
    }


    get jerseysInfo()
    {
      return this._jerseys;
    }
  }
