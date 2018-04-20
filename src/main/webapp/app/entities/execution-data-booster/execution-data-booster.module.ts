import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DataQualityBoosterSharedModule } from '../../shared';
import {
    ExecutionDataBoosterService,
    ExecutionDataBoosterPopupService,
    ExecutionDataBoosterComponent,
    ExecutionDataBoosterDetailComponent,
    ExecutionDataBoosterDialogComponent,
    ExecutionDataBoosterPopupComponent,
    ExecutionDataBoosterDeletePopupComponent,
    ExecutionDataBoosterDeleteDialogComponent,
    executionRoute,
    executionPopupRoute,
    ExecutionDataBoosterResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...executionRoute,
    ...executionPopupRoute,
];

@NgModule({
    imports: [
        DataQualityBoosterSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ExecutionDataBoosterComponent,
        ExecutionDataBoosterDetailComponent,
        ExecutionDataBoosterDialogComponent,
        ExecutionDataBoosterDeleteDialogComponent,
        ExecutionDataBoosterPopupComponent,
        ExecutionDataBoosterDeletePopupComponent,
    ],
    entryComponents: [
        ExecutionDataBoosterComponent,
        ExecutionDataBoosterDialogComponent,
        ExecutionDataBoosterPopupComponent,
        ExecutionDataBoosterDeleteDialogComponent,
        ExecutionDataBoosterDeletePopupComponent,
    ],
    providers: [
        ExecutionDataBoosterService,
        ExecutionDataBoosterPopupService,
        ExecutionDataBoosterResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DataQualityBoosterExecutionDataBoosterModule {}
