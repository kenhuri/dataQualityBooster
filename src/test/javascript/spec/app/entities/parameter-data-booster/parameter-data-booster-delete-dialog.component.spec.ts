/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { DataQualityBoosterTestModule } from '../../../test.module';
import { ParameterDataBoosterDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/parameter-data-booster/parameter-data-booster-delete-dialog.component';
import { ParameterDataBoosterService } from '../../../../../../main/webapp/app/entities/parameter-data-booster/parameter-data-booster.service';

describe('Component Tests', () => {

    describe('ParameterDataBooster Management Delete Component', () => {
        let comp: ParameterDataBoosterDeleteDialogComponent;
        let fixture: ComponentFixture<ParameterDataBoosterDeleteDialogComponent>;
        let service: ParameterDataBoosterService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DataQualityBoosterTestModule],
                declarations: [ParameterDataBoosterDeleteDialogComponent],
                providers: [
                    ParameterDataBoosterService
                ]
            })
            .overrideTemplate(ParameterDataBoosterDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ParameterDataBoosterDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ParameterDataBoosterService);
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
