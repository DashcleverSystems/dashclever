import {
  ChangeDetectionStrategy,
  Component,
  inject,
  OnInit,
} from '@angular/core';
import { Router } from '@angular/router';
import CoreStore from '../store/core-store';

@Component({
  templateUrl: './shell.component.html',
  styleUrls: ['./shell.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ShellComponent implements OnInit {
  get isMobile() {
    return this.coreStore.mobile;
  }

  private coreStore = inject(CoreStore);

  constructor(private router: Router) {}

  ngOnInit(): void {
    const { url } = this.router;
    if (url === '/') {
      this.router.navigate(['home']);
    }
  }
}
