/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DataQualityBoosterTestModule } from '../../../test.module';
import { PickleDataBoosterComponent } from '../../../../../../main/webapp/app/entities/pickle-data-booster/pickle-data-booster.component';
import { PickleDataBoosterService } from '../../../../../../main/webapp/app/entities/pickle-data-booster/pickle-data-booster.service';
import { PickleDataBooster } from '../../../../../../main/webapp/app/entities/pickle-data-booster/pickle-data-booster.model';

describe('Component Tests', () => {

    describe('PickleDataBooster Management Component', () => {
        let comp: PickleDataBoosterComponent;
        let fixture: ComponentFixture<PickleDataBoosterComponent>;
        let service: PickleDataBoosterService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DataQualityBoosterTestModule],
                declarations: [PickleDataBoosterComponent],
                providers: [
                    PickleDataBoosterService
                ]
            })
            .overrideTemplate(PickleDataBoosterComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PickleDataBoosterComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PickleDataBoosterService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new PickleDataBooster(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.pickles[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
