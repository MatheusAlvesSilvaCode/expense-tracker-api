import { Component, inject, ChangeDetectorRef, NgZone, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Auth } from './services/auth';
import {
  Expenses,
  CategoryItem,
  ExpenseItem,
  CreateExpenseRequest,
  UpdateExpenseRequest,
} from './services/expenses';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App implements OnInit {
  private auth = inject(Auth);
  private expensesService = inject(Expenses);
  private cdr = inject(ChangeDetectorRef);
  private ngZone = inject(NgZone);

  // Auth state
  isRegisterMode = false;
  nameUser = '';
  email = '';
  password = '';
  loggedIn = this.auth.isLoggedIn();
  isLoading = false;
  statusMessage = '';
  errorMessage = '';

  // Categories
  categories: CategoryItem[] = [];

  // Expenses & filters
  expenses: ExpenseItem[] = [];
  activeFilter: 'week' | 'month' | 'three-months' = 'week';

  // Create form
  newExpense: CreateExpenseRequest = {
    description: '',
    amount: 0,
    expenseDate: this.getLocalDateTimeNow(),
    categoryId: 1,
  };

  // Edit state
  editingId: number | null = null;
  editForm: {
    description: string;
    amount: number;
    expenseDate: string;
    categoryId: number;
  } = {
    description: '',
    amount: 0,
    expenseDate: '',
    categoryId: 1,
  };

  ngOnInit(): void {
    if (this.loggedIn) {
      this.loadCategories();
      this.loadExpenses(this.activeFilter);
    }
  }

  private refreshView(): void {
    this.ngZone.run(() => {
      this.cdr.detectChanges();
    });
  }

  private getLocalDateTimeNow(): string {
    const now = new Date();
    const pad = (n: number) => String(n).padStart(2, '0');
    const year = now.getFullYear();
    const month = pad(now.getMonth() + 1);
    const day = pad(now.getDate());
    const hours = pad(now.getHours());
    const minutes = pad(now.getMinutes());
    const seconds = pad(now.getSeconds());
    return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;
  }

  private normalizeDateTimeForInput(value?: string): string {
    if (!value) return this.getLocalDateTimeNow();
    return value.slice(0, 19);
  }

  private normalizeDateTimeForBackend(value?: string): string {
    const base = (value || this.getLocalDateTimeNow()).trim();
    if (base.length === 16) {
      return `${base}:00`;
    }
    return base.slice(0, 19);
  }

  get totalAmount(): number {
    return this.expenses.reduce((sum, item) => sum + Number(item.amount || 0), 0);
  }

  private resolveLabel(cat?: CategoryItem): string {
    if (!cat) return '';
    return cat.name || cat.categoryName || cat.title || '';
  }

  getCategoryLabel(cat: CategoryItem): string {
    return this.resolveLabel(cat) || `Category #${cat.id}`;
  }

  loadCategories(): void {
    this.expensesService.getCategories().subscribe({
      next: (data) => {
        this.categories = Array.isArray(data) ? data : [];
        if (this.categories.length > 0) {
          const exists = this.categories.some((c) => c.id === Number(this.newExpense.categoryId));
          if (!exists) {
            this.newExpense.categoryId = this.categories[0].id;
          }
        }
        this.refreshView();
      },
      error: (err) => {
        console.error('Error fetching categories:', err);
        this.handleAuthOrError(err, 'Could not load categories.');
      },
    });
  }

  getCategoryName(item: ExpenseItem): string {
    const directLabel = this.resolveLabel(item.category);
    if (directLabel) return directLabel;

    const id = item.category?.id ?? item.categoryId;
    if (!id) return 'Uncategorized';
    const found = this.categories.find((c) => c.id === id);
    return found ? this.getCategoryLabel(found) : `Cat #${id}`;
  }

  private isTokenExpiredError(err: any): boolean {
    if (err?.status === 401 || err?.status === 403) {
      return true;
    }
    const raw = JSON.stringify(err?.error ?? err?.message ?? '').toLowerCase();
    return (
      raw.includes('expiredjwtexception') ||
      raw.includes('jwt expired') ||
      raw.includes('token expired')
    );
  }

  private handleAuthOrError(err: any, fallbackMessage: string): void {
    this.isLoading = false;
    this.statusMessage = '';

    if (this.isTokenExpiredError(err)) {
      this.auth.logout();
      this.loggedIn = false;
      this.isRegisterMode = false;
      this.expenses = [];
      this.categories = [];
      this.editingId = null;
      this.errorMessage = 'Your session has expired. Please sign in again.';
      this.refreshView();
      return;
    }

    this.errorMessage = `${fallbackMessage} (${err?.status || 'network error'}).`;
    this.refreshView();
  }

  toggleAuthMode(): void {
    this.isRegisterMode = !this.isRegisterMode;
    this.errorMessage = '';
    this.statusMessage = '';
    this.refreshView();
  }

  onRegister(): void {
    this.errorMessage = '';
    this.statusMessage = 'Creating account...';
    this.isLoading = true;
    this.refreshView();

    const cleanEmail = this.email.trim();

    this.auth
      .register({
        nameUser: this.nameUser.trim(),
        emailUser: cleanEmail,
        password: this.password,
      })
      .subscribe({
        next: () => {
          this.isLoading = false;
          this.isRegisterMode = false;
          this.nameUser = '';
          this.email = cleanEmail;
          this.password = '';
          this.errorMessage = '';
          this.statusMessage = 'Account created! You can now sign in.';
          this.refreshView();
        },
        error: (err) => {
          console.error('Register error:', err);
          this.isLoading = false;
          this.statusMessage = '';
          this.errorMessage = `Could not create account (${err?.status || 'network error'}). Check your details or backend.`;
          this.refreshView();
        },
      });
  }

  onLogin(): void {
    this.errorMessage = '';
    this.statusMessage = 'Signing in...';
    this.isLoading = true;
    this.refreshView();

    this.auth.login({ email: this.email, password: this.password }).subscribe({
      next: () => {
        this.loggedIn = true;
        this.password = '';
        this.isLoading = false;
        this.statusMessage = 'Welcome back! You are signed in.';
        this.refreshView();
        this.loadCategories();
        this.loadExpenses('week');
      },
      error: (err) => {
        console.error('Login error:', err);
        this.isLoading = false;
        this.statusMessage = '';
        this.errorMessage = `Sign-in failed (${err.status || 'network error'}). Check your credentials and backend.`;
        this.refreshView();
      },
    });
  }

  onLogout(): void {
    this.auth.logout();
    this.loggedIn = false;
    this.isRegisterMode = false;
    this.expenses = [];
    this.categories = [];
    this.editingId = null;
    this.statusMessage = '';
    this.errorMessage = '';
    this.refreshView();
  }

  loadExpenses(filter: 'week' | 'month' | 'three-months'): void {
    this.activeFilter = filter;
    this.errorMessage = '';
    this.isLoading = true;
    this.refreshView();

    const request$ =
      filter === 'week'
        ? this.expensesService.getPastWeek()
        : filter === 'month'
          ? this.expensesService.getPastMonth()
          : this.expensesService.getLastThreeMonths();

    request$.subscribe({
      next: (data) => {
        this.expenses = Array.isArray(data) ? data : [];
        this.isLoading = false;
        this.refreshView();
      },
      error: (err) => {
        console.error('Load expenses error:', err);
        this.handleAuthOrError(err, 'Could not load expenses.');
      },
    });
  }

  onCreateExpense(): void {
    this.errorMessage = '';
    this.statusMessage = 'Saving expense...';
    this.refreshView();

    const payload: CreateExpenseRequest = {
      description: this.newExpense.description,
      amount: Number(this.newExpense.amount),
      expenseDate: this.normalizeDateTimeForBackend(this.newExpense.expenseDate),
      categoryId: Number(this.newExpense.categoryId),
    };

    this.expensesService.createExpense(payload).subscribe({
      next: () => {
        this.newExpense.description = '';
        this.newExpense.amount = 0;
        this.newExpense.expenseDate = this.getLocalDateTimeNow();
        this.statusMessage = 'Expense created successfully!';
        this.refreshView();
        this.loadExpenses(this.activeFilter);
      },
      error: (err) => {
        console.error('Create expense error:', err);
        this.isLoading = false;
        this.handleAuthOrError(err, 'Failed to save expense. Check category and fields.');
      },
    });
  }

  startEdit(expense: ExpenseItem): void {
    this.editingId = expense.id;
    this.editForm = {
      description: expense.description,
      amount: expense.amount,
      expenseDate: this.normalizeDateTimeForInput(expense.expenseDate),
      categoryId: expense.category?.id ?? expense.categoryId ?? this.categories[0]?.id ?? 1,
    };
    this.refreshView();
  }

  cancelEdit(): void {
    this.editingId = null;
    this.refreshView();
  }

  saveEdit(id: number): void {
    this.errorMessage = '';
    this.statusMessage = 'Updating expense...';
    this.refreshView();

    const payload: UpdateExpenseRequest = {
      description: this.editForm.description,
      amount: Number(this.editForm.amount),
      expense_date: this.normalizeDateTimeForBackend(this.editForm.expenseDate),
      expenseId: Number(this.editForm.categoryId),
    };

    this.expensesService.updateExpense(id, payload).subscribe({
      next: () => {
        this.editingId = null;
        this.statusMessage = 'Expense updated!';
        this.refreshView();
        this.loadExpenses(this.activeFilter);
      },
      error: (err) => {
        console.error('Update expense error:', err);
        this.handleAuthOrError(err, 'Failed to update expense.');
      },
    });
  }

  onDeleteExpense(id: number): void {
    this.errorMessage = '';
    this.statusMessage = 'Deleting expense...';
    this.refreshView();

    this.expensesService.deleteExpense(id).subscribe({
      next: () => {
        if (this.editingId === id) {
          this.editingId = null;
        }
        this.statusMessage = 'Expense deleted!';
        this.refreshView();
        this.loadExpenses(this.activeFilter);
      },
      error: (err) => {
        console.error('Delete expense error:', err);
        this.handleAuthOrError(err, 'Failed to delete expense.');
      },
    });
  }
}
