import { Routes } from '@angular/router';

export const routes: Routes = [
  { 
    path: '', 
    redirectTo: 'registro', 
    pathMatch: 'full' 

  },
  { 
    path: 'termos', 
    loadComponent: () => import('./views/termos/Termos')
    .then(m => m.Termos) 
  },
  { 
    path: 'registro', 
    loadComponent: () => import('./views/auth/registro/Registro')
    .then(m => m.Registro)
  },
  { 
    path: 'login', 
    loadComponent: () => import('./views/auth/login/Login')
    .then(m => m.Login)
  },
  { 
    path: 'logout', 
    loadComponent: () => import('./views/auth/logout/Logout')
    .then(m => m.Logout) 
  },
  
  {
    path: '',
    loadComponent: () => import('./layouts/Layout')
    .then(m => m.Layout),
    children: [
      {
        path: 'dashboard',
        loadComponent: () => import('./views/dashboard/Dashboard')
        .then(m => m.Dashboard)
      },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },

  { 
    path: '**', 
    redirectTo: 'registro' 
  }
];