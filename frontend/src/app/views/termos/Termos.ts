import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-termos',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './termos.html'
})
export class Termos {
    imprimirTermos() {
        window.print();
    }
}