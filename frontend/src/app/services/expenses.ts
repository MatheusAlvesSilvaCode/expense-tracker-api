import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Auth } from './auth';

export interface CategoryItem {
  id: number;
  name?: string;
  categoryName?: string;
  title?: string;
}

export interface ExpenseItem {
  id: number;
  description: string;
  amount: number;
  expenseDate: string;
  category?: {
    id: number;
    name?: string;
    categoryName?: string;
    title?: string;
  };
  categoryId?: number;
}

export interface CreateExpenseRequest {
  description: string;
  amount: number;
  expenseDate: string;
  categoryId: number;
}

export interface UpdateExpenseRequest {
  description?: string;
  amount?: number;
  expense_date?: string;
  expenseId?: number;
}

@Injectable({ providedIn: 'root' })
export class Expenses {
  private http = inject(HttpClient);
  private auth = inject(Auth);
  private readonly baseUrl = 'http://localhost:8080';
  private readonly expensesUrl = `${this.baseUrl}/expenses`;

  private getHeaders(): HttpHeaders {
    const token = this.auth.getToken() ?? '';
    return new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
  }

  getCategories(): Observable<CategoryItem[]> {
    return this.http.get<CategoryItem[]>(`${this.baseUrl}/categories`, {
      headers: this.getHeaders(),
    });
  }

  createExpense(payload: CreateExpenseRequest): Observable<ExpenseItem> {
    return this.http.post<ExpenseItem>(`${this.expensesUrl}/save-expenses`, payload, {
      headers: this.getHeaders(),
    });
  }

  updateExpense(id: number, payload: UpdateExpenseRequest): Observable<ExpenseItem> {
    return this.http.put<ExpenseItem>(`${this.expensesUrl}/update-expense/${id}`, payload, {
      headers: this.getHeaders(),
    });
  }

  deleteExpense(id: number): Observable<void> {
    return this.http.put<void>(
      `${this.expensesUrl}/delete-expense/${id}`,
      {},
      {
        headers: this.getHeaders(),
      }
    );
  }

  getPastWeek(): Observable<ExpenseItem[]> {
    return this.http.get<ExpenseItem[]>(`${this.expensesUrl}/pastweek-expense`, {
      headers: this.getHeaders(),
    });
  }

  getPastMonth(): Observable<ExpenseItem[]> {
    return this.http.get<ExpenseItem[]>(`${this.expensesUrl}/pastMonth-expense`, {
      headers: this.getHeaders(),
    });
  }

  getLastThreeMonths(): Observable<ExpenseItem[]> {
    return this.http.get<ExpenseItem[]>(`${this.expensesUrl}/lastThreeMonths-expense`, {
      headers: this.getHeaders(),
    });
  }
}
