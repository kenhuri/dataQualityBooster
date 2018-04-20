/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DataQualityBoosterTestModule } from '../../../test.module';
import { ParameterDataBoosterDetailComponent } from '../../../../../../main/webapp/app/entities/parameter-data-booster/parameter-data-booster-detail.component';
import { ParameterDataBoosterService } from '../../../../../../main/webapp/app/entities/parameter-data-booster/parameter-data-booster.service';
import { ParameterDataBooster } from '../../../../../../main/webapp/app/entities/parameter-data-booster/parameter-data-booster.model';

describe('Component Tests', () => {

    describe('ParameterDataBooster Management Detail Component', () => {
        let comp: ParameterDataBoosterDetailComponent;
        let fixture: ComponentFixture<ParameterDataBoosterDetailComponent>;
        let service: ParameterDataBoosterService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DataQualityBoosterTestModule],
                declarations: [ParameterDataBoosterDetailComponent],
                providers: [
                    ParameterDataBoosterService
                ]
            })
            .overrideTemplate(ParameterDataBoosterDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ParameterDataBoosterDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ParameterDataBoosterService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new ParameterDataBooster(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.parameter).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
