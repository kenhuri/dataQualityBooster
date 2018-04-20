import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DataQualityBoosterSharedModule } from '../../shared';
import {
    PythonDataBoosterService,
    PythonDataBoosterPopupService,
    PythonDataBoosterComponent,
    PythonDataBoosterDetailComponent,
    PythonDataBoosterDialogComponent,
    PythonDataBoosterPopupComponent,
    PythonDataBoosterDeletePopupComponent,
    PythonDataBoosterDeleteDialogComponent,
    pythonRoute,
    pythonPopupRoute,
} from './';

const ENTITY_STATES = [
    ...pythonRoute,
    ...pythonPopupRoute,
];

@NgModule({
    imports: [
        DataQualityBoosterSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        PythonDataBoosterComponent,
        PythonDataBoosterDetailComponent,
        PythonDataBoosterDialogComponent,
        PythonDataBoosterDeleteDialogComponent,
        PythonDataBoosterPopupComponent,
        PythonDataBoosterDeletePopupComponent,
    ],
    entryComponents: [
        PythonDataBoosterComponent,
        PythonDataBoosterDialogComponent,
        PythonDataBoosterPopupComponent,
        PythonDataBoosterDeleteDialogComponent,
        PythonDataBoosterDeletePopupComponent,
    ],
    providers: [
        PythonDataBoosterService,
        PythonDataBoosterPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DataQualityBoosterPythonDataBoosterModule {}
