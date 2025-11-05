import { Routes } from '@angular/router';
import { roleGuard } from './core/guards/role.guard';
import { InsideLayoutComponent } from './shared/components/inside-layout.component';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./features/landing/landing-page.component').then(m => m.LandingPageComponent)
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
    component: InsideLayoutComponent,
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
    component: InsideLayoutComponent,
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
    component: InsideLayoutComponent,
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
    component: InsideLayoutComponent,
    canActivate: [roleGuard(['client'])],
    children: [
      {
        path: '',
        loadComponent: () => import('./features/client/dashboard/dashboard.component').then(m => m.DashboardComponent)
      }
    ]
  },
  {
    path: '**',
    redirectTo: ''
  }
];
