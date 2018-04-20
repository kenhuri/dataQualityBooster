import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { ContextDataBoosterComponent } from './context-data-booster.component';
import { ContextDataBoosterDetailComponent } from './context-data-booster-detail.component';
import { ContextDataBoosterPopupComponent } from './context-data-booster-dialog.component';
import { ContextDataBoosterDeletePopupComponent } from './context-data-booster-delete-dialog.component';

@Injectable()
export class ContextDataBoosterResolvePagingParams implements Resolve<any> {

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

export const contextRoute: Routes = [
    {
        path: 'context-data-booster',
        component: ContextDataBoosterComponent,
        resolve: {
            'pagingParams': ContextDataBoosterResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dataQualityBoosterApp.context.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'context-data-booster/:id',
        component: ContextDataBoosterDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dataQualityBoosterApp.context.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const contextPopupRoute: Routes = [
    {
        path: 'context-data-booster-new',
        component: ContextDataBoosterPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dataQualityBoosterApp.context.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'context-data-booster/:id/edit',
        component: ContextDataBoosterPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dataQualityBoosterApp.context.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'context-data-booster/:id/delete',
        component: ContextDataBoosterDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dataQualityBoosterApp.context.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
