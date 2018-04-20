import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { DataQualityBoosterSharedModule } from '../../shared';
import {
    PickleDataBoosterService,
    PickleDataBoosterPopupService,
    PickleDataBoosterComponent,
    PickleDataBoosterDetailComponent,
    PickleDataBoosterDialogComponent,
    PickleDataBoosterPopupComponent,
    PickleDataBoosterDeletePopupComponent,
    PickleDataBoosterDeleteDialogComponent,
    pickleRoute,
    picklePopupRoute,
} from './';

const ENTITY_STATES = [
    ...pickleRoute,
    ...picklePopupRoute,
];

@NgModule({
    imports: [
        DataQualityBoosterSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        PickleDataBoosterComponent,
        PickleDataBoosterDetailComponent,
        PickleDataBoosterDialogComponent,
        PickleDataBoosterDeleteDialogComponent,
        PickleDataBoosterPopupComponent,
        PickleDataBoosterDeletePopupComponent,
    ],
    entryComponents: [
        PickleDataBoosterComponent,
        PickleDataBoosterDialogComponent,
        PickleDataBoosterPopupComponent,
        PickleDataBoosterDeleteDialogComponent,
        PickleDataBoosterDeletePopupComponent,
    ],
    providers: [
        PickleDataBoosterService,
        PickleDataBoosterPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DataQualityBoosterPickleDataBoosterModule {}
