import { TCountry } from '../types';

export type ApiUrlSet = {
  OAUTH_TOKEN: string;
  ONA_CHARGE_COMPLETE: string;
  ONA_CHARGE_INIT: string;
  ONA_CHARGE_STATUS: string;
  ONA_ONE_TIME_CHARGE_STATUS: string;
  ONA_REFUND: string;
  ONA_REFUND_STATUS: string;

  POS_INITIATE: string;
  POS_INQUIRE: string;
  POS_REFUND: string;
  POS_CANCEL: string;
};

const API_PATH_GRAB_ID_V1 = '/grabid/v1';
const API_PATH_MOCA_PARTNER_V2 = '/mocapay/partner/v2';
const API_PATH_MOCA_PARTNERS_V3 = '/mocapay/partner/v3';
const API_PATH_REGIONAL_PARTNER_V3 = '/grabpay/partner/v3';
const API_PATH_REGIONAL_PARTNER_V2 = '/grabpay/partner/v2';

const urlSets: Partial<Record<TCountry, ApiUrlSet>> = {
  VN: {
    OAUTH_TOKEN: `${API_PATH_GRAB_ID_V1}/oauth2/token`,
    ONA_CHARGE_COMPLETE: `${API_PATH_MOCA_PARTNER_V2}/charge/complete`,
    ONA_CHARGE_INIT: `${API_PATH_MOCA_PARTNER_V2}/charge/init`,
    ONA_CHARGE_STATUS: `/moca/partner/v2/charge/{partnerTxID}/status`,
    ONA_ONE_TIME_CHARGE_STATUS: `${API_PATH_MOCA_PARTNER_V2}/one-time-charge/{partnerTxID}/status`,
    ONA_REFUND: `${API_PATH_MOCA_PARTNER_V2}/refund`,
    ONA_REFUND_STATUS: `${API_PATH_MOCA_PARTNER_V2}/refund/{partnerTxID}/status`,

    POS_INITIATE: `${API_PATH_MOCA_PARTNERS_V3}/payment/init`,
    POS_INQUIRE: `${API_PATH_MOCA_PARTNERS_V3}/payment/inquiry`,
    POS_REFUND: `${API_PATH_MOCA_PARTNERS_V3}/payment/refund`,
    POS_CANCEL: `${API_PATH_MOCA_PARTNERS_V3}/payment/cancellation`,
  },
};

const fallbackUrlSet: ApiUrlSet = {
  OAUTH_TOKEN: `${API_PATH_GRAB_ID_V1}/oauth2/token`,
  ONA_CHARGE_COMPLETE: `${API_PATH_REGIONAL_PARTNER_V2}/charge/complete`,
  ONA_CHARGE_INIT: `${API_PATH_REGIONAL_PARTNER_V2}/charge/init`,
  ONA_CHARGE_STATUS: `${API_PATH_REGIONAL_PARTNER_V2}/charge/{partnerTxID}/status`,
  ONA_ONE_TIME_CHARGE_STATUS: `${API_PATH_REGIONAL_PARTNER_V2}/one-time-charge/{partnerTxID}/status`,
  ONA_REFUND: `${API_PATH_REGIONAL_PARTNER_V2}/refund`,
  ONA_REFUND_STATUS: `${API_PATH_REGIONAL_PARTNER_V2}/refund/{partnerTxID}/status`,

  POS_INITIATE: `${API_PATH_REGIONAL_PARTNER_V3}/payment/init`,
  POS_INQUIRE: `${API_PATH_REGIONAL_PARTNER_V3}/payment/inquiry`,
  POS_REFUND: `${API_PATH_REGIONAL_PARTNER_V3}/payment/refund`,
  POS_CANCEL: `${API_PATH_REGIONAL_PARTNER_V3}/payment/cancellation`,
};

export function getUrls(country: TCountry) {
  return (country in urlSets ? urlSets[country] : undefined) || fallbackUrlSet;
}
