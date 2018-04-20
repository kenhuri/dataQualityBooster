/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { DataQualityBoosterTestModule } from '../../../test.module';
import { ExecutionDataBoosterDialogComponent } from '../../../../../../main/webapp/app/entities/execution-data-booster/execution-data-booster-dialog.component';
import { ExecutionDataBoosterService } from '../../../../../../main/webapp/app/entities/execution-data-booster/execution-data-booster.service';
import { ExecutionDataBooster } from '../../../../../../main/webapp/app/entities/execution-data-booster/execution-data-booster.model';
import { ContextDataBoosterService } from '../../../../../../main/webapp/app/entities/context-data-booster';

describe('Component Tests', () => {

    describe('ExecutionDataBooster Management Dialog Component', () => {
        let comp: ExecutionDataBoosterDialogComponent;
        let fixture: ComponentFixture<ExecutionDataBoosterDialogComponent>;
        let service: ExecutionDataBoosterService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DataQualityBoosterTestModule],
                declarations: [ExecutionDataBoosterDialogComponent],
                providers: [
                    ContextDataBoosterService,
                    ExecutionDataBoosterService
                ]
            })
            .overrideTemplate(ExecutionDataBoosterDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ExecutionDataBoosterDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ExecutionDataBoosterService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new ExecutionDataBooster(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.execution = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'executionListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new ExecutionDataBooster();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.execution = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'executionListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
