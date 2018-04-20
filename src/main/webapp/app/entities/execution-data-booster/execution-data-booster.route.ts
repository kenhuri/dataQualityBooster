import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { ExecutionDataBoosterComponent } from './execution-data-booster.component';
import { ExecutionDataBoosterDetailComponent } from './execution-data-booster-detail.component';
import { ExecutionDataBoosterPopupComponent } from './execution-data-booster-dialog.component';
import { ExecutionDataBoosterDeletePopupComponent } from './execution-data-booster-delete-dialog.component';

@Injectable()
export class ExecutionDataBoosterResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const executionRoute: Routes = [
    {
        path: 'execution-data-booster',
        component: ExecutionDataBoosterComponent,
        resolve: {
            'pagingParams': ExecutionDataBoosterResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dataQualityBoosterApp.execution.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'execution-data-booster/:id',
        component: ExecutionDataBoosterDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dataQualityBoosterApp.execution.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const executionPopupRoute: Routes = [
    {
        path: 'execution-data-booster-new',
        component: ExecutionDataBoosterPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dataQualityBoosterApp.execution.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'execution-data-booster/:id/edit',
        component: ExecutionDataBoosterPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dataQualityBoosterApp.execution.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'execution-data-booster/:id/delete',
        component: ExecutionDataBoosterDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dataQualityBoosterApp.execution.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
