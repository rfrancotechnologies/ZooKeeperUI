import { RouterModule, Routes } from '@angular/router';
import { NodesPageComponent } from './components/nodes-page/nodes-page.component';
const appRoutes: Routes = [
  { path: ':nodePath', component: NodesPageComponent },
  { path: '', pathMatch: 'full', redirectTo: '/~~' }
];

export const routing = RouterModule.forRoot(appRoutes);
