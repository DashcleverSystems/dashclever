import {
  AfterViewInit,
  Component,
  ElementRef,
  Input,
  OnDestroy,
  Output,
  ViewChild,
} from '@angular/core';
import { isEqual } from 'lodash';
import { Observable, Subject } from 'rxjs';

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
  @Output() clicked: Subject<void> = new Subject<void>();

  abstract itemList: Observable<T[]>;
  value: string = 'value';
  title: string = 'Choose Item';
  itemName: string = 'Item';
  initialValue: T | undefined;

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

  abstract onClick(index: number, item: T | null): void;

  onSelected(index: number, item: T, itemRef: HTMLDivElement): void {
    const isSelected = itemRef.classList.contains('selected');
    this.addClassToSelectedItem(itemRef);

    if (isSelected) {
      this.onClick(index, null);
    } else {
      this.onClick(index, item);
    }
  }

  defineInitialValue(): void {
    if (this.listContainer && this.initialValue) {
      const listItems =
        this.listContainer.nativeElement.querySelectorAll('.item');
      const itemsWithIds: { item: Element; id: string | number | null }[] = [];

      const hasId = this.hasId(this.initialValue);
      listItems.forEach((listItem) => {
        const listItemId = listItem.getAttribute('id');
        if (listItemId) {
          const itemToPush = {
            item: listItem,
            id: hasId
              ? listItemId.split('-').at(-2) ?? null
              : listItemId.split('-').at(-1) ?? null,
          };
          itemsWithIds.push(itemToPush);
        }
      });

      const selectedItem = itemsWithIds.find((item) =>
        hasId
          ? item.id === (this.initialValue as any)['id']
          : isEqual(item, this.initialValue)
      );

      if (selectedItem) {
        selectedItem.item.classList.add('selected');
      }
    }
  }

  private addClassToSelectedItem(el: HTMLDivElement): void {
    if (el.classList.contains('selected')) {
      el.classList.remove('selected');
    } else {
      this.removeSelected();
      el.classList.add('selected');
    }
  }

  private removeSelected(): void {
    if (this.listContainer) {
      const selectedItems =
        this.listContainer.nativeElement.querySelectorAll('.item.selected');
      selectedItems.forEach((item) => item.classList.remove('selected'));
    }
  }

  private hasId(obj: any): boolean {
    return typeof obj === 'object' && 'id' in obj;
  }
}
