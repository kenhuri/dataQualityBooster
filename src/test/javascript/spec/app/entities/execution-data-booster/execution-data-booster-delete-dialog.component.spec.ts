/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { DataQualityBoosterTestModule } from '../../../test.module';
import { ExecutionDataBoosterDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/execution-data-booster/execution-data-booster-delete-dialog.component';
import { ExecutionDataBoosterService } from '../../../../../../main/webapp/app/entities/execution-data-booster/execution-data-booster.service';

describe('Component Tests', () => {

    describe('ExecutionDataBooster Management Delete Component', () => {
        let comp: ExecutionDataBoosterDeleteDialogComponent;
        let fixture: ComponentFixture<ExecutionDataBoosterDeleteDialogComponent>;
        let service: ExecutionDataBoosterService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DataQualityBoosterTestModule],
                declarations: [ExecutionDataBoosterDeleteDialogComponent],
                providers: [
                    ExecutionDataBoosterService
                ]
            })
            .overrideTemplate(ExecutionDataBoosterDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ExecutionDataBoosterDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ExecutionDataBoosterService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
