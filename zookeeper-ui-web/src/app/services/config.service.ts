import * as config from '../../assets/config.json';

export interface Properties {
  zooKeeperServiceBaseUrl: string;
}

export const properties: Properties = {
  zooKeeperServiceBaseUrl: (<any>config).zooKeeperServiceBaseUrl
};
