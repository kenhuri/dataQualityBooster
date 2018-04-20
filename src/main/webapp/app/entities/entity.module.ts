import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { DataQualityBoosterContextDataBoosterModule } from './context-data-booster/context-data-booster.module';
import { DataQualityBoosterPickleDataBoosterModule } from './pickle-data-booster/pickle-data-booster.module';
import { DataQualityBoosterParameterDataBoosterModule } from './parameter-data-booster/parameter-data-booster.module';
import { DataQualityBoosterPythonDataBoosterModule } from './python-data-booster/python-data-booster.module';
import { DataQualityBoosterExecutionDataBoosterModule } from './execution-data-booster/execution-data-booster.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        DataQualityBoosterContextDataBoosterModule,
        DataQualityBoosterPickleDataBoosterModule,
        DataQualityBoosterParameterDataBoosterModule,
        DataQualityBoosterPythonDataBoosterModule,
        DataQualityBoosterExecutionDataBoosterModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DataQualityBoosterEntityModule {}
