import { ConnectionBackend, Headers, Http, Request, RequestOptions, RequestOptionsArgs, Response } from "@angular/http";
import { Observable } from "rxjs";
import 'rxjs/add/operator/catch';
import { Injectable, Injector } from "@angular/core";
import { Router } from '@angular/router';

@Injectable()
export class HttpInterceptor extends Http {
    constructor(backend: ConnectionBackend,
                defaultOptions: RequestOptions,
                private injector: Injector) {
        super(backend, defaultOptions);
    }

    request(url: string | Request, options?: RequestOptionsArgs): Observable<Response> {
        // Redirect to login when a 401 response is received
        return this.intercept(super.request(url, options));
    }

    private get router(): Router {
       return this.injector.get(Router);
    }

    intercept(observable: Observable<Response>): Observable<Response> {
        return observable.catch((error, source) => {
            if (error.status === 401) {
                this.router.navigate(['/login']);

                return Observable.throw(error);
            } else if (error.status === 404) {
                this.router.navigate(['/not_found']);

                return Observable.throw(error);
            } else {
                return Observable.throw(error);
            }
        });
    }
}
