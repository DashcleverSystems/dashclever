import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  EventEmitter,
  Input,
  OnDestroy,
  OnInit,
  Optional,
  Output,
  Self,
} from '@angular/core';
import { ControlValueAccessor, FormsModule, NgControl } from '@angular/forms';
import { isNil } from 'lodash';
import moment from 'moment';
import { CalendarModule } from 'primeng/calendar';
import { Nullable } from 'primeng/ts-helpers';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-linear-date-picker',
  template: `
    <div class="date-wrapper">
      @if (!disabled) {
        <div class="arrow-left">
          <i
            class="pi pi-angle-left cursor-pointer"
            (click)="onDayBefore()"
          ></i>
        </div>
      }
      <div class="date">
        <p-calendar
          [disabled]="disabled"
          [(ngModel)]="value"
          (onSelect)="onCustomSelectDateChange($event)"
          [dateFormat]="dateFormat"
          [readonlyInput]="true"
        ></p-calendar>
      </div>
      @if (!disabled) {
        <div class="arrow-right">
          <i class="pi pi-angle-right cursor-pointer" (click)="onNextDay()"></i>
        </div>
      }
    </div>
  `,
  styles: `
    .date-wrapper {
      display: flex;
      justify-content: center;
      align-items:center;
      flex-direction: row;
    }
    :host ::ng-deep {
      input {
        text-align: center;
        border: none;
        background-color: transparent;
        cursor: pointer;
        width: fit-content;
        caret-color: transparent;
      }

      .p-inputtext:enabled:focus {
        border: none;
        box-shadow: none;
      }
    }
  `,
  standalone: true,
  imports: [CommonModule, CalendarModule, FormsModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class LinearDatePickerComponent
  implements OnInit, OnDestroy, ControlValueAccessor
{
  @Input() dateFormat: string = 'dd.mm.yy';
  @Input() get disabled(): boolean | undefined {
    return this._disabled;
  }

  set disabled(disabled: boolean | undefined) {
    if (disabled) this.focused = false;

    this._disabled = disabled;
  }

  @Output() onChange: EventEmitter<Date> = new EventEmitter<Date>();

  value: Nullable<Date> = null;
  defaultValue: Nullable<Date> = null;

  focused: boolean = false;
  filled: boolean = false;
  _disabled: boolean = false;

  private destroy$ = new Subject<void>();
  private stateChanges = new Subject<void>();

  private ngChange = (value: any) => {};
  private ngTouched = () => {};

  constructor(
    private cdr: ChangeDetectorRef,
    @Optional() @Self() public ngControl: NgControl,
  ) {
    if (this.ngControl != null) {
      this.ngControl.valueAccessor = this;
    }
  }

  onCustomSelectDateChange(value: Date): void {
    if (moment(value).isValid()) {
      this.value = value;

      this.ngChange(value);
      this.onChange.emit(value);
      this.stateChanges.next();
    }
  }

  ngOnInit(): void {
    this.stateChanges.subscribe(() => this.cdr.markForCheck());
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
    this.stateChanges.complete();
  }

  onBlur(): void {
    this.focused = false;
    this.ngTouched();
  }

  onFocus(): void {
    this.focused = true;
  }

  onNextDay(): void {
    if (!this.value) {
      return;
    }

    const currentDate = this.value;
    const nextDayDate = moment(currentDate).add(1, 'day').toDate();

    this.onChangeValue({
      value: nextDayDate,
    });
  }

  onDayBefore(): void {
    if (!this.value) {
      return;
    }

    const currentDate = this.value;
    const beforeCurrentDay = moment(currentDate).subtract(1, 'day').toDate();

    this.onChangeValue({
      value: beforeCurrentDay,
    });
  }

  onChangeValue(event: { value: any }): void {
    const { value } = event;

    if (moment(value).isValid()) {
      this.value = value;

      this.ngChange(value);
      this.onChange.emit(value);
      this.stateChanges.next();
    }
  }

  writeValue(writeValue: any): void {
    let value = writeValue;

    if (isNil(value)) {
      this.defaultValue = new Date();
    } else {
      this.defaultValue = value;
    }

    this.value = value;

    this.stateChanges.next();
  }

  registerOnChange(fn: any): void {
    this.ngChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.ngTouched = fn;
  }

  setDisabledState?(isDisabled: boolean): void {
    this.disabled = isDisabled;
    this.stateChanges.next();
  }
}
