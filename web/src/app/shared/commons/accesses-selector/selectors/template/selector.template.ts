import {
  AfterViewInit,
  Component,
  ElementRef,
  Input,
  OnDestroy,
  Output,
  ViewChild,
} from '@angular/core';
import { Observable, Subject, take } from 'rxjs';
import { isEqual } from 'lodash';

@Component({
  template: '',
})
export abstract class SelectorListComponent<T>
  implements AfterViewInit, OnDestroy
{
  @ViewChild('listContainer') listContainer:
    | ElementRef<HTMLUListElement>
    | undefined;
  @Input() visible: boolean = false;
  @Output() clicked: Subject<T | null> = new Subject<T | null>();

  abstract itemList: Observable<T[]>;
  abstract itemName: string;
  value: string = 'value';
  alternativeValue: string = 'alternativeValue';
  title: string = 'Choose Item';

  protected selected: { [key: string]: any } = {};

  itemId(item: any, index: number): string {
    const haveId = this.hasId(item);
    const id = haveId ? item['id'] + '-' + index : index;

    return `listItemEl-${this.itemName}-${id}`;
  }

  protected destroy$ = new Subject<void>();

  ngAfterViewInit(): void {
    this.defineInitialValue();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  abstract onClick(index: number, item: T | undefined): void;
  abstract defineInitialValue(): void;

  onSelected(index: number, item: T, itemRef: HTMLDivElement): void {
    const id = this.itemId(item, index);
    const isSelected =
      itemRef.classList.contains('selected') || this.selected[id];
    this.addClassToSelectedItem(itemRef, id);

    if (isSelected) {
      this.onClick(index, undefined);
      this.clicked.next(null);
    } else {
      this.onClick(index, item);
      this.clicked.next(item);
    }
  }

  protected selectSpecificItem(item: T) {
    this.itemList.pipe(take(1)).subscribe((list) => {
      const index = list.findIndex((i) => isEqual(i, item));
      const id = this.itemId(item, index);
      const selectedItem = this.listContainer?.nativeElement.querySelector(
        `div[id="${id}"]`,
      );
      this.addClassToSelectedItem(selectedItem as HTMLDivElement, id);
    });
  }

  private addClassToSelectedItem(el: HTMLDivElement, id: string): void {
    if (el.classList.contains('selected')) {
      el.classList.remove('selected');
      this.selected[id] = undefined;
    } else {
      this.removeSelected();
      el.classList.add('selected');
      this.selected[id] = true;
    }
  }

  protected removeSelected(): void {
    if (this.listContainer) {
      const selectedItems =
        this.listContainer.nativeElement.querySelectorAll('.item.selected');
      selectedItems.forEach((item) => item.classList.remove('selected'));
      this.selected = {};
    }
  }

  private hasId(obj: any): boolean {
    return typeof obj === 'object' && 'id' in obj;
  }
}
