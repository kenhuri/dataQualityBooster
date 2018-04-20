import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { ParameterDataBooster } from './parameter-data-booster.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<ParameterDataBooster>;

@Injectable()
export class ParameterDataBoosterService {

    private resourceUrl =  SERVER_API_URL + 'api/parameters';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/parameters';

    constructor(private http: HttpClient) { }

    create(parameter: ParameterDataBooster): Observable<EntityResponseType> {
        const copy = this.convert(parameter);
        return this.http.post<ParameterDataBooster>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(parameter: ParameterDataBooster): Observable<EntityResponseType> {
        const copy = this.convert(parameter);
        return this.http.put<ParameterDataBooster>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ParameterDataBooster>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<ParameterDataBooster[]>> {
        const options = createRequestOption(req);
        return this.http.get<ParameterDataBooster[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<ParameterDataBooster[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<ParameterDataBooster[]>> {
        const options = createRequestOption(req);
        return this.http.get<ParameterDataBooster[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<ParameterDataBooster[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: ParameterDataBooster = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<ParameterDataBooster[]>): HttpResponse<ParameterDataBooster[]> {
        const jsonResponse: ParameterDataBooster[] = res.body;
        const body: ParameterDataBooster[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to ParameterDataBooster.
     */
    private convertItemFromServer(parameter: ParameterDataBooster): ParameterDataBooster {
        const copy: ParameterDataBooster = Object.assign({}, parameter);
        return copy;
    }

    /**
     * Convert a ParameterDataBooster to a JSON which can be sent to the server.
     */
    private convert(parameter: ParameterDataBooster): ParameterDataBooster {
        const copy: ParameterDataBooster = Object.assign({}, parameter);
        return copy;
    }
}
