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

  navList: IShellRoute[] = [];

  trackBy = (index: number) => index;

  constructor(private readonly _cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.navList = ShellRoutes;
    this._cdr.markForCheck();
  }
}
