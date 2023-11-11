import { HttpHeaders } from '@angular/common/http';

type HttpObserveType = 'body' | 'events' | 'response';

export class HttpRequestOptions {
  body?: any;
  headers?:
    | HttpHeaders
    | {
        [header: string]: string | string[];
      };
  params?: object;
  observe?: HttpObserveType;
  reportProgress?: boolean;
  responseType?: 'arraybuffer' | 'blob' | 'json' | 'text';
  withCredentials?: boolean;
}
