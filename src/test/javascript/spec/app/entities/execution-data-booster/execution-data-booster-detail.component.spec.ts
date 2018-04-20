/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DataQualityBoosterTestModule } from '../../../test.module';
import { ExecutionDataBoosterDetailComponent } from '../../../../../../main/webapp/app/entities/execution-data-booster/execution-data-booster-detail.component';
import { ExecutionDataBoosterService } from '../../../../../../main/webapp/app/entities/execution-data-booster/execution-data-booster.service';
import { ExecutionDataBooster } from '../../../../../../main/webapp/app/entities/execution-data-booster/execution-data-booster.model';

describe('Component Tests', () => {

    describe('ExecutionDataBooster Management Detail Component', () => {
        let comp: ExecutionDataBoosterDetailComponent;
        let fixture: ComponentFixture<ExecutionDataBoosterDetailComponent>;
        let service: ExecutionDataBoosterService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DataQualityBoosterTestModule],
                declarations: [ExecutionDataBoosterDetailComponent],
                providers: [
                    ExecutionDataBoosterService
                ]
            })
            .overrideTemplate(ExecutionDataBoosterDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ExecutionDataBoosterDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ExecutionDataBoosterService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new ExecutionDataBooster(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.execution).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
