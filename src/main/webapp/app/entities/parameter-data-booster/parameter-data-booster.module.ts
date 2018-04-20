import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DataQualityBoosterSharedModule } from '../../shared';
import {
    ParameterDataBoosterService,
    ParameterDataBoosterPopupService,
    ParameterDataBoosterComponent,
    ParameterDataBoosterDetailComponent,
    ParameterDataBoosterDialogComponent,
    ParameterDataBoosterPopupComponent,
    ParameterDataBoosterDeletePopupComponent,
    ParameterDataBoosterDeleteDialogComponent,
    parameterRoute,
    parameterPopupRoute,
} from './';

const ENTITY_STATES = [
    ...parameterRoute,
    ...parameterPopupRoute,
];

@NgModule({
    imports: [
        DataQualityBoosterSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ParameterDataBoosterComponent,
        ParameterDataBoosterDetailComponent,
        ParameterDataBoosterDialogComponent,
        ParameterDataBoosterDeleteDialogComponent,
        ParameterDataBoosterPopupComponent,
        ParameterDataBoosterDeletePopupComponent,
    ],
    entryComponents: [
        ParameterDataBoosterComponent,
        ParameterDataBoosterDialogComponent,
        ParameterDataBoosterPopupComponent,
        ParameterDataBoosterDeleteDialogComponent,
        ParameterDataBoosterDeletePopupComponent,
    ],
    providers: [
        ParameterDataBoosterService,
        ParameterDataBoosterPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DataQualityBoosterParameterDataBoosterModule {}
