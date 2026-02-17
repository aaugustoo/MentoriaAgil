import { Component, OnInit, OnDestroy, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';

@Component({
  selector: 'app-logout-view',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './logout.html'
})
export class Logout implements OnInit, OnDestroy {
  contador = signal(3);
  private timer: any;

  constructor(private router: Router) {}

  ngOnInit(): void {
    this.timer = setInterval(() => {
      this.contador.update(v => v - 1);

      if (this.contador() === 0){
        clearInterval(this.timer);
        this.router.navigate(['/']);
      }
    }, 1000);
  }

  ngOnDestroy(): void {
    if (this.timer) clearInterval(this.timer);
  }
}