import { Component, Input } from '@angular/core';
import { coreStoreActions } from '@app/core/store/core-store.actions';
import { Store } from '@ngrx/store';

@Component({
  selector: 'app-login-cloud',
  templateUrl: './login-cloud.component.html',
  styleUrls: ['./login-cloud.component.scss'],
})
export class LoginCloudComponent {
  @Input() mobile: boolean | undefined;

  constructor(private store: Store) {}

  onLogout(): void {
    this.store.dispatch(coreStoreActions.logout());
  }
}
