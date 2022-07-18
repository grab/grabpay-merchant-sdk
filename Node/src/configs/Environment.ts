import { TCountry, TEnvironment } from '../types';

export type EnvironmentSet = {
  baseUrl: string;
};

const VN_STG_HOST = 'https://stg-paysi.moca.vn';
const VN_PRD_HOST = 'https://partner-gw.moca.vn';
const REGIONAL_STG_HOST = 'https://partner-api.stg-myteksi.com';
const REGIONAL_PRD_HOST = 'https://partner-api.grab.com';

const definedEnvs: Partial<Record<TCountry, Record<TEnvironment, EnvironmentSet>>> = {
  VN: {
    STG: {
      baseUrl: VN_STG_HOST,
    },
    PRD: {
      baseUrl: VN_PRD_HOST,
    },
  },
};

const fallbackEnvironment: Record<TEnvironment, EnvironmentSet> = {
  STG: {
    baseUrl: REGIONAL_STG_HOST,
  },
  PRD: {
    baseUrl: REGIONAL_PRD_HOST,
  },
};

export function getEnvs(country: TCountry, env: TEnvironment) {
  const countryEnv = country in definedEnvs ? definedEnvs[country] : undefined;
  return countryEnv ? countryEnv[env] : fallbackEnvironment[env];
}
