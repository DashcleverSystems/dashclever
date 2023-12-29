import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-login-cloud',
  templateUrl: './logout-cloud.component.html',
  styleUrls: ['./logout-cloud.component.scss'],
})
export class LogoutCloudComponent {
  @Input() mobile: boolean | undefined;

  constructor(
    private readonly router: Router,
    private readonly http: HttpClient,
  ) {}

  onLogout(): void {
    this.http.post('/api/logout', {}).subscribe({
      next: () => this.router.navigate(['login']),
      error: () => this.router.navigate(['login']),
    });
  }
}
