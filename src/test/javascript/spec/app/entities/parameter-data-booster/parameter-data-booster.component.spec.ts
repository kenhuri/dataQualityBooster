/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DataQualityBoosterTestModule } from '../../../test.module';
import { ParameterDataBoosterComponent } from '../../../../../../main/webapp/app/entities/parameter-data-booster/parameter-data-booster.component';
import { ParameterDataBoosterService } from '../../../../../../main/webapp/app/entities/parameter-data-booster/parameter-data-booster.service';
import { ParameterDataBooster } from '../../../../../../main/webapp/app/entities/parameter-data-booster/parameter-data-booster.model';

describe('Component Tests', () => {

    describe('ParameterDataBooster Management Component', () => {
        let comp: ParameterDataBoosterComponent;
        let fixture: ComponentFixture<ParameterDataBoosterComponent>;
        let service: ParameterDataBoosterService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DataQualityBoosterTestModule],
                declarations: [ParameterDataBoosterComponent],
                providers: [
                    ParameterDataBoosterService
                ]
            })
            .overrideTemplate(ParameterDataBoosterComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ParameterDataBoosterComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ParameterDataBoosterService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new ParameterDataBooster(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.parameters[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
