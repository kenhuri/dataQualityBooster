/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { DataQualityBoosterTestModule } from '../../../test.module';
import { PickleDataBoosterDialogComponent } from '../../../../../../main/webapp/app/entities/pickle-data-booster/pickle-data-booster-dialog.component';
import { PickleDataBoosterService } from '../../../../../../main/webapp/app/entities/pickle-data-booster/pickle-data-booster.service';
import { PickleDataBooster } from '../../../../../../main/webapp/app/entities/pickle-data-booster/pickle-data-booster.model';
import { ContextDataBoosterService } from '../../../../../../main/webapp/app/entities/context-data-booster';

describe('Component Tests', () => {

    describe('PickleDataBooster Management Dialog Component', () => {
        let comp: PickleDataBoosterDialogComponent;
        let fixture: ComponentFixture<PickleDataBoosterDialogComponent>;
        let service: PickleDataBoosterService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DataQualityBoosterTestModule],
                declarations: [PickleDataBoosterDialogComponent],
                providers: [
                    ContextDataBoosterService,
                    PickleDataBoosterService
                ]
            })
            .overrideTemplate(PickleDataBoosterDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PickleDataBoosterDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PickleDataBoosterService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new PickleDataBooster(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.pickle = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'pickleListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new PickleDataBooster();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.pickle = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'pickleListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
