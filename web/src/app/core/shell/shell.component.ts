import {
  AfterViewInit,
  ChangeDetectorRef,
  Component,
  ElementRef,
  OnDestroy,
  ViewChild,
} from '@angular/core';

@Component({
  templateUrl: './shell.component.html',
  styleUrls: ['./shell.component.scss'],
})
export class ShellComponent implements AfterViewInit, OnDestroy {
  @ViewChild('container') container: ElementRef<HTMLDivElement> | undefined;
  @ViewChild('content') content: ElementRef<HTMLDivElement> | undefined;

  mobile: boolean = false;

  resizeObserver: ResizeObserver = new ResizeObserver(
    (event: ResizeObserverEntry[]) => {
      const { width } = event[0].contentRect;
      if (width >= 700 && this.mobile) {
        this.mobile = false;
        this.content?.nativeElement.classList.remove('mobile');
        this.container?.nativeElement.classList.remove('mobile');

        this._cdr.detectChanges();
      } else if (!this.mobile && width < 700) {
        this.mobile = true;
        this.content?.nativeElement.classList.add('mobile');
        this.container?.nativeElement.classList.add('mobile');
        this._cdr.detectChanges();
      }
    }
  );

  constructor(private _cdr: ChangeDetectorRef) {}

  ngAfterViewInit(): void {
    if (this.container) {
      this.resizeObserver.observe(this.container.nativeElement);
    }
  }

  ngOnDestroy(): void {
    this.resizeObserver.disconnect();
  }
}
