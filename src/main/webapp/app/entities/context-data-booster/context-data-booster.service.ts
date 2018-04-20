import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { ContextDataBooster } from './context-data-booster.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<ContextDataBooster>;

@Injectable()
export class ContextDataBoosterService {

    private resourceUrl =  SERVER_API_URL + 'api/contexts';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/contexts';

    constructor(private http: HttpClient) { }

    create(context: ContextDataBooster): Observable<EntityResponseType> {
        const copy = this.convert(context);
        return this.http.post<ContextDataBooster>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(context: ContextDataBooster): Observable<EntityResponseType> {
        const copy = this.convert(context);
        return this.http.put<ContextDataBooster>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ContextDataBooster>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<ContextDataBooster[]>> {
        const options = createRequestOption(req);
        return this.http.get<ContextDataBooster[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<ContextDataBooster[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<ContextDataBooster[]>> {
        const options = createRequestOption(req);
        return this.http.get<ContextDataBooster[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<ContextDataBooster[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: ContextDataBooster = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<ContextDataBooster[]>): HttpResponse<ContextDataBooster[]> {
        const jsonResponse: ContextDataBooster[] = res.body;
        const body: ContextDataBooster[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to ContextDataBooster.
     */
    private convertItemFromServer(context: ContextDataBooster): ContextDataBooster {
        const copy: ContextDataBooster = Object.assign({}, context);
        return copy;
    }

    /**
     * Convert a ContextDataBooster to a JSON which can be sent to the server.
     */
    private convert(context: ContextDataBooster): ContextDataBooster {
        const copy: ContextDataBooster = Object.assign({}, context);
        return copy;
    }
}
