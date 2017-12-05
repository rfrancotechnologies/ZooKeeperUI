import { RouterModule, Routes } from '@angular/router';
import { NodesPageComponent } from './components/nodes-page/nodes-page.component';
import { LoginComponent } from './components/login/login.component';

const appRoutes: Routes = [
  { path: 'nodes/:nodePath', component: NodesPageComponent },
  { path: '', pathMatch: 'full', redirectTo: '/nodes/~~' },
  { path: 'login', component: LoginComponent }
];

export const routing = RouterModule.forRoot(appRoutes);
