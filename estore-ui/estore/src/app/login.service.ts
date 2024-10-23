import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})

// Handles keeping track of the logged in user during their session.
export class LoginService {
  // Since app component is always init first, it must observes who logs in
  loggedInUserObv: Subject<string> = new Subject;
  isAdminObv: Subject<boolean> = new Subject;

  // Other components can reference who logged in with these variables
  latestLogin: string = "";
  isAdmin: boolean = false;

  get loggedInUser(): Observable<string> {
    return this.loggedInUserObv.asObservable();
  }

  login(username: string){
    this.loggedInUserObv.next(username);
    this.latestLogin = username;
    
    this.isAdmin = username.toLowerCase() == 'admin';
    this.isAdminObv.next(this.isAdmin);
  }

  logout(){
    this.loggedInUserObv.next("");
    this.latestLogin = "";
    this.isAdminObv.next(false);
    this.isAdmin = false;
  }
}
