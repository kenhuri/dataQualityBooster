import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ParameterDataBooster } from './parameter-data-booster.model';
import { ParameterDataBoosterPopupService } from './parameter-data-booster-popup.service';
import { ParameterDataBoosterService } from './parameter-data-booster.service';

@Component({
    selector: 'jhi-parameter-data-booster-delete-dialog',
    templateUrl: './parameter-data-booster-delete-dialog.component.html'
})
export class ParameterDataBoosterDeleteDialogComponent {

    parameter: ParameterDataBooster;

    constructor(
        private parameterService: ParameterDataBoosterService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.parameterService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'parameterListModification',
                content: 'Deleted an parameter'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-parameter-data-booster-delete-popup',
    template: ''
})
export class ParameterDataBoosterDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private parameterPopupService: ParameterDataBoosterPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.parameterPopupService
                .open(ParameterDataBoosterDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
