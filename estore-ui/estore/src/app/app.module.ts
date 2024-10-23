import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CartComponent } from './cart/cart.component';
import { JerseysComponent } from './jerseys/jerseys.component';
import { LoginComponent } from './login/login.component';
import { JerseyDetailComponent } from './jersey-detail/jersey-detail.component';
import {FormsModule} from "@angular/forms";
import { PagenotfoundComponent } from './pagenotfound/pagenotfound.component';
import { AdminDashboardComponent } from './admin-dashboard/admin-dashboard.component';
import { ReciptComponent } from './recipt/recipt.component';

@NgModule({
  declarations: [
    AppComponent,
    CartComponent,
    JerseysComponent,
    LoginComponent,
    JerseyDetailComponent,
    PagenotfoundComponent,
    AdminDashboardComponent,
    ReciptComponent
  ],
  imports: [
    HttpClientModule,
    BrowserModule,
    AppRoutingModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
