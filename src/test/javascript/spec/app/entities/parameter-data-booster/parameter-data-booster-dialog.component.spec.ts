/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { DataQualityBoosterTestModule } from '../../../test.module';
import { ParameterDataBoosterDialogComponent } from '../../../../../../main/webapp/app/entities/parameter-data-booster/parameter-data-booster-dialog.component';
import { ParameterDataBoosterService } from '../../../../../../main/webapp/app/entities/parameter-data-booster/parameter-data-booster.service';
import { ParameterDataBooster } from '../../../../../../main/webapp/app/entities/parameter-data-booster/parameter-data-booster.model';
import { ContextDataBoosterService } from '../../../../../../main/webapp/app/entities/context-data-booster';

describe('Component Tests', () => {

    describe('ParameterDataBooster Management Dialog Component', () => {
        let comp: ParameterDataBoosterDialogComponent;
        let fixture: ComponentFixture<ParameterDataBoosterDialogComponent>;
        let service: ParameterDataBoosterService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DataQualityBoosterTestModule],
                declarations: [ParameterDataBoosterDialogComponent],
                providers: [
                    ContextDataBoosterService,
                    ParameterDataBoosterService
                ]
            })
            .overrideTemplate(ParameterDataBoosterDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ParameterDataBoosterDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ParameterDataBoosterService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new ParameterDataBooster(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.parameter = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'parameterListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new ParameterDataBooster();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.parameter = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'parameterListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
