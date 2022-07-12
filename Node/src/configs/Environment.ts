import { TCountry, TEnvironment } from '../types';

export type EnvironmentSet = {
  baseUrl: string;
};

const definedEnvs: Partial<Record<TCountry, Record<TEnvironment, EnvironmentSet>>> = {
  VN: {
    STG: {
      baseUrl: 'https://stg-paysi.moca.vn',
    },
    PRD: {
      baseUrl: 'https://partner-gw.moca.vn',
    },
  },
};

const fallbackEnvironment: Record<TEnvironment, EnvironmentSet> = {
  STG: {
    baseUrl: 'https://partner-api.stg-myteksi.com',
  },
  PRD: {
    baseUrl: 'https://partner-api.grab.com',
  },
};

export function getEnvs(country: TCountry, env: TEnvironment) {
  const countryEnv = country in definedEnvs ? definedEnvs[country] : undefined;
  return countryEnv ? countryEnv[env] : fallbackEnvironment[env];
}
