import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Message, MessageService } from 'primeng/api';

export interface IToastMessage {
  title?: string;
  message: string;
  translate?: boolean;
  options?: Omit<Message, 'severity' | 'summary' | 'detail'>;
}

@Injectable({
  providedIn: 'root',
})
export class ToastService {
  constructor(
    private messageService: MessageService,
    private translate: TranslateService,
  ) {}

  success(data: IToastMessage): void {
    this.show('success', data);
  }

  warn(data: IToastMessage): void {
    this.show('warn', data);
  }

  error(data: IToastMessage, time?: number): void {
    this.show('error', data, time);
  }

  private show(
    type: 'success' | 'warn' | 'error',
    data: IToastMessage,
    time?: number,
  ): void {
    if (data.translate) {
      data = this.translateOptions(data);
    }

    this.messageService.add({
      severity: type,
      summary: data.title ?? '',
      detail: data.message,
      life: time ?? 1500,
      ...data.options,
    });
  }

  private translateOptions(data: IToastMessage): IToastMessage {
    return {
      ...data,
      title: data.title && this.translate.instant(data.title),
      message: this.translate.instant(data.message),
    };
  }
}
