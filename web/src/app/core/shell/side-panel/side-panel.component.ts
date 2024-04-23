import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  inject,
  Input,
  OnInit,
} from '@angular/core';
import { IShellRoute, ShellRoutes } from '../shell-routes';
import CoreStore from '@app/core/store/core-store';

@Component({
  selector: 'app-side-panel',
  templateUrl: './side-panel.component.html',
  styleUrls: ['./side-panel.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SidePanelComponent implements OnInit {
  navList: IShellRoute[] = [];
  isDarkTheme: boolean = false;

  get isMobile() {
    return this.coreStore.mobile;
  }

  private coreStore = inject(CoreStore);

  toggleTheme(): void {
    this.isDarkTheme = !this.isDarkTheme;

    if (this.isDarkTheme) {
      document.body.classList.add('dark');
    } else document.body.classList.remove('dark');
  }

  trackBy = (index: number) => index;

  constructor(private readonly _cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.navList = ShellRoutes;
    this._cdr.markForCheck();
  }
}
