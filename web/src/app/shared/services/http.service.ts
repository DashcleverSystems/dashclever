import {
  HttpClient,
  HttpHeaders,
  HttpParams,
  HttpResponse,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CacheService } from './cache.service';
import { Pageable } from '../types/pageable';
import { HttpOptions } from '../types/http-options';
import { HttpRequestOptions } from '../types/http-request-options';

import { isMoment } from 'moment';

import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { get, isNil, set, size } from 'lodash';
import { environment } from '@env/environments';

interface ApiOptions<T = any> extends HttpOptions {
  type?: T;
}

interface ApiRequestOptions<T = any> extends HttpRequestOptions {
  type?: T;
}

@Injectable()
export class HttpService<O = any> {
  constructor(public http: HttpClient, public cache: CacheService) {}

  /**
   * Request type GET
   *
   * @param url The endpoint URL
   * @param options The request options
   * @param customApiPrefix The custom api prefix
   */
  protected get<T>(
    url: string,
    options: ApiOptions<O> = {},
    customApiPrefix?: string
  ): Observable<T> {
    const apiUrl = `${customApiPrefix ? customApiPrefix + url : url}`;

    return this.http.get<T>(apiUrl, {
      ...options,
      headers: this.getHttpHeaders(options),
      params: this.toHttpParams(options.params),
    });
  }

  /**
   * Request type POST with pagination
   *
   * @param url The endpoint URL
   * @param options The request options
   * @param customApiPrefix The custom api prefix
   */
  protected getCollection<T>(
    url: string,
    options: ApiRequestOptions<O> = {},
    method: string = 'GET',
    customApiPrefix?: string
  ): Observable<Pageable<T>> {
    const apiUrl = `${customApiPrefix ? customApiPrefix + url : url}`;
    return this.http
      .request(method, apiUrl, {
        ...options,
        observe: 'response',
        body: this.formatPost(options.body),
        headers: this.getHttpHeaders(options),
        params: this.toHttpParams(options.params),
      })
      .pipe(
        map((response: HttpResponse<T[]>) => ({
          totalElements: parseInt(response.headers.get('X-Total-Count'), 10),
          content: response.body,
        }))
      );
  }

  /**
   * Request type POST
   *
   * @param url The endpoint URL
   * @param data data to send
   * @param options The request options
   * @param customApiPrefix The custom api prefix
   */
  protected post<T>(
    url: string,
    data: any,
    options: ApiOptions<O> = {},
    customApiPrefix?: string
  ): Observable<T> {
    const apiUrl = `${customApiPrefix ? customApiPrefix + url : url}`;

    return this.http.post<T>(apiUrl, this.formatPost(data), {
      ...options,
      headers: this.getHttpHeaders(options),
      params: this.toHttpParams(options.params),
    });
  }

  /**
   * Request type PUT
   *
   * @param url The endpoint URL
   * @param data data to send
   * @param options The request options
   * @param customApiPrefix The custom api prefix
   */
  protected put<T>(
    url: string,
    data: any,
    options: ApiOptions<O> = {},
    customApiPrefix?: string
  ): Observable<T> {
    const apiUrl = `${customApiPrefix ? customApiPrefix + url : url}`;

    return this.http.put<T>(apiUrl, this.formatPost(data), {
      ...options,
      headers: this.getHttpHeaders(options),
      params: this.toHttpParams(options.params),
    });
  }

  /**
   * Request type DELETE
   *
   * @param url The endpoint URL
   * @param options The request options
   * @param customApiPrefix The custom api prefix
   */
  protected delete<T>(
    url: string,
    options: ApiOptions<O> = {},
    customApiPrefix?: string
  ): Observable<T> {
    const apiUrl = `${customApiPrefix ? customApiPrefix + url : url}`;

    return this.http.delete<T>(apiUrl, {
      ...options,
      headers: this.getHttpHeaders(options),
      params: this.toHttpParams(options.params),
    });
  }

