import { RouterModule, Routes } from '@angular/router';
import { NodesPageComponent } from './components/nodes-page/nodes-page.component';
import { LoginComponent } from './components/login/login.component';
import { NotFoundComponent } from './components/not-found/not-found.component';

const appRoutes: Routes = [
  { path: 'nodes/:nodePath', component: NodesPageComponent },
  { path: '', pathMatch: 'full', redirectTo: '/nodes/~~' },
  { path: 'login', component: LoginComponent },
  { path: 'not_found', component: NotFoundComponent }
];

export const routing = RouterModule.forRoot(appRoutes);
