import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, of } from 'rxjs';
import { Cart } from './cart';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  private estoreUrl = "http://localhost:8080/estore/shoppingcart"
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient) {}

  createCart(newCart: Cart): Observable<Cart> {
    return this.http.post<Cart>(this.estoreUrl, newCart, this.httpOptions).pipe(
      catchError(this.handleError<Cart>('createCart'))
    );
  }

  getCarts(): Observable<Cart[]> {
    return this.http.get<Cart[]>(`${this.estoreUrl}/`).pipe(
      catchError(this.handleError<Cart[]>('getCarts', []))
    );
  }

  getCart(id: number): Observable<Cart> {
    const url = `${this.estoreUrl}/${id}`;
    return this.http.get<Cart>(url).pipe(
      catchError(this.handleError<Cart>(`getCart id=${id}`))
    )
  }

  searchCarts(username: string): Observable<Cart> {
    return this.http.get<Cart>(`${this.estoreUrl}/?username=${username}`).pipe(
      catchError(this.handleError<Cart>('searchCarts'))
    );
  }

  //updates the cart using the provided cart's id and changes its contents
  updateCart(cart: Cart): Observable<any> {
    return this.http.put(this.estoreUrl, cart, this.httpOptions).pipe(
      catchError(this.handleError<any>('updateJersey'))
    );
  }

  /**
 * Handle Http operation that failed.
 * Let the app continue.
 *
 * @param operation - name of the operation that failed
 * @param result - optional value to return as the observable result
 */
  private handleError<T>(operation = 'operation', result?: T) {
  return (error: any): Observable<T> => {

    // TODO: send the error to remote logging infrastructure
    console.error(error); // log to console instead

    // Let the app keep running by returning an empty result.
    return of(result as T);
    };
  }
}
