/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { DataQualityBoosterTestModule } from '../../../test.module';
import { PickleDataBoosterDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/pickle-data-booster/pickle-data-booster-delete-dialog.component';
import { PickleDataBoosterService } from '../../../../../../main/webapp/app/entities/pickle-data-booster/pickle-data-booster.service';

describe('Component Tests', () => {

    describe('PickleDataBooster Management Delete Component', () => {
        let comp: PickleDataBoosterDeleteDialogComponent;
        let fixture: ComponentFixture<PickleDataBoosterDeleteDialogComponent>;
        let service: PickleDataBoosterService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DataQualityBoosterTestModule],
                declarations: [PickleDataBoosterDeleteDialogComponent],
                providers: [
                    PickleDataBoosterService
                ]
            })
            .overrideTemplate(PickleDataBoosterDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PickleDataBoosterDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PickleDataBoosterService);
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
