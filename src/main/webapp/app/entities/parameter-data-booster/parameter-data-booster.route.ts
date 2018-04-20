import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { ParameterDataBoosterComponent } from './parameter-data-booster.component';
import { ParameterDataBoosterDetailComponent } from './parameter-data-booster-detail.component';
import { ParameterDataBoosterPopupComponent } from './parameter-data-booster-dialog.component';
import { ParameterDataBoosterDeletePopupComponent } from './parameter-data-booster-delete-dialog.component';

export const parameterRoute: Routes = [
    {
        path: 'parameter-data-booster',
        component: ParameterDataBoosterComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dataQualityBoosterApp.parameter.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'parameter-data-booster/:id',
        component: ParameterDataBoosterDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dataQualityBoosterApp.parameter.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const parameterPopupRoute: Routes = [
    {
        path: 'parameter-data-booster-new',
        component: ParameterDataBoosterPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dataQualityBoosterApp.parameter.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'parameter-data-booster/:id/edit',
        component: ParameterDataBoosterPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dataQualityBoosterApp.parameter.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'parameter-data-booster/:id/delete',
        component: ParameterDataBoosterDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dataQualityBoosterApp.parameter.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
