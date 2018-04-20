/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { DataQualityBoosterTestModule } from '../../../test.module';
import { ContextDataBoosterDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/context-data-booster/context-data-booster-delete-dialog.component';
import { ContextDataBoosterService } from '../../../../../../main/webapp/app/entities/context-data-booster/context-data-booster.service';

describe('Component Tests', () => {

    describe('ContextDataBooster Management Delete Component', () => {
        let comp: ContextDataBoosterDeleteDialogComponent;
        let fixture: ComponentFixture<ContextDataBoosterDeleteDialogComponent>;
        let service: ContextDataBoosterService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DataQualityBoosterTestModule],
                declarations: [ContextDataBoosterDeleteDialogComponent],
                providers: [
                    ContextDataBoosterService
                ]
            })
            .overrideTemplate(ContextDataBoosterDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ContextDataBoosterDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ContextDataBoosterService);
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
