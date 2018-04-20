import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { PickleDataBooster } from './pickle-data-booster.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<PickleDataBooster>;

@Injectable()
export class PickleDataBoosterService {

    private resourceUrl =  SERVER_API_URL + 'api/pickles';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/pickles';

    constructor(private http: HttpClient) { }

    create(pickle: PickleDataBooster): Observable<EntityResponseType> {
        const copy = this.convert(pickle);
        return this.http.post<PickleDataBooster>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(pickle: PickleDataBooster): Observable<EntityResponseType> {
        const copy = this.convert(pickle);
        return this.http.put<PickleDataBooster>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<PickleDataBooster>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<PickleDataBooster[]>> {
        const options = createRequestOption(req);
        return this.http.get<PickleDataBooster[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<PickleDataBooster[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<PickleDataBooster[]>> {
        const options = createRequestOption(req);
        return this.http.get<PickleDataBooster[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<PickleDataBooster[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: PickleDataBooster = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<PickleDataBooster[]>): HttpResponse<PickleDataBooster[]> {
        const jsonResponse: PickleDataBooster[] = res.body;
        const body: PickleDataBooster[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to PickleDataBooster.
     */
    private convertItemFromServer(pickle: PickleDataBooster): PickleDataBooster {
        const copy: PickleDataBooster = Object.assign({}, pickle);
        return copy;
    }

    /**
     * Convert a PickleDataBooster to a JSON which can be sent to the server.
     */
    private convert(pickle: PickleDataBooster): PickleDataBooster {
        const copy: PickleDataBooster = Object.assign({}, pickle);
        return copy;
    }
}
