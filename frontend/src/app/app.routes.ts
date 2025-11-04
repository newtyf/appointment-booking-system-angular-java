import { Routes } from '@angular/router';
import { roleGuard } from './core/guards/role.guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/auth/login',
    pathMatch: 'full'
  },
  {
    path: 'auth/login',
    loadComponent: () => import('./features/auth/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'auth/register',
    loadComponent: () => import('./features/auth/register/register.component').then(m => m.RegisterComponent)
  },
  {
    path: 'admin',
    canActivate: [roleGuard(['admin'])],
    children: [
      {
        path: '',
        loadComponent: () => import('./features/admin/dashboard/dashboard.component').then(m => m.DashboardComponent)
      }
    ]
  },
  {
    path: 'receptionist',
    canActivate: [roleGuard(['receptionist'])],
    children: [
      {
        path: '',
        loadComponent: () => import('./features/receptionist/dashboard/dashboard.component').then(m => m.DashboardComponent)
      }
    ]
  },
  {
    path: 'stylist',
    canActivate: [roleGuard(['stylist'])],
    children: [
      {
        path: '',
        loadComponent: () => import('./features/stylist/dashboard/dashboard.component').then(m => m.DashboardComponent)
      }
    ]
  },
  {
    path: 'client',
    canActivate: [roleGuard(['client'])],
    children: [
      {
        path: '',
        loadComponent: () => import('./features/client/dashboard/dashboard.component').then(m => m.DashboardComponent)
      }
    ]
  }
];
