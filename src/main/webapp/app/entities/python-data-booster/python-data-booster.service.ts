import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { PythonDataBooster } from './python-data-booster.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<PythonDataBooster>;

@Injectable()
export class PythonDataBoosterService {

    private resourceUrl =  SERVER_API_URL + 'api/pythons';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/pythons';

    constructor(private http: HttpClient) { }

    create(python: PythonDataBooster): Observable<EntityResponseType> {
        const copy = this.convert(python);
        return this.http.post<PythonDataBooster>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(python: PythonDataBooster): Observable<EntityResponseType> {
        const copy = this.convert(python);
        return this.http.put<PythonDataBooster>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<PythonDataBooster>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<PythonDataBooster[]>> {
        const options = createRequestOption(req);
        return this.http.get<PythonDataBooster[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<PythonDataBooster[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<PythonDataBooster[]>> {
        const options = createRequestOption(req);
        return this.http.get<PythonDataBooster[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<PythonDataBooster[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: PythonDataBooster = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<PythonDataBooster[]>): HttpResponse<PythonDataBooster[]> {
        const jsonResponse: PythonDataBooster[] = res.body;
        const body: PythonDataBooster[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to PythonDataBooster.
     */
    private convertItemFromServer(python: PythonDataBooster): PythonDataBooster {
        const copy: PythonDataBooster = Object.assign({}, python);
        return copy;
    }

    /**
     * Convert a PythonDataBooster to a JSON which can be sent to the server.
     */
    private convert(python: PythonDataBooster): PythonDataBooster {
        const copy: PythonDataBooster = Object.assign({}, python);
        return copy;
    }
}
