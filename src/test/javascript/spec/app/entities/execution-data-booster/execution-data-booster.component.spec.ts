/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DataQualityBoosterTestModule } from '../../../test.module';
import { ExecutionDataBoosterComponent } from '../../../../../../main/webapp/app/entities/execution-data-booster/execution-data-booster.component';
import { ExecutionDataBoosterService } from '../../../../../../main/webapp/app/entities/execution-data-booster/execution-data-booster.service';
import { ExecutionDataBooster } from '../../../../../../main/webapp/app/entities/execution-data-booster/execution-data-booster.model';

describe('Component Tests', () => {

    describe('ExecutionDataBooster Management Component', () => {
        let comp: ExecutionDataBoosterComponent;
        let fixture: ComponentFixture<ExecutionDataBoosterComponent>;
        let service: ExecutionDataBoosterService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DataQualityBoosterTestModule],
                declarations: [ExecutionDataBoosterComponent],
                providers: [
                    ExecutionDataBoosterService
                ]
            })
            .overrideTemplate(ExecutionDataBoosterComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ExecutionDataBoosterComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ExecutionDataBoosterService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new ExecutionDataBooster(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.executions[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
