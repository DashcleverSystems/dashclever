import {
  AfterViewInit,
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  ElementRef,
  inject,
  OnDestroy,
  OnInit,
  ViewChild,
} from '@angular/core';
import { IShellRoute, ShellRoutes } from '../shell-routes';
import CoreStore from '@app/core/store/core-store';
import { Sidebar } from 'primeng/sidebar';
import { fromEvent, Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'app-side-panel',
  templateUrl: './side-panel.component.html',
  styleUrls: ['./side-panel.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SidePanelComponent implements OnInit, AfterViewInit, OnDestroy {
  @ViewChild('sidebarHolder', { read: ElementRef })
  sidebarHolder: ElementRef<HTMLDivElement>;
  @ViewChild('sidebar')
  sidebar: Sidebar;

  navList: IShellRoute[] = [];
  isDarkTheme: boolean = false;
  sidebarVisibility: boolean = false;

  get isMobile() {
    return this.coreStore.mobile;
  }

  private destroy$ = new Subject<void>();
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

  ngAfterViewInit(): void {
    if (this.sidebarHolder) {
      fromEvent(this.sidebarHolder.nativeElement, 'click')
        .pipe(takeUntil(this.destroy$))
        .subscribe(() => {
          this.sidebarVisibility = true;
          this._cdr.markForCheck();
        });
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  onClose(event: Event): void {
    this.sidebar.close(event);
  }
}
