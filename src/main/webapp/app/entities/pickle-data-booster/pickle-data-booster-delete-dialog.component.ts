import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { PickleDataBooster } from './pickle-data-booster.model';
import { PickleDataBoosterPopupService } from './pickle-data-booster-popup.service';
import { PickleDataBoosterService } from './pickle-data-booster.service';

@Component({
    selector: 'jhi-pickle-data-booster-delete-dialog',
    templateUrl: './pickle-data-booster-delete-dialog.component.html'
})
export class PickleDataBoosterDeleteDialogComponent {

    pickle: PickleDataBooster;

    constructor(
        private pickleService: PickleDataBoosterService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.pickleService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'pickleListModification',
                content: 'Deleted an pickle'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-pickle-data-booster-delete-popup',
    template: ''
})
export class PickleDataBoosterDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private picklePopupService: PickleDataBoosterPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.picklePopupService
                .open(PickleDataBoosterDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
