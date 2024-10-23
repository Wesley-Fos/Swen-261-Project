import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CartComponent } from './cart/cart.component';
import { JerseysComponent } from './jerseys/jerseys.component';
import { LoginComponent } from './login/login.component';
import {JerseyDetailComponent} from "./jersey-detail/jersey-detail.component";
import {PagenotfoundComponent} from "./pagenotfound/pagenotfound.component";
import { AdminDashboardComponent } from './admin-dashboard/admin-dashboard.component';
import { ReciptComponent } from './recipt/recipt.component';

const routes: Routes = [
  { path: 'cart', component: CartComponent },
  { path: 'home', component: JerseysComponent },
  { path: 'login', component: LoginComponent},
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'jersey/:id', component: JerseyDetailComponent },
  { path: 'admin', component: AdminDashboardComponent },
  { path: 'recipt', component: ReciptComponent },

  { path: '**', pathMatch: 'full', component: PagenotfoundComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
