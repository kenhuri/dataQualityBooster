/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { DataQualityBoosterTestModule } from '../../../test.module';
import { ContextDataBoosterDialogComponent } from '../../../../../../main/webapp/app/entities/context-data-booster/context-data-booster-dialog.component';
import { ContextDataBoosterService } from '../../../../../../main/webapp/app/entities/context-data-booster/context-data-booster.service';
import { ContextDataBooster } from '../../../../../../main/webapp/app/entities/context-data-booster/context-data-booster.model';
import { PythonDataBoosterService } from '../../../../../../main/webapp/app/entities/python-data-booster';

describe('Component Tests', () => {

    describe('ContextDataBooster Management Dialog Component', () => {
        let comp: ContextDataBoosterDialogComponent;
        let fixture: ComponentFixture<ContextDataBoosterDialogComponent>;
        let service: ContextDataBoosterService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DataQualityBoosterTestModule],
                declarations: [ContextDataBoosterDialogComponent],
                providers: [
                    PythonDataBoosterService,
                    ContextDataBoosterService
                ]
            })
            .overrideTemplate(ContextDataBoosterDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ContextDataBoosterDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ContextDataBoosterService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new ContextDataBooster(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.context = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'contextListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new ContextDataBooster();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.context = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'contextListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
