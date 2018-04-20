import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { PythonDataBooster } from './python-data-booster.model';
import { PythonDataBoosterService } from './python-data-booster.service';

@Component({
    selector: 'jhi-python-data-booster-detail',
    templateUrl: './python-data-booster-detail.component.html'
})
export class PythonDataBoosterDetailComponent implements OnInit, OnDestroy {

    python: PythonDataBooster;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private pythonService: PythonDataBoosterService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInPythons();
    }

    load(id) {
        this.pythonService.find(id)
            .subscribe((pythonResponse: HttpResponse<PythonDataBooster>) => {
                this.python = pythonResponse.body;
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

    registerChangeInPythons() {
        this.eventSubscriber = this.eventManager.subscribe(
            'pythonListModification',
            (response) => this.load(this.python.id)
        );
    }
}
