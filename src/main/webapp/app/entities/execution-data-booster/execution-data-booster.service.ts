import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { ExecutionDataBooster } from './execution-data-booster.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<ExecutionDataBooster>;

@Injectable()
export class ExecutionDataBoosterService {

    private resourceUrl =  SERVER_API_URL + 'api/executions';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/executions';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(execution: ExecutionDataBooster): Observable<EntityResponseType> {
        const copy = this.convert(execution);
        return this.http.post<ExecutionDataBooster>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(execution: ExecutionDataBooster): Observable<EntityResponseType> {
        const copy = this.convert(execution);
        return this.http.put<ExecutionDataBooster>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ExecutionDataBooster>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<ExecutionDataBooster[]>> {
        const options = createRequestOption(req);
        return this.http.get<ExecutionDataBooster[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<ExecutionDataBooster[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<ExecutionDataBooster[]>> {
        const options = createRequestOption(req);
        return this.http.get<ExecutionDataBooster[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<ExecutionDataBooster[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: ExecutionDataBooster = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<ExecutionDataBooster[]>): HttpResponse<ExecutionDataBooster[]> {
        const jsonResponse: ExecutionDataBooster[] = res.body;
        const body: ExecutionDataBooster[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to ExecutionDataBooster.
     */
    private convertItemFromServer(execution: ExecutionDataBooster): ExecutionDataBooster {
        const copy: ExecutionDataBooster = Object.assign({}, execution);
        copy.startDate = this.dateUtils
            .convertDateTimeFromServer(execution.startDate);
        copy.endDate = this.dateUtils
            .convertDateTimeFromServer(execution.endDate);
        return copy;
    }

    /**
     * Convert a ExecutionDataBooster to a JSON which can be sent to the server.
     */
    private convert(execution: ExecutionDataBooster): ExecutionDataBooster {
        const copy: ExecutionDataBooster = Object.assign({}, execution);

        copy.startDate = this.dateUtils.toDate(execution.startDate);

        copy.endDate = this.dateUtils.toDate(execution.endDate);
        return copy;
    }
}
