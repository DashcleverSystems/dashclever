import {
  AfterViewInit,
  ChangeDetectorRef,
  Component,
  ElementRef,
  OnDestroy,
  OnInit,
  ViewChild,
} from '@angular/core';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { coreStoreActions } from '../store/core-store.actions';

@Component({
  templateUrl: './shell.component.html',
  styleUrls: ['./shell.component.scss'],
})
export class ShellComponent implements OnInit, AfterViewInit, OnDestroy {
  @ViewChild('container') container: ElementRef<HTMLDivElement> | undefined;
  @ViewChild('content') content: ElementRef<HTMLDivElement> | undefined;

  mobile: boolean = false;

  resizeObserver: ResizeObserver = new ResizeObserver(
    (event: ResizeObserverEntry[]) => {
      const { width } = event[0].contentRect;
      if (width >= 700 && this.mobile) {
        this.mobile = false;
        this.store.dispatch(
          coreStoreActions.changeAppView({ mobile: this.mobile }),
        );
        this.content?.nativeElement.classList.remove('mobile');
        this.container?.nativeElement.classList.remove('mobile');
        this._cdr.detectChanges();
      } else if (!this.mobile && width < 700) {
        this.mobile = true;
        this.store.dispatch(
          coreStoreActions.changeAppView({ mobile: this.mobile }),
        );

        this.content?.nativeElement.classList.add('mobile');
        this.container?.nativeElement.classList.add('mobile');
        this._cdr.detectChanges();
      }
    },
  );

  constructor(
    private _cdr: ChangeDetectorRef,
    private router: Router,
    private store: Store,
  ) {}

  ngOnInit(): void {
    const { url } = this.router;
    if (url === '/') {
      this.router.navigate(['home']);
    }

    this.store.dispatch(
      coreStoreActions.changeAppView({
        mobile: document.body.getBoundingClientRect().width <= 700,
      }),
    );
  }

  ngAfterViewInit(): void {
    if (this.container) {
      this.resizeObserver.observe(this.container.nativeElement);
    }
  }

  ngOnDestroy(): void {
    this.resizeObserver.disconnect();
  }
}
