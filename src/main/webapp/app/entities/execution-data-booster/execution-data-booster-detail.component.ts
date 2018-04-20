import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { ExecutionDataBooster } from './execution-data-booster.model';
import { ExecutionDataBoosterService } from './execution-data-booster.service';

@Component({
    selector: 'jhi-execution-data-booster-detail',
    templateUrl: './execution-data-booster-detail.component.html'
})
export class ExecutionDataBoosterDetailComponent implements OnInit, OnDestroy {

    execution: ExecutionDataBooster;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private executionService: ExecutionDataBoosterService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInExecutions();
    }

    load(id) {
        this.executionService.find(id)
            .subscribe((executionResponse: HttpResponse<ExecutionDataBooster>) => {
                this.execution = executionResponse.body;
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

    registerChangeInExecutions() {
        this.eventSubscriber = this.eventManager.subscribe(
            'executionListModification',
            (response) => this.load(this.execution.id)
        );
    }
}
