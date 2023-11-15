import * as moment from 'moment';
import * as generateHash from 'object-hash';

import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

// TODO
@Injectable()
export class CacheService {
  readonly CACHE_DURATION_IN_MINUTES = 5;

  private cache: {
    [key: string]: {
      [hash: string]: {
        expires: Date;
        value: Observable<any[]>;
      };
    };
  } = {};

  getParametersHash(params: object): string {
    return generateHash(params);
  }

  getValue(key: string, hash: string): Observable<any[]> {
    if (!this.cache) {
      return null;
    }

    if (!this.cache[key] || !this.cache[key][hash]) {
      return null;
    }

    if (moment(new Date()).isAfter(this.cache[key][hash].expires)) {
      return null;
    }

    return this.cache[key][hash].value;
  }

  setValue(key: string, hash: string, value: Observable<any[]>): void {
    this.cache = {
      ...this.cache,
      [key]: {
        ...this.cache[key],
        [hash]: {
          value,
          expires: moment(new Date())
            .add(this.CACHE_DURATION_IN_MINUTES, 'minutes')
            .toDate(),
        },
      },
    };
  }

  clearCache(key?: string, hash?: string): void {
    if (key && this.cache[key]) {
      if (hash) {
        this.cache[key][hash] = null;
      } else {
        this.cache[key] = null;
      }
    }

    if (!key) {
      this.cache = {};
    }
  }
}
