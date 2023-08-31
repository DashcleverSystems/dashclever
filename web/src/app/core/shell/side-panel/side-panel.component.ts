import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  ElementRef,
  Input,
  OnChanges,
  OnInit,
  SimpleChanges,
  ViewChild,
} from '@angular/core';
import { IShellRoute, ShellRoutes } from '../shell-routes';

@Component({
  selector: 'app-side-panel',
  templateUrl: './side-panel.component.html',
  styleUrls: ['./side-panel.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SidePanelComponent implements OnInit, OnChanges {
  @Input() mobile: boolean = false;

  @ViewChild('container') container: ElementRef<HTMLDivElement> | undefined;

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

  ngOnChanges(changes: SimpleChanges): void {
    const mobile = changes['mobile'];

    if (mobile && this.container) {
      if (this.mobile) {
        this.container.nativeElement.classList.add('mobile');
      } else {
        this.container.nativeElement.classList.remove('mobile');
      }
    }
  }
}
