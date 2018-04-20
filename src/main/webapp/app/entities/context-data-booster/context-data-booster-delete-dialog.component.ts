import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ContextDataBooster } from './context-data-booster.model';
import { ContextDataBoosterPopupService } from './context-data-booster-popup.service';
import { ContextDataBoosterService } from './context-data-booster.service';

@Component({
    selector: 'jhi-context-data-booster-delete-dialog',
    templateUrl: './context-data-booster-delete-dialog.component.html'
})
export class ContextDataBoosterDeleteDialogComponent {

    context: ContextDataBooster;

    constructor(
        private contextService: ContextDataBoosterService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.contextService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'contextListModification',
                content: 'Deleted an context'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-context-data-booster-delete-popup',
    template: ''
})
export class ContextDataBoosterDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private contextPopupService: ContextDataBoosterPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.contextPopupService
                .open(ContextDataBoosterDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
