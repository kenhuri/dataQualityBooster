import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PickleDataBoosterComponent } from './pickle-data-booster.component';
import { PickleDataBoosterDetailComponent } from './pickle-data-booster-detail.component';
import { PickleDataBoosterPopupComponent } from './pickle-data-booster-dialog.component';
import { PickleDataBoosterDeletePopupComponent } from './pickle-data-booster-delete-dialog.component';

export const pickleRoute: Routes = [
    {
        path: 'pickle-data-booster',
        component: PickleDataBoosterComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dataQualityBoosterApp.pickle.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'pickle-data-booster/:id',
        component: PickleDataBoosterDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dataQualityBoosterApp.pickle.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const picklePopupRoute: Routes = [
    {
        path: 'pickle-data-booster-new',
        component: PickleDataBoosterPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dataQualityBoosterApp.pickle.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'pickle-data-booster/:id/edit',
        component: PickleDataBoosterPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dataQualityBoosterApp.pickle.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'pickle-data-booster/:id/delete',
        component: PickleDataBoosterDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'dataQualityBoosterApp.pickle.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
