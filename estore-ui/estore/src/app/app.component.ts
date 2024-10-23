import { Component } from '@angular/core';
import { LoginService } from './login.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'estore';
  currentUser: string = "";
  isAdmin?: boolean;

  constructor(private loginService: LoginService, private router: Router){
    // app subscribes to login service and will know who is logged in
    this.loginService.loggedInUserObv.subscribe(user => this.currentUser = user);
    this.loginService.isAdminObv.subscribe(admin => this.isAdmin = admin);
  }

  logout(){
    this.loginService.logout();
    this.router.navigateByUrl("/login");
  }
}
