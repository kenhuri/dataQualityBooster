import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { ParameterDataBooster } from './parameter-data-booster.model';
import { ParameterDataBoosterPopupService } from './parameter-data-booster-popup.service';
import { ParameterDataBoosterService } from './parameter-data-booster.service';
import { ContextDataBooster, ContextDataBoosterService } from '../context-data-booster';

@Component({
    selector: 'jhi-parameter-data-booster-dialog',
    templateUrl: './parameter-data-booster-dialog.component.html'
})
export class ParameterDataBoosterDialogComponent implements OnInit {

    parameter: ParameterDataBooster;
    isSaving: boolean;

    contexts: ContextDataBooster[];

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private parameterService: ParameterDataBoosterService,
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
        if (this.parameter.id !== undefined) {
            this.subscribeToSaveResponse(
                this.parameterService.update(this.parameter));
        } else {
            this.subscribeToSaveResponse(
                this.parameterService.create(this.parameter));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ParameterDataBooster>>) {
        result.subscribe((res: HttpResponse<ParameterDataBooster>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: ParameterDataBooster) {
        this.eventManager.broadcast({ name: 'parameterListModification', content: 'OK'});
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
    selector: 'jhi-parameter-data-booster-popup',
    template: ''
})
export class ParameterDataBoosterPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private parameterPopupService: ParameterDataBoosterPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.parameterPopupService
                    .open(ParameterDataBoosterDialogComponent as Component, params['id']);
            } else {
                this.parameterPopupService
                    .open(ParameterDataBoosterDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
