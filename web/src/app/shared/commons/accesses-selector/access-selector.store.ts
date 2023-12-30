import { IAccess } from '@shared/models/accesses';
import { ComponentStore, tapResponse } from '@ngrx/component-store';
import { Injectable } from '@angular/core';
import { IWorkshop } from '@shared/models/workshop';
import { AccountRestApiService } from '@api/services/accountRestApi.service';
import { EMPTY, map, Observable, switchMap, tap } from 'rxjs';
import { WorkshopAccessesDto } from '@api/models/workshopAccessesDto';
import { AccessDto } from '@api/models/accessDto';
import { ToastService } from '@shared/services/toast.service';
import { values } from 'lodash';

interface AccessesState {
  workshops: IWorkshop[];
  selectedWorkshop: IWorkshop | undefined;
  selectedAccess: IAccess | undefined;
}

@Injectable()
export class AccessesSelectorComponentStore extends ComponentStore<AccessesState> {
  constructor(
    private readonly accountApi: AccountRestApiService,
    private readonly toast: ToastService,
  ) {
    super({
      workshops: [],
      selectedWorkshop: undefined,
      selectedAccess: undefined,
    });
    this.loadAccesses();
    this.loadCurrentAccess();
  }

  private loadCurrentAccess() {
    this.accountApi.currentUser().subscribe((accessDto: AccessDto | null) => {
      if (accessDto != null) {
        const access: IAccess = this.mapToIAccess(accessDto);
        this.selectWorkshopByWorkshopId(accessDto.workshopId);
        this.selectAccess(access);
      } else {
        this.selectWorkshop(undefined);
        this.selectAccess(undefined);
      }
    });
  }

  readonly loadAccesses = this.effect((effect$) =>
    effect$.pipe(
      switchMap(() => this.accountApi.getAccesses()),
      map((workshopAccessDtos: Set<WorkshopAccessesDto>) =>
        [...workshopAccessDtos].map(
          (workshopAccessDto: WorkshopAccessesDto): IWorkshop =>
            this.mapToIWorkshop(workshopAccessDto),
        ),
      ),
      tap((workshops: IWorkshop[]) => {
        this.changeWorkshops(workshops);
      }),
    ),
  );

  private mapToIWorkshop(workshopAccessDto: WorkshopAccessesDto): IWorkshop {
    return {
      workshopId: workshopAccessDto.workshopId,
      workshopName: workshopAccessDto.workshopName,
      accesses: [...workshopAccessDto.accesses].map((accessDto: AccessDto) =>
        this.mapToIAccess(accessDto),
      ),
    };
  }

  private mapToIAccess(accessDto: AccessDto): IAccess {
    return {
      isOwnerAccess: accessDto.isOwnerAccess,
      default: false,
      employeeId: accessDto.employeeId,
      employeeFirstName: accessDto.employeeFirstName,
      authorities: [...accessDto.authorities].map((auth) => auth.toString()),
    };
  }

  readonly changeWorkshops = this.updater((state, workshops: IWorkshop[]) => ({
    ...state,
    workshops: workshops,
  }));

  readonly selectWorkshop = this.updater(
    (state, workshop: IWorkshop | undefined) => ({
      ...state,
      selectedWorkshop: workshop,
    }),
  );

  private readonly selectAccess = this.updater(
    (state, access: IAccess | undefined) => ({
      ...state,
      selectedAccess: access,
    }),
  );

  readonly selectWorkshopByWorkshopId = this.updater(
    (state, workshopId: string) => {
      const foundWorkshop = state.workshops.find(
        (w) => w.workshopId === workshopId,
      );
      return {
        ...state,
        selectedWorkshop: foundWorkshop ?? undefined,
        selectedAccess: undefined,
      };
    },
  );

  readonly workshops$ = this.select((state) => state.workshops);

  readonly selectedAccess$ = this.select((state) => state.selectedAccess);

  readonly selectedWorkshop$ = this.select((state) => state.selectedWorkshop);

  readonly selectedWorkshopAccesses$ = this.select(
    (state) => state.selectedWorkshop?.accesses ?? [],
  );

  readonly changeAccess = this.effect(
    (access$: Observable<(IAccess & { workshopId: string }) | undefined>) => {
      return access$.pipe(
        switchMap((access: (IAccess & { workshopId: string }) | undefined) => {
          if (access === undefined || access === null) {
            this.selectAccess(undefined);
            return EMPTY;
          }
          return this.accountApi
            .choseSessionAccess({
              workshopId: access.workshopId,
              employeeId: access.employeeId,
            })
            .pipe(
              tapResponse(
                () => this.selectAccess(access),
                () => this.showUnknownErrorToast(),
              ),
            );
        }),
      );
    },
  );

  private showUnknownErrorToast() {
    this.toast.error({
      title: 'toast.unknown',
      message: 'error.unknown',
      translate: true,
    });
  }
}
