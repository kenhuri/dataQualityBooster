import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { ParameterDataBooster } from './parameter-data-booster.model';
import { ParameterDataBoosterService } from './parameter-data-booster.service';

@Component({
    selector: 'jhi-parameter-data-booster-detail',
    templateUrl: './parameter-data-booster-detail.component.html'
})
export class ParameterDataBoosterDetailComponent implements OnInit, OnDestroy {

    parameter: ParameterDataBooster;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private parameterService: ParameterDataBoosterService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInParameters();
    }

    load(id) {
        this.parameterService.find(id)
            .subscribe((parameterResponse: HttpResponse<ParameterDataBooster>) => {
                this.parameter = parameterResponse.body;
            });
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInParameters() {
        this.eventSubscriber = this.eventManager.subscribe(
            'parameterListModification',
            (response) => this.load(this.parameter.id)
        );
    }
}
