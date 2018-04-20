import { Component, OnInit, OnDestroy, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { ContextDataBooster } from './context-data-booster.model';
import { ContextDataBoosterPopupService } from './context-data-booster-popup.service';
import { ContextDataBoosterService } from './context-data-booster.service';
import { PythonDataBooster, PythonDataBoosterService } from '../python-data-booster';

@Component({
    selector: 'jhi-context-data-booster-dialog',
    templateUrl: './context-data-booster-dialog.component.html'
})
export class ContextDataBoosterDialogComponent implements OnInit {

    context: ContextDataBooster;
    isSaving: boolean;

    pythons: PythonDataBooster[];

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private contextService: ContextDataBoosterService,
        private pythonService: PythonDataBoosterService,
        private elementRef: ElementRef,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.pythonService.query()
            .subscribe((res: HttpResponse<PythonDataBooster[]>) => { this.pythons = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
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

    clearInputImage(field: string, fieldContentType: string, idInput: string) {
        this.dataUtils.clearInputImage(this.context, this.elementRef, field, fieldContentType, idInput);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.context.id !== undefined) {
            this.subscribeToSaveResponse(
                this.contextService.update(this.context));
        } else {
            this.subscribeToSaveResponse(
                this.contextService.create(this.context));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ContextDataBooster>>) {
        result.subscribe((res: HttpResponse<ContextDataBooster>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: ContextDataBooster) {
        this.eventManager.broadcast({ name: 'contextListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackPythonById(index: number, item: PythonDataBooster) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-context-data-booster-popup',
    template: ''
})
export class ContextDataBoosterPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private contextPopupService: ContextDataBoosterPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.contextPopupService
                    .open(ContextDataBoosterDialogComponent as Component, params['id']);
            } else {
                this.contextPopupService
                    .open(ContextDataBoosterDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
