/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { DataQualityBoosterTestModule } from '../../../test.module';
import { PythonDataBoosterDialogComponent } from '../../../../../../main/webapp/app/entities/python-data-booster/python-data-booster-dialog.component';
import { PythonDataBoosterService } from '../../../../../../main/webapp/app/entities/python-data-booster/python-data-booster.service';
import { PythonDataBooster } from '../../../../../../main/webapp/app/entities/python-data-booster/python-data-booster.model';

describe('Component Tests', () => {

    describe('PythonDataBooster Management Dialog Component', () => {
        let comp: PythonDataBoosterDialogComponent;
        let fixture: ComponentFixture<PythonDataBoosterDialogComponent>;
        let service: PythonDataBoosterService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DataQualityBoosterTestModule],
                declarations: [PythonDataBoosterDialogComponent],
                providers: [
                    PythonDataBoosterService
                ]
            })
            .overrideTemplate(PythonDataBoosterDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PythonDataBoosterDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PythonDataBoosterService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new PythonDataBooster(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.python = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'pythonListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new PythonDataBooster();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.python = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'pythonListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
