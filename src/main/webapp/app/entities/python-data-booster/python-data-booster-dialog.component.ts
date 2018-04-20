import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { PythonDataBooster } from './python-data-booster.model';
import { PythonDataBoosterPopupService } from './python-data-booster-popup.service';
import { PythonDataBoosterService } from './python-data-booster.service';

@Component({
    selector: 'jhi-python-data-booster-dialog',
    templateUrl: './python-data-booster-dialog.component.html'
})
export class PythonDataBoosterDialogComponent implements OnInit {

    python: PythonDataBooster;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private pythonService: PythonDataBoosterService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
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
        if (this.python.id !== undefined) {
            this.subscribeToSaveResponse(
                this.pythonService.update(this.python));
        } else {
            this.subscribeToSaveResponse(
                this.pythonService.create(this.python));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<PythonDataBooster>>) {
        result.subscribe((res: HttpResponse<PythonDataBooster>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: PythonDataBooster) {
        this.eventManager.broadcast({ name: 'pythonListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-python-data-booster-popup',
    template: ''
})
export class PythonDataBoosterPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private pythonPopupService: PythonDataBoosterPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.pythonPopupService
                    .open(PythonDataBoosterDialogComponent as Component, params['id']);
            } else {
                this.pythonPopupService
                    .open(PythonDataBoosterDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
