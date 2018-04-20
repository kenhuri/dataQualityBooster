/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DataQualityBoosterTestModule } from '../../../test.module';
import { PythonDataBoosterDetailComponent } from '../../../../../../main/webapp/app/entities/python-data-booster/python-data-booster-detail.component';
import { PythonDataBoosterService } from '../../../../../../main/webapp/app/entities/python-data-booster/python-data-booster.service';
import { PythonDataBooster } from '../../../../../../main/webapp/app/entities/python-data-booster/python-data-booster.model';

describe('Component Tests', () => {

    describe('PythonDataBooster Management Detail Component', () => {
        let comp: PythonDataBoosterDetailComponent;
        let fixture: ComponentFixture<PythonDataBoosterDetailComponent>;
        let service: PythonDataBoosterService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DataQualityBoosterTestModule],
                declarations: [PythonDataBoosterDetailComponent],
                providers: [
                    PythonDataBoosterService
                ]
            })
            .overrideTemplate(PythonDataBoosterDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PythonDataBoosterDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PythonDataBoosterService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new PythonDataBooster(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.python).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
