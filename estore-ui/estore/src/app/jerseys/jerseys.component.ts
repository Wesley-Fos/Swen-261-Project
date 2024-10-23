import { Component } from '@angular/core';
import { JerseyService } from '../jersey.service';
import { Jersey } from '../jersey';



@Component({
  selector: 'app-jerseys',
  templateUrl: './jerseys.component.html',
  styleUrls: ['./jerseys.component.css']
})

// Component that shows all the jerseys in the inventory and search functions
export class JerseysComponent {
  _filterText: string = "";
  _filterSize: string = "";
  _filterRange: number[] = [-1,-1];
  _filterNumber: number = 0;
  _minReview: number = 0;

  filteredJerseys: Jersey[] = [];

  get filterText() {
    return this._filterText;
  }
  get filterSize() {
    return this._filterSize;
  }
  get filterRange() {
    return this._filterRange;
  }
  get filterNumber() {
    return this._filterNumber;
  }
  get minReview(){
    return this._minReview;
  }

  set minReview(value: number){
    this._minReview=value
    this.filteredJerseys = [];
    this.filterJersey(this._filterText, this._filterSize, 
      this._filterRange, this._filterNumber, value)
  }

  set filterText(value: string) {
    this._filterText = value;
    this.filteredJerseys = [];
    this.filterJersey(value, this._filterSize, 
      this._filterRange, this._filterNumber, this._minReview);
  }

  set filterSize(value: string) {
    this._filterSize = value;
    this.filteredJerseys = [];
    this.filterJersey(this._filterText, value, 
      this._filterRange, this._filterNumber, this._minReview);
  }

  set filterRange1(value: number) {
    if(value == null) value == -1;
    this._filterRange[1] = value;
    this.filterJersey(this._filterText, this.filterSize, 
      this._filterRange, this._filterNumber, this._minReview); 
  }

  set filterRange0(value: number) {
    if(value == null) value == -1;
    this._filterRange[0] = value;
    this.filterJersey(this._filterText, this.filterSize, 
      this._filterRange, this._filterNumber, this._minReview); 
  }

  set filterNumber(value: number) {
    if(value == null) value == 0;
    this._filterNumber = value;
    this.filteredJerseys = [];
    this.filterJersey(this._filterText, this.filterSize, 
      this._filterRange, value, this._minReview);
  }

  constructor(private jerseyService: JerseyService){}

  ngOnInit(): void {
    this.jerseyService.searchJerseys("", "", 
        [-1,-1], -1, 0).subscribe(jerseys => this.filteredJerseys = jerseys);
    this.filterJersey("", "", [-1,-1], 0, 0);
  }

  clear()
  {
    this._filterRange = [-1,-1];
    this._filterText = "";
    this._filterSize = "";
    this._filterNumber = 0;
    this._minReview = 0;

    this.filterJersey(this._filterText, this.filterSize, 
      this._filterRange, this._filterNumber, this._minReview);
  }


  filterJersey(filterTerm: string, filterSize: string, filterRange: number[], filterNumber: number, filterMin: number): void {
    if( filterTerm == "" && filterSize == "" &&
      filterRange[0] < 0 && filterRange[1] < 0 && filterNumber <= 0) 
      {this.jerseyService.searchJerseys("","", [-1,-1], 0, 0).subscribe(jerseys => this.filteredJerseys = jerseys);}
    this.jerseyService.searchJerseys(filterTerm, filterSize, 
      filterRange, filterNumber, filterMin).subscribe(jerseys => this.filteredJerseys = jerseys);
  }
    
}
