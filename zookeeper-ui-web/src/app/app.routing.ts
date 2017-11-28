import { RouterModule, Routes } from '@angular/router';
import { NodesPageComponent } from './components/nodes-page/nodes-page.component';
const appRoutes: Routes = [
  { path: '', component: NodesPageComponent }
];

export const routing = RouterModule.forRoot(appRoutes);
