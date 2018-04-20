import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { PickleDataBooster } from './pickle-data-booster.model';
import { PickleDataBoosterService } from './pickle-data-booster.service';

@Component({
    selector: 'jhi-pickle-data-booster-detail',
    templateUrl: './pickle-data-booster-detail.component.html'
})
export class PickleDataBoosterDetailComponent implements OnInit, OnDestroy {

    pickle: PickleDataBooster;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private pickleService: PickleDataBoosterService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInPickles();
    }

    load(id) {
        this.pickleService.find(id)
            .subscribe((pickleResponse: HttpResponse<PickleDataBooster>) => {
                this.pickle = pickleResponse.body;
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

    registerChangeInPickles() {
        this.eventSubscriber = this.eventManager.subscribe(
            'pickleListModification',
            (response) => this.load(this.pickle.id)
        );
    }
}