  /**
   * Request to get any request type
   *
   * @param method The request method
   * @param url The endpoint URL
   * @param options The request options
   * @param customApiPrefix The custom api prefix
   */
  protected request(
    method: string,
    url: string,
    options: ApiRequestOptions<O> = {},
    customApiPrefix?: string
  ): Observable<any> {
    const apiUrl = `${customApiPrefix ? customApiPrefix + url : url}`;

    return this.http.request(method, apiUrl, {
      ...options,
      body: this.formatPost(options.body),
      headers: this.getHttpHeaders(options),
      params: this.toHttpParams(options.params),
    });
  }

  /**
   * Format data to date format with time
   *
   * @param data The data
   * @param paths The paths to format
   */
  protected formatDateTime(data: any, paths: string[]) {
    paths.forEach((path: string) => {
      let value = get(data, path);

      if (isMoment(value)) {
        value = value.format('YYYY-MM-DD HH:mm:ss');
      } else if (value instanceof Date) {
        value = moment(value).format('YYYY-MM-DD HH:mm:ss');
      }

      set(data, path, value);
    });

    return data;
  }

  /**
   * Prepared http params
   *
   * @param params The object data
   */
  protected toHttpParams(
    data: object,
    paramsData: HttpParams = new HttpParams(),
    prefix: string = null
  ): HttpParams {
    if (!data) {
      return new HttpParams();
    }

    return Object.entries(data)
      .filter(
        ([, value]) =>
          !isNil(value) && (size(value) > 0 || !Array.isArray(value))
      )
      .reduce((params: HttpParams, [key, value]) => {
        const valueKey = prefix ? `${prefix}[${key}]` : key;

        if (Array.isArray(value) && key === 'sort') {
          value.forEach((paramValue) => {
            params = params.append(
              valueKey,
              this.preparedHttpParamsValue(paramValue)
            );
          });

          return params;
        } else if (
          typeof value === 'object' &&
          !Array.isArray(value) &&
          !isMoment(value) &&
          !(value instanceof Date)
        ) {
          return this.toHttpParams(value, params, key);
        }

        return params.append(valueKey, this.preparedHttpParamsValue(value));
      }, paramsData);
  }

  /**
   * Prepared data to send
   *
   * @param data data to send
   */
  protected formatPost(data: any): any {
    if (!data) {
      return null;
    }

    if (data instanceof FormData) {
      return data;
    }

    if (Array.isArray(data)) {
      const formattedArray = [];

      data.forEach((value: any, index: number) => {
        formattedArray[index] = value;

        if (value instanceof Object) {
          formattedArray[index] = this.formatPost(value);
        }
      });

      return formattedArray;
    } else if (data instanceof Object) {
      const formattedData = {};

      Object.keys(data).forEach((key) => {
        const value = data[key];
        if (isMoment(value) || value instanceof Date) {
          formattedData[key] = moment(value).format('YYYY-MM-DD');
        } else if (value instanceof Map) {
          formattedData[key] = Object.fromEntries(value);
        } else if (value instanceof Object) {
          formattedData[key] = this.formatPost(value);
        } else {
          formattedData[key] = value;
        }
      });

      return formattedData;
    }

    return data;
  }

  /**
   * Get HTTP headers with request-type
   */
  private getHttpHeaders(options: ApiOptions | ApiRequestOptions):
    | HttpHeaders
    | {
        [header: string]: string | string[];
      }
    | undefined {
    if (!options.type) {
      return options.headers;
    }

    return {
      ...options.headers,
      'request-type': options.type.toString(),
    };
  }

  /**
   * Convert value to string for params
   *
   * @param value The value
   */
  private preparedHttpParamsValue(value: any): string {
    if (value instanceof String) {
      return value.toString();
    } else if (isMoment(value)) {
      return value.format('YYYY-MM-DD');
    } else if (value instanceof Date) {
      return moment(value).format('YYYY-MM-DD');
    } else if (Array.isArray(value)) {
      return value.join(',');
    }

    return value.toString();
  }
}
