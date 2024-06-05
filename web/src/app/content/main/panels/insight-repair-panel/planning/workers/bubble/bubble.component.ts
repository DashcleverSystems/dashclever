import {AfterViewInit, ChangeDetectionStrategy, ChangeDetectorRef, Component, ElementRef, HostListener, Input, OnChanges, Self, SimpleChanges, SkipSelf, ViewChild,} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import moment from 'moment';
import {Chart} from 'chart.js';
import {Worker} from "@content/main/panels/insight-repair-panel/planning/planning.store";
import {fromEvent, take} from 'rxjs';

@Component({
  selector: 'app-bubble',
  templateUrl: './bubble.component.html',
  styleUrl: './bubble.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class BubbleComponent implements AfterViewInit, OnChanges {
  @ViewChild('bubble') bubble: Chart;
  @Input({required: true}) worker: Worker;

  isDragAbove: boolean = false;

  private graphColors: [string, string, string] = [
    '#C3E2C2',
    '#49b0ce',
    '#7a3a8e',
  ];

  get id() {
    return this.worker.id;
  }

  constructor(
    @Self() private _cdr: ChangeDetectorRef,
    @Self() private ref: ElementRef,
    @SkipSelf() private translate: TranslateService,
  ) {
  }

  @HostListener('mouseenter', ['$event']) MouseOver(event: MouseEvent) {
    if (event.buttons > 0) {
      this.isDragAbove = true;
      fromEvent(this.ref.nativeElement, 'mouseup')
        .pipe(take(1))
        .subscribe(() => {
          this.isDragAbove = false;
          this._cdr.markForCheck();
        });

      fromEvent(this.ref.nativeElement, 'mouseleave')
        .pipe(take(1))
        .subscribe(() => {
          this.isDragAbove = false;
          this._cdr.markForCheck();
        });
    }
  }

  ngAfterViewInit() {
    if (this.bubble) {
      this.initBubble();
    }
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes.worker && this.bubble) {
      this.getBubbleData();
    }
  }

  private initBubble() {
    this.bubble.options = {
      plugins: {
        tooltip: {
          callbacks: {
            label: (item: any) => `${this.formatTime(item.formattedValue)}`,
          },
          mode: 'point',
        },
        legend: {
          display: false,
        },
      },
    };

    this.getBubbleData();
  }

  private getBubbleData() {
    let freeTimeLeft = 480 - this.worker.occupation;
    const labels: string[] = [
      this.translate.instant('components.bubble.chartLabels.freeTime'),
      this.translate.instant('components.bubble.chartLabels.workTime'),
      this.translate.instant('components.bubble.chartLabels.overTime'),
    ];
    let overTime = 0;

    if (freeTimeLeft < 0) {
      this.graphColors[1] = '#FF0000';
      overTime = Math.abs(freeTimeLeft);
      freeTimeLeft = 0;
    } else {
      this.graphColors[1] = '#49b0ce';
    }

    this.bubble.data = {
      labels: [...labels],
      datasets: [
        {
          data: [freeTimeLeft, this.worker.occupation || 0, overTime || 0],
          backgroundColor: this.graphColors,
          hoverBackgroundColor: this.graphColors,
        },
      ],
    };
    this._cdr.markForCheck();
  }

  private formatTime(time: number) {
    const hours = time / 60 || 0;
    const minutes = time % 60 || 0;

    if (hours < 1) {
      return `${minutes} min.`;
    }

    return moment().hours(hours).minutes(minutes).format('hh:mm') + 'h';
  }
}
