import { Component, OnDestroy, OnInit, SkipSelf } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';

type RoutingItem = {
  name: string;
  value: RoutesType;
};

enum RoutesType {
  PLANNING = 'PLANNING',
  ESTIMATE_CATALOGUE = 'ESTIMATE-CATALOGUE',
}

@Component({
  selector: 'app-insight-repair-panel',
  templateUrl: './insight-repair-panel.component.html',
  styleUrls: ['./insight-repair-panel.component.scss'],
})
export class InsightRepairPanelComponent implements OnInit, OnDestroy {
  items: RoutingItem[] = [
    {
      name: 'panels.insightRepairPanel.routes.planning',
      value: RoutesType.PLANNING,
    },
    {
      name: 'panels.insightRepairPanel.routes.estimateCatalogue',
      value: RoutesType.ESTIMATE_CATALOGUE,
    },
  ];

  selected: RoutesType;

  private destroy$ = new Subject<void>();

  constructor(@SkipSelf() private router: Router) {}

  ngOnInit(): void {
    this.setCorrectLink();
    this.subscribeRouteChange();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  routeChange({ value }: any) {
    this.changeRoute(value);
  }

  private subscribeRouteChange(): void {
    this.router.events.pipe(takeUntil(this.destroy$)).subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.setCorrectLink();
      }
    });
  }

  private setCorrectLink() {
    const url = this.router.url;

    const lastRoute = url.split('/').at(-1);
    if (lastRoute === 'insight-repair') {
      this.setDefault();
    } else this.changeRoute(lastRoute.toUpperCase() as RoutesType);
  }

  private changeRoute(route: RoutesType) {
    switch (route) {
      case RoutesType.PLANNING: {
        this.router.navigate(['/insight-repair/planning']);
        this.selected = route;

        break;
      }
      case RoutesType.ESTIMATE_CATALOGUE: {
        this.router.navigate(['/insight-repair/estimate-catalogue']);
        this.selected = route;
        break;
      }
    }
  }

  private setDefault() {
    this.selected = RoutesType.PLANNING;
    this.changeRoute(this.selected);
  }
}
