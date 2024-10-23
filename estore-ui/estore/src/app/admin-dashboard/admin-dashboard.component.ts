import { Component } from '@angular/core';
import { Jersey } from '../jersey';
import { JerseyService } from '../jersey.service';
import { ValidatorService } from '../validator.service';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})

// Dashboard for the admin to view, delete, add, and edit the jerseys in inventory
export class AdminDashboardComponent {
  jerseys: Jersey[] = [];

  constructor(private jerseyService: JerseyService, private validator: ValidatorService) { }

  ngOnInit(): void {
    this.getJerseys();
  }

  getJerseys(): void {
    this.jerseyService.getJerseys()
      .subscribe(jerseys => this.jerseys = jerseys);
  }

  add(playerName: string, playerNumber: string, color: string, size: string, price: string, quantity: string, imagePath: string){
    let jersey: Jersey = {
      id: 0,
      playerName: playerName,
      playerNumber: +playerNumber,
      color: color,
      size: size,
      price: +price,
      quantity: +quantity,
      avgReview: 0,
      reviews: [],
      imagePath: imagePath
    }

    if(this.validator.validateJersey(jersey)){
      this.jerseyService.createJersey(jersey).subscribe(jersey => this.jerseys.push(jersey))
    }    
  }

  delete(jersey: Jersey){
    if(confirm(`DELETING ${jersey?.playerName.toUpperCase()} JERSEY\nCLICK OK TO CONTINUE`)){
      const id = jersey.id
      this.jerseys = this.jerseys.filter(j => j.id != id)
      this.jerseyService.deleteJersey(id).subscribe()
    }
  }
}
