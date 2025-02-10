import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from '../admin/navbar/navbar.component'; // Assurez-vous du bon chemin

@NgModule({
  declarations: [NavbarComponent],
  imports: [CommonModule],
  exports: [NavbarComponent], // 👈 Exporte le composant pour l'utiliser ailleurs
})
export class SharedModule {}
