import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DataQualityBoosterSharedModule } from '../../shared';
import {
    ContextDataBoosterService,
    ContextDataBoosterPopupService,
    ContextDataBoosterComponent,
    ContextDataBoosterDetailComponent,
    ContextDataBoosterDialogComponent,
    ContextDataBoosterPopupComponent,
    ContextDataBoosterDeletePopupComponent,
    ContextDataBoosterDeleteDialogComponent,
    contextRoute,
    contextPopupRoute,
    ContextDataBoosterResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...contextRoute,
    ...contextPopupRoute,
];

@NgModule({
    imports: [
        DataQualityBoosterSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ContextDataBoosterComponent,
        ContextDataBoosterDetailComponent,
        ContextDataBoosterDialogComponent,
        ContextDataBoosterDeleteDialogComponent,
        ContextDataBoosterPopupComponent,
        ContextDataBoosterDeletePopupComponent,
    ],
    entryComponents: [
        ContextDataBoosterComponent,
        ContextDataBoosterDialogComponent,
        ContextDataBoosterPopupComponent,
        ContextDataBoosterDeleteDialogComponent,
        ContextDataBoosterDeletePopupComponent,
    ],
    providers: [
        ContextDataBoosterService,
        ContextDataBoosterPopupService,
        ContextDataBoosterResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DataQualityBoosterContextDataBoosterModule {}
