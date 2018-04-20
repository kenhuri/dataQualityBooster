import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { ExecutionDataBooster } from './execution-data-booster.model';
import { ExecutionDataBoosterPopupService } from './execution-data-booster-popup.service';
import { ExecutionDataBoosterService } from './execution-data-booster.service';
import { ContextDataBooster, ContextDataBoosterService } from '../context-data-booster';

@Component({
    selector: 'jhi-execution-data-booster-dialog',
    templateUrl: './execution-data-booster-dialog.component.html'
})
export class ExecutionDataBoosterDialogComponent implements OnInit {

    execution: ExecutionDataBooster;
    isSaving: boolean;

    contexts: ContextDataBooster[];

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private executionService: ExecutionDataBoosterService,
        private contextService: ContextDataBoosterService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.contextService.query()
            .subscribe((res: HttpResponse<ContextDataBooster[]>) => { this.contexts = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.execution.id !== undefined) {
            this.subscribeToSaveResponse(
                this.executionService.update(this.execution));
        } else {
            this.subscribeToSaveResponse(
                this.executionService.create(this.execution));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ExecutionDataBooster>>) {
        result.subscribe((res: HttpResponse<ExecutionDataBooster>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: ExecutionDataBooster) {
        this.eventManager.broadcast({ name: 'executionListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackContextById(index: number, item: ContextDataBooster) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-execution-data-booster-popup',
    template: ''
})
export class ExecutionDataBoosterPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private executionPopupService: ExecutionDataBoosterPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.executionPopupService
                    .open(ExecutionDataBoosterDialogComponent as Component, params['id']);
            } else {
                this.executionPopupService
                    .open(ExecutionDataBoosterDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
