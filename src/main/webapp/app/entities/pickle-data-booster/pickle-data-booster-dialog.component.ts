import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { PickleDataBooster } from './pickle-data-booster.model';
import { PickleDataBoosterPopupService } from './pickle-data-booster-popup.service';
import { PickleDataBoosterService } from './pickle-data-booster.service';
import { ContextDataBooster, ContextDataBoosterService } from '../context-data-booster';

@Component({
    selector: 'jhi-pickle-data-booster-dialog',
    templateUrl: './pickle-data-booster-dialog.component.html'
})
export class PickleDataBoosterDialogComponent implements OnInit {

    pickle: PickleDataBooster;
    isSaving: boolean;

    contexts: ContextDataBooster[];

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private pickleService: PickleDataBoosterService,
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
        if (this.pickle.id !== undefined) {
            this.subscribeToSaveResponse(
                this.pickleService.update(this.pickle));
        } else {
            this.subscribeToSaveResponse(
                this.pickleService.create(this.pickle));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<PickleDataBooster>>) {
        result.subscribe((res: HttpResponse<PickleDataBooster>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: PickleDataBooster) {
        this.eventManager.broadcast({ name: 'pickleListModification', content: 'OK'});
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
    selector: 'jhi-pickle-data-booster-popup',
    template: ''
})
export class PickleDataBoosterPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private picklePopupService: PickleDataBoosterPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.picklePopupService
                    .open(PickleDataBoosterDialogComponent as Component, params['id']);
            } else {
                this.picklePopupService
                    .open(PickleDataBoosterDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
