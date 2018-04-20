/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DataQualityBoosterTestModule } from '../../../test.module';
import { PythonDataBoosterComponent } from '../../../../../../main/webapp/app/entities/python-data-booster/python-data-booster.component';
import { PythonDataBoosterService } from '../../../../../../main/webapp/app/entities/python-data-booster/python-data-booster.service';
import { PythonDataBooster } from '../../../../../../main/webapp/app/entities/python-data-booster/python-data-booster.model';

describe('Component Tests', () => {

    describe('PythonDataBooster Management Component', () => {
        let comp: PythonDataBoosterComponent;
        let fixture: ComponentFixture<PythonDataBoosterComponent>;
        let service: PythonDataBoosterService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DataQualityBoosterTestModule],
                declarations: [PythonDataBoosterComponent],
                providers: [
                    PythonDataBoosterService
                ]
            })
            .overrideTemplate(PythonDataBoosterComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PythonDataBoosterComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PythonDataBoosterService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new PythonDataBooster(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.pythons[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
