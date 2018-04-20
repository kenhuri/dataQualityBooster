/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DataQualityBoosterTestModule } from '../../../test.module';
import { PickleDataBoosterDetailComponent } from '../../../../../../main/webapp/app/entities/pickle-data-booster/pickle-data-booster-detail.component';
import { PickleDataBoosterService } from '../../../../../../main/webapp/app/entities/pickle-data-booster/pickle-data-booster.service';
import { PickleDataBooster } from '../../../../../../main/webapp/app/entities/pickle-data-booster/pickle-data-booster.model';

describe('Component Tests', () => {

    describe('PickleDataBooster Management Detail Component', () => {
        let comp: PickleDataBoosterDetailComponent;
        let fixture: ComponentFixture<PickleDataBoosterDetailComponent>;
        let service: PickleDataBoosterService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DataQualityBoosterTestModule],
                declarations: [PickleDataBoosterDetailComponent],
                providers: [
                    PickleDataBoosterService
                ]
            })
            .overrideTemplate(PickleDataBoosterDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PickleDataBoosterDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PickleDataBoosterService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new PickleDataBooster(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.pickle).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
