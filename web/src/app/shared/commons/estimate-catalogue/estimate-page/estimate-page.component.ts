import {Component, Input, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {DialogService} from 'primeng/dynamicdialog';
import {Store} from '@ngrx/store';
import {isMobile} from '@core/store/core-store.selectors';
import {Subject, distinctUntilChanged, finalize, takeUntil, Observable, of, map} from 'rxjs';
import {Estimate, EstimateApiService, PageEstimate} from "generated/openapi";

@Component({
    selector: 'app-estimate-page',
    templateUrl: './estimate-page.component.html',
    styleUrls: ['./estimate-page.componenet.scss'],
})
export class EstimatePageComponent implements OnInit, OnDestroy {

    @Input() refreshListener$: Observable<void> | undefined;

    isMobile: boolean = false;

    estimates: Estimate[] = []

    page: Page = {size: 20, number: 0}

    pages: Pages | null = null

    private destroy$ = new Subject<void>();

    constructor(
        private store: Store,
        private apiService: EstimateApiService
    ) {
    }

    ngOnInit(): void {
        this.store
            .select(isMobile)
            .pipe(takeUntil(this.destroy$), distinctUntilChanged())
            .subscribe((mobile) => (this.isMobile = mobile));
        this.getPage()
        this.subscribeRefreshListener()

    }

    private subscribeRefreshListener(): void {
        if (this.refreshListener$) {
            this.refreshListener$.pipe(
                takeUntil(this.destroy$))
                .subscribe(() => this.getPage())
        }
    }

    private getPage(): void {
        this.apiService.get(
            undefined,
            undefined,
            this.page.number,
            this.page.size,
            undefined).subscribe((pageEstimate: PageEstimate) => {
            this.page = {
                size: pageEstimate.size ?? this.page.size,
                number: pageEstimate.number ?? this.page.number
            }
            this.pages = {
                totalPages: pageEstimate.totalPages,
                totalElements: pageEstimate.totalElements
            }
            this.estimates = pageEstimate.content ?? []
        });
    }

    ngOnDestroy(): void {
        this.destroy$.next();
        this.destroy$.complete();
    }
}

interface Page {
    size: number,
    number: number,
}

interface Pages {
    totalPages: number | undefined,
    totalElements: number | undefined,
}
