import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { ParameterDataBooster } from './parameter-data-booster.model';
import { ParameterDataBoosterService } from './parameter-data-booster.service';

@Injectable()
export class ParameterDataBoosterPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private parameterService: ParameterDataBoosterService

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
                this.parameterService.find(id)
                    .subscribe((parameterResponse: HttpResponse<ParameterDataBooster>) => {
                        const parameter: ParameterDataBooster = parameterResponse.body;
                        this.ngbModalRef = this.parameterModalRef(component, parameter);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.parameterModalRef(component, new ParameterDataBooster());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    parameterModalRef(component: Component, parameter: ParameterDataBooster): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.parameter = parameter;
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
