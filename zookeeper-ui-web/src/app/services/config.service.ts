import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Observable } from 'rxjs';

export interface Properties {
  zooKeeperServiceBaseUrl: string;
}

@Injectable()
export class ConfigService {
  properties: Properties;

  constructor() {

  }

  load(http: Http): Promise<Properties> {
    return http.get('assets/config.json').map(data => {
      this.properties = data.json();
      return this.properties;
    }).toPromise();
  }

  public getZooKeeperServiceBaseUrl(): string {
    return this.properties.zooKeeperServiceBaseUrl;
  }
}
