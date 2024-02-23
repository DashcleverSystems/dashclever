import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  Input,
  OnInit,
} from '@angular/core';
import { IShellRoute, ShellRoutes } from '../shell-routes';

@Component({
  selector: 'app-side-panel',
  templateUrl: './side-panel.component.html',
  styleUrls: ['./side-panel.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SidePanelComponent implements OnInit {
  @Input() mobile: boolean = false;

  private open: boolean = false;

  get openValue() {
    return this.open;
  }

  set openValue(val: boolean) {
    this.open = val;
  }

  navList: IShellRoute[] = [];

  trackBy = (index: number, el: any) => index;

  constructor(private _cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.navList = ShellRoutes;
    this._cdr.markForCheck();
  }
}
