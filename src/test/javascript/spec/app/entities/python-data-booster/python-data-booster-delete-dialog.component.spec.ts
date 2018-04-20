/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { DataQualityBoosterTestModule } from '../../../test.module';
import { PythonDataBoosterDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/python-data-booster/python-data-booster-delete-dialog.component';
import { PythonDataBoosterService } from '../../../../../../main/webapp/app/entities/python-data-booster/python-data-booster.service';

describe('Component Tests', () => {

    describe('PythonDataBooster Management Delete Component', () => {
        let comp: PythonDataBoosterDeleteDialogComponent;
        let fixture: ComponentFixture<PythonDataBoosterDeleteDialogComponent>;
        let service: PythonDataBoosterService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DataQualityBoosterTestModule],
                declarations: [PythonDataBoosterDeleteDialogComponent],
                providers: [
                    PythonDataBoosterService
                ]
            })
            .overrideTemplate(PythonDataBoosterDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PythonDataBoosterDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PythonDataBoosterService);
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
