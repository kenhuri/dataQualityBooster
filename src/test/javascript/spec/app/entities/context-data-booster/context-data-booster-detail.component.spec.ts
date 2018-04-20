/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DataQualityBoosterTestModule } from '../../../test.module';
import { ContextDataBoosterDetailComponent } from '../../../../../../main/webapp/app/entities/context-data-booster/context-data-booster-detail.component';
import { ContextDataBoosterService } from '../../../../../../main/webapp/app/entities/context-data-booster/context-data-booster.service';
import { ContextDataBooster } from '../../../../../../main/webapp/app/entities/context-data-booster/context-data-booster.model';

describe('Component Tests', () => {

    describe('ContextDataBooster Management Detail Component', () => {
        let comp: ContextDataBoosterDetailComponent;
        let fixture: ComponentFixture<ContextDataBoosterDetailComponent>;
        let service: ContextDataBoosterService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DataQualityBoosterTestModule],
                declarations: [ContextDataBoosterDetailComponent],
                providers: [
                    ContextDataBoosterService
                ]
            })
            .overrideTemplate(ContextDataBoosterDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ContextDataBoosterDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ContextDataBoosterService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new ContextDataBooster(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.context).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
