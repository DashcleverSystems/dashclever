import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-login-cloud',
  templateUrl: './login-cloud.component.html',
  styleUrls: ['./login-cloud.component.scss'],
})
export class LoginCloudComponent {
  @Input() mobile: boolean | undefined;
}
