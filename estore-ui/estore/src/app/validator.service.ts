import { Injectable } from '@angular/core';
import { Jersey } from './jersey';

@Injectable({
  providedIn: 'root'
})

// Service created to validate user input when editing or adding new jerseys
export class ValidatorService {

  constructor() { }

  validateJersey(jersey: Jersey): boolean {
    if(jersey.playerName.trim() == "" || jersey.playerNumber == 0 || jersey.color.trim() == "" || jersey.size.trim() == "" || jersey.imagePath.trim() == ""){
      alert("All fields must be filled in.")
      return false
    }
    if(isNaN(+jersey.playerNumber) || isNaN(+jersey.price) || isNaN(+jersey.quantity)){
      alert("Only numbers allowed for player number, price, and quantity")
      return false
    }
    if(Math.sign(+jersey.playerNumber) == -1 || Math.sign(+jersey.price) == -1 || Math.sign(+jersey.quantity) == -1){
      alert("Negative numbers not allowed for player number, price, or quantity.")
      return false
    }
    return true
  }
}
