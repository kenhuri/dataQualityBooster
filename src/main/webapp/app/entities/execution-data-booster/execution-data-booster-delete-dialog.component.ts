import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ExecutionDataBooster } from './execution-data-booster.model';
import { ExecutionDataBoosterPopupService } from './execution-data-booster-popup.service';
import { ExecutionDataBoosterService } from './execution-data-booster.service';

@Component({
    selector: 'jhi-execution-data-booster-delete-dialog',
    templateUrl: './execution-data-booster-delete-dialog.component.html'
})
export class ExecutionDataBoosterDeleteDialogComponent {

    execution: ExecutionDataBooster;

    constructor(
        private executionService: ExecutionDataBoosterService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.executionService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'executionListModification',
                content: 'Deleted an execution'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-execution-data-booster-delete-popup',
    template: ''
})
export class ExecutionDataBoosterDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private executionPopupService: ExecutionDataBoosterPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.executionPopupService
                .open(ExecutionDataBoosterDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
