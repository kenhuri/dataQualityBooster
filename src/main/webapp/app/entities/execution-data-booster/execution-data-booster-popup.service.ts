import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { DatePipe } from '@angular/common';
import { ExecutionDataBooster } from './execution-data-booster.model';
import { ExecutionDataBoosterService } from './execution-data-booster.service';

@Injectable()
export class ExecutionDataBoosterPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private executionService: ExecutionDataBoosterService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.executionService.find(id)
                    .subscribe((executionResponse: HttpResponse<ExecutionDataBooster>) => {
                        const execution: ExecutionDataBooster = executionResponse.body;
                        execution.startDate = this.datePipe
                            .transform(execution.startDate, 'yyyy-MM-ddTHH:mm:ss');
                        execution.endDate = this.datePipe
                            .transform(execution.endDate, 'yyyy-MM-ddTHH:mm:ss');
                        this.ngbModalRef = this.executionModalRef(component, execution);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.executionModalRef(component, new ExecutionDataBooster());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    executionModalRef(component: Component, execution: ExecutionDataBooster): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.execution = execution;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
