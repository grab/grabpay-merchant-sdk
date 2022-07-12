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

const urlSets: Partial<Record<TCountry, ApiUrlSet>> = {
  VN: {
    OAUTH_TOKEN: '/grabid/v1/oauth2/token',
    ONA_CHARGE_COMPLETE: '/mocapay/partner/v2/charge/complete',
    ONA_CHARGE_INIT: '/mocapay/partner/v2/charge/init',
    ONA_CHARGE_STATUS: '/moca/partner/v2/charge/{partnerTxID}/status',
    ONA_ONE_TIME_CHARGE_STATUS: '/mocapay/partner/v2/one-time-charge/{partnerTxID}/status',
    ONA_REFUND: '/mocapay/partner/v2/refund',
    ONA_REFUND_STATUS: '/mocapay/partner/v2/refund/{partnerTxID}/status',
    POS_CANCEL_TRANSACTION: '/mocapay/partners/v1/terminal/transaction/{origPartnerTxID}/cancel',
    POS_CREATE_QR_CODE: '/mocapay/partners/v1/terminal/qrcode/create',
    POS_GET_TXN_DETAIL: '/mocapay/partners/v1/terminal/transaction/{partnerTxID}',
    POS_PERFORM_TRANSACTION: '/mocapay/partners/v1/terminal/transaction/perform',
    POS_REFUND_TRANSACTION: '/mocapay/partners/v1/terminal/transaction/{origPartnerTxID}/refund',
  },
};

const fallbackUrlSet: ApiUrlSet = {
  OAUTH_TOKEN: '/grabid/v1/oauth2/token',
  ONA_CHARGE_COMPLETE: '/grabpay/partner/v2/charge/complete',
  ONA_CHARGE_INIT: '/grabpay/partner/v2/charge/init',
  ONA_CHARGE_STATUS: '/grabpay/partner/v2/charge/{partnerTxID}/status',
  ONA_ONE_TIME_CHARGE_STATUS: '/grabpay/partner/v2/one-time-charge/{partnerTxID}/status',
  ONA_REFUND: '/grabpay/partner/v2/refund',
  ONA_REFUND_STATUS: '/grabpay/partner/v2/refund/{partnerTxID}/status',
  POS_CANCEL_TRANSACTION: '/grabpay/partner/v1/terminal/transaction/{origPartnerTxID}/cancel',
  POS_CREATE_QR_CODE: '/grabpay/partner/v1/terminal/qrcode/create',
  POS_GET_TXN_DETAIL: '/grabpay/partner/v1/terminal/transaction/{partnerTxID}',
  POS_PERFORM_TRANSACTION: '/grabpay/partner/v1/terminal/transaction/perform',
  POS_REFUND_TRANSACTION: '/grabpay/partner/v1/terminal/transaction/{origPartnerTxID}/refund',
};

export function getUrls(country: TCountry) {
  return (country in urlSets ? urlSets[country] : undefined) || fallbackUrlSet;
}
