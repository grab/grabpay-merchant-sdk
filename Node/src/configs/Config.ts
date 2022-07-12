import { TCountry, TEnvironment } from '../types';
import { ApiUrlSet, getUrls } from './ApiUrls';
import { EnvironmentSet, getEnvs } from './Environment';

const definedCountryScopes: Partial<Record<TCountry, string>> = {
  VN: 'payment.vn.one_time_charge',
};
const fallbackScope = ['openid', 'payment.one_time_charge'].join(' ');

type CommonClientRawConfig = {
  env: TEnvironment;
  country: TCountry;
  partnerId: string;
  partnerSecret: string;
  merchantId: string;
};

export type OfflineClientRawConfig = CommonClientRawConfig & {
  terminalId: string;
};

export type OnlineClientRawConfig = CommonClientRawConfig & {
  clientId: string;
  clientSecret: string;
  redirectUrl: string;
};

export type ClientRawConfig = OnlineClientRawConfig | OfflineClientRawConfig;

export type InitializedConfig<T extends ClientRawConfig> = {
  envVars: EnvironmentSet;
  urlSet: ApiUrlSet;
  scope: string;
} & T;

export type OnlineClientConfig = InitializedConfig<OnlineClientRawConfig>;
export type OfflineClientConfig = InitializedConfig<OfflineClientRawConfig>;
export type ClientConfig = OnlineClientConfig | OfflineClientConfig;

export function initializeWithConfig<T extends ClientRawConfig>(input: T): InitializedConfig<T> {
  return {
    ...input,
    envVars: getEnvs(input.country, input.env),
    urlSet: getUrls(input.country),
    scope: (input.country in definedCountryScopes ? definedCountryScopes[input.country] : undefined) || fallbackScope,
  };
}
