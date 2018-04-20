import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { ContextDataBooster } from './context-data-booster.model';
import { ContextDataBoosterService } from './context-data-booster.service';

@Component({
    selector: 'jhi-context-data-booster-detail',
    templateUrl: './context-data-booster-detail.component.html'
})
export class ContextDataBoosterDetailComponent implements OnInit, OnDestroy {

    context: ContextDataBooster;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private contextService: ContextDataBoosterService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInContexts();
    }

    load(id) {
        this.contextService.find(id)
            .subscribe((contextResponse: HttpResponse<ContextDataBooster>) => {
                this.context = contextResponse.body;
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

    registerChangeInContexts() {
        this.eventSubscriber = this.eventManager.subscribe(
            'contextListModification',
            (response) => this.load(this.context.id)
        );
    }
}
