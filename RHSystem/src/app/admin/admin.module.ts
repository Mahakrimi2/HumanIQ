import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdminRoutingModule } from './admin-routing.module';
import { DashboardComponent } from './dashboard/dashboard.component';
import { NavbarComponent } from './navbar/navbar.component';
import { SharedModule } from '../shared/shared.module';
import { EmployeesListComponent } from './employees-list/employees-list.component';

@NgModule({
  declarations: [DashboardComponent, EmployeesListComponent],
  imports: [CommonModule, AdminRoutingModule,SharedModule],
  
})
export class AdminModule {}
