import { Injectable } from '@angular/core';
import { Jersey } from './jersey';
import { Observable, catchError, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class JerseyService {
  private estoreUrl = "http://localhost:8080/estore"
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient) {}
  

  getJerseys(): Observable<Jersey[]>{
    return this.http.get<Jersey[]>(this.estoreUrl).pipe(
      catchError(this.handleError<Jersey[]>('getJerseys', []))
    );
  }

  getJersey(id: number): Observable<Jersey> {
    const url = `${this.estoreUrl}/${id}`;
    return this.http.get<Jersey>(url).pipe(
      catchError(this.handleError<Jersey>(`getJersey id=${id}`))
    )
  }

  searchJerseys(term: string, size: string, range: number[], num: number, minReview: number): Observable<Jersey[]> {

    return this.http.get<Jersey[]>(`${this.estoreUrl}/?playerName=${term}&size=${size}&priceRange=${range[0]}&priceRange=${range[1]}&playerNum=${num}&minReview=${minReview}`).pipe(
      catchError(this.handleError<Jersey[]>('searchJerseys', []))
    );
  }

  createJersey(jersey: Jersey): Observable<Jersey>{
    return this.http.post<Jersey>(this.estoreUrl, jersey, this.httpOptions).pipe(
      catchError(this.handleError<Jersey>("createJersey"))
    )}

  updateJersey(jersey: Jersey): Observable<any> {
    return this.http.put(this.estoreUrl, jersey, this.httpOptions).pipe(
      catchError(this.handleError<any>('updateJersey'))
    );
  }

  deleteJersey(id: number): Observable<Jersey> {
    const url = `${this.estoreUrl}/${id}`;

    return this.http.delete<Jersey>(url, this.httpOptions).pipe(
      catchError(this.handleError<Jersey>('deleteJersey'))
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
