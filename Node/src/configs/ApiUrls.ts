import { TCountry } from '../types';

export type ApiUrlSet = {
  OAUTH_TOKEN: string;
  ONA_CHARGE_COMPLETE: string;
  ONA_CHARGE_INIT: string;
  ONA_CHARGE_STATUS: string;
  ONA_ONE_TIME_CHARGE_STATUS: string;
  ONA_REFUND: string;
  ONA_REFUND_STATUS: string;
  POS_CANCEL_TRANSACTION: string;
  POS_CREATE_QR_CODE: string;
  POS_GET_TXN_DETAIL: string;
  POS_PERFORM_TRANSACTION: string;
  POS_REFUND_TRANSACTION: string;
};

const API_PATH_GRAB_ID_V1 = '/grabid/v1';
const API_PATH_MOCA_PARTNER_V2 = '/mocapay/partner/v2';
const API_PATH_MOCA_PARTNERS_V1 = '/mocapay/partners/v1';
const API_PATH_REGIONAL_PARTNER_V1 = '/grabpay/partner/v1';
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

    POS_CANCEL_TRANSACTION: `${API_PATH_MOCA_PARTNERS_V1}/terminal/transaction/{origPartnerTxID}/cancel`,
    POS_CREATE_QR_CODE: `${API_PATH_MOCA_PARTNERS_V1}/terminal/qrcode/create`,
    POS_GET_TXN_DETAIL: `${API_PATH_MOCA_PARTNERS_V1}/terminal/transaction/{partnerTxID}`,
    POS_PERFORM_TRANSACTION: `${API_PATH_MOCA_PARTNERS_V1}/terminal/transaction/perform`,
    POS_REFUND_TRANSACTION: `${API_PATH_MOCA_PARTNERS_V1}/terminal/transaction/{origPartnerTxID}/refund`,
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

  POS_CANCEL_TRANSACTION: `${API_PATH_REGIONAL_PARTNER_V1}/terminal/transaction/{origPartnerTxID}/cancel`,
  POS_CREATE_QR_CODE: `${API_PATH_REGIONAL_PARTNER_V1}/terminal/qrcode/create`,
  POS_GET_TXN_DETAIL: `${API_PATH_REGIONAL_PARTNER_V1}/terminal/transaction/{partnerTxID}`,
  POS_PERFORM_TRANSACTION: `${API_PATH_REGIONAL_PARTNER_V1}/terminal/transaction/perform`,
  POS_REFUND_TRANSACTION: `${API_PATH_REGIONAL_PARTNER_V1}/terminal/transaction/{origPartnerTxID}/refund`,
};

export function getUrls(country: TCountry) {
  return (country in urlSets ? urlSets[country] : undefined) || fallbackUrlSet;
}
