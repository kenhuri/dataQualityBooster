import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { PythonDataBooster } from './python-data-booster.model';
import { PythonDataBoosterPopupService } from './python-data-booster-popup.service';
import { PythonDataBoosterService } from './python-data-booster.service';

@Component({
    selector: 'jhi-python-data-booster-delete-dialog',
    templateUrl: './python-data-booster-delete-dialog.component.html'
})
export class PythonDataBoosterDeleteDialogComponent {

    python: PythonDataBooster;

    constructor(
        private pythonService: PythonDataBoosterService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.pythonService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'pythonListModification',
                content: 'Deleted an python'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-python-data-booster-delete-popup',
    template: ''
})
export class PythonDataBoosterDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private pythonPopupService: PythonDataBoosterPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.pythonPopupService
                .open(PythonDataBoosterDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
