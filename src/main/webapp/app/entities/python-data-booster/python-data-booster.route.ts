import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PythonDataBoosterComponent } from './python-data-booster.component';
import { PythonDataBoosterDetailComponent } from './python-data-booster-detail.component';
import { PythonDataBoosterPopupComponent } from './python-data-booster-dialog.component';
import { PythonDataBoosterDeletePopupComponent } from './python-data-booster-delete-dialog.component';

export const pythonRoute: Routes = [
    {
        path: 'python-data-booster',
        component: PythonDataBoosterComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dataQualityBoosterApp.python.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'python-data-booster/:id',
        component: PythonDataBoosterDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dataQualityBoosterApp.python.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const pythonPopupRoute: Routes = [
    {
        path: 'python-data-booster-new',
        component: PythonDataBoosterPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dataQualityBoosterApp.python.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'python-data-booster/:id/edit',
        component: PythonDataBoosterPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dataQualityBoosterApp.python.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'python-data-booster/:id/delete',
        component: PythonDataBoosterDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dataQualityBoosterApp.python.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
