import { Component, inject, Input } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import CoreStore from '@app/core/store/core-store';

@Component({
  selector: 'app-login-cloud',
  templateUrl: './logout-cloud.component.html',
  styleUrls: ['./logout-cloud.component.scss'],
})
export class LogoutCloudComponent {
  get isMobile() {
    return this.coreStore.mobile;
  }

  private coreStore = inject(CoreStore);

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
