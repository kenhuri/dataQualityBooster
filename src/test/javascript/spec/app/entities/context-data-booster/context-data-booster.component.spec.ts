/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DataQualityBoosterTestModule } from '../../../test.module';
import { ContextDataBoosterComponent } from '../../../../../../main/webapp/app/entities/context-data-booster/context-data-booster.component';
import { ContextDataBoosterService } from '../../../../../../main/webapp/app/entities/context-data-booster/context-data-booster.service';
import { ContextDataBooster } from '../../../../../../main/webapp/app/entities/context-data-booster/context-data-booster.model';

describe('Component Tests', () => {

    describe('ContextDataBooster Management Component', () => {
        let comp: ContextDataBoosterComponent;
        let fixture: ComponentFixture<ContextDataBoosterComponent>;
        let service: ContextDataBoosterService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DataQualityBoosterTestModule],
                declarations: [ContextDataBoosterComponent],
                providers: [
                    ContextDataBoosterService
                ]
            })
            .overrideTemplate(ContextDataBoosterComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ContextDataBoosterComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ContextDataBoosterService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new ContextDataBooster(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.contexts[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
