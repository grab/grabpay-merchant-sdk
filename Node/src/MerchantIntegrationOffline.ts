import Requester, { IRequester } from './utils/Requester';
import { initializeWithConfig, OfflineClientConfig } from './configs/Config';
import { SDK_VERSION } from './configs/Version';
import { OfflineClientRawConfig } from './configs/Config';
import { TCurrency } from './types';

export type PosInitiateResponse = {
  transactionDetails?: {
    paymentChannel?: 'CPQR' | 'MPQR';
    storeGrabID?: string;
    grabTxID?: string;
    partnerTxID?: string;
    partnerGroupTxID?: string;
    billRefNumber?: string;
    amount?: number;
    currency?: TCurrency;
    paymentExpiryTime?: number;
  };
  POSDetails?: {
    terminalID?: string;
    qrPayload?: string;
  };
  statusDetails?: {
    status: string;
    statusCode: string;
    statusReason: string;
  };
};

export type PosInitiateRequest = {
  transactionDetails?: {
    paymentChannel: 'CPQR' | 'MPQR';
    partnerTxID: string;
    partnerGroupTxID?: string;
    billRefNumber?: string;
    amount: number;
    currency: TCurrency;
    paymentExpiryTime: number;
  };
  paymentMethod?: {
    paymentMethodExclusion?: ('POSTPAID' | 'INSTALMENT_4')[];
    minAmtPostpaid?: number;
    minAmt4Instalment?: number;
  };
  POSDetails?: {
    consumerIdentifier?: string;
  };
};

export type PosInquiryResponse = {
  transactionDetails?: {
    paymentChannel?: 'CPQR' | 'MPQR';
    storeGrabID?: string;
    txType?: 'PAYMENT' | 'REFUND';
    grabTxID?: string;
    partnerTxID?: string;
    partnerGroupTxID?: string;
    billRefNumber?: string;
    amount?: number;
    refundedAmount?: number;
    currency?: TCurrency;
    paymentMethod: 'GPWALLET' | 'POSTPAID' | 'INSTALMENT_4' | 'CARD' | 'PAYNOW' | 'DUITNOW' | 'QRPH';
    updatedTime: number;
    paymentExpiryTime?: number;
  };
  POSDetails?: {
    terminalID?: string;
  };
  statusDetails?: {
    status: string;
    statusCode: string;
    statusReason: string;
  };
  originTxDetails?: {
    originGrabTxID?: string;
    originPartnerTxID?: string;
    originPartnerGroupTxID?: string;
    originAmount?: number;
  };
  promoDetails?: {
    promoCode?: string;
    consumerPaidAmt?: number;
    merchantFundedPromoAmt?: number;
    pointsMultiplier?: string;
    pointsAwarded?: number;
    promoRefund?: {
      consumerRefundedAmt?: number;
      merchantFundedPromoRefundAmt?: number;
      pointsRefunded?: number;
    };
  };
  metadata?: {
    offusTxID?: string;
    originOffusTxID?: string;
  };
};

export type PosInquiryRequest = {
  transactionDetails?: {
    paymentChannel: 'CPQR' | 'MPQR';
    currency: TCurrency;
    txType: 'PAYMENT' | 'REFUND';
    txRefType: 'GRABTXID' | 'PARTNERTXID';
    txID: string;
  };
};

export type RefundPaymentResponse = {
  transactionDetails?: {
    paymentChannel?: 'CPQR' | 'MPQR';
    storeGrabID?: string;
    grabTxID?: string;
    partnerTxID?: string;
    partnerGroupTxID?: string;
    amount?: number;
    currency?: TCurrency;
    paymentMethod: 'GPWALLET' | 'POSTPAID' | 'INSTALMENT_4' | 'CARD' | 'PAYNOW' | 'DUITNOW' | 'QRPH';
    updatedTime: number;
  };
  originTxDetails?: {
    originGrabTxID?: string;
    originPartnerTxID?: string;
    originPartnerGroupTxID?: string;
    originAmount?: number;
  };
  promoRefundDetails?: {
    consumerRefundedAmt?: number;
    merchantFundedPromoRefundAmt?: number;
    pointsRefunded?: number;
  };
  statusDetails?: {
    status: string;
    statusCode: string;
    statusReason: string;
  };
  metadata?: {
    offusTxID?: string;
    originOffusTxID?: string;
  };
};

export type PosRefundRequest = {
  transactionDetails?: {
    paymentChannel: 'CPQR' | 'MPQR';
    originPartnerTxID: string;
    partnerTxID: string;
    partnerGroupTxID?: string;
    billRefNumber?: string;
    amount: number;
    currency: TCurrency;
    reason?: string;
  };
};

export type PosCancelResponse = {
  transactionDetails?: {
    paymentChannel?: 'CPQR' | 'MPQR';
    storeGrabID?: string;
    originPartnerTxID?: string;
    currency?: TCurrency;
  };
  statusDetails?: {
    status: string;
    statusCode: string;
    statusReason: string;
  };
};

export type PosCancelRequest = {
  transactionDetails?: {
    paymentChannel: 'CPQR' | 'MPQR';
    originPartnerTxID: string;
    currency: TCurrency;
  };
};

export class MerchantIntegrationOffline {
  _requester: Requester;
  _config: OfflineClientConfig;

  /**
   * Constructor
   */
  constructor(rawConfig: OfflineClientRawConfig, requester: Requester | null = null) {
    this._config = initializeWithConfig(rawConfig);
    this._requester = requester ?? new Requester(this._config);
  }

  /**
   * Initiate a payment
   * @param payload
   * @returns
   */
  initiate(payload: PosInitiateRequest) {
    const data = configurePayloadTerminalId(configurePayloadStoreGrabId(payload, this._config), this._config);
    return this._requester.hmacRequest<PosInitiateResponse, typeof data>({
      path: this._config.urlSet.POS_INITIATE,
      method: 'POST',
      data,
    });
  }

  inquiry(payload: PosInquiryRequest) {
    const data = configurePayloadStoreGrabId(payload, this._config);
    const query = flattenNestedMapForQuery(data);
    return this._requester.hmacRequest<PosInquiryResponse, typeof data>({
      path: this._config.urlSet.POS_INQUIRE,
      method: 'GET',
      query,
    });
  }

  /**
   * Cancel a payment
   */
  cancel(payload: PosCancelRequest) {
    const data = configurePayloadStoreGrabId(payload, this._config);
    return this._requester.hmacRequest<PosCancelResponse, typeof data>({
      path: this._config.urlSet.POS_CANCEL,
      method: 'PUT',
      data,
    });
  }

  /**
   * Refund a payment
   */
  refund(payload: PosRefundRequest) {
    const data = configurePayloadStoreGrabId(payload, this._config);

    return this._requester.hmacRequest<PosRefundRequest, typeof data>({
      path: this._config.urlSet.POS_REFUND,
      method: 'PUT',
      data,
    });
  }

  /**
   * Get SDK Version
   *
   * @returns {string}
   */
  version() {
    return SDK_VERSION;
  }
}

const configurePayloadStoreGrabId = <T extends { transactionDetails?: {} }>(
  payload: T,
  config: OfflineClientConfig,
) => ({
  ...payload,
  transactionDetails: {
    ...payload.transactionDetails,
    storeGrabID: config.merchantId,
  },
});

const configurePayloadTerminalId = <T extends { POSDetails?: {} }>(payload: T, config: OfflineClientConfig) => ({
  ...payload,
  POSDetails: {
    ...payload.POSDetails,
    terminalID: config.terminalId,
  },
});

const flattenNestedMapForQuery = <T>(obj: T) => {
  if (typeof obj !== 'object' || obj === null) {
    return obj;
  }
  return Object.entries(obj).reduce((acc, [property, value]) => {
    const processedValue = flattenNestedMapForQuery(value);
    if (typeof processedValue === 'object' && processedValue) {
      Object.entries(processedValue).forEach(([innerKey, innerValue]) => {
        acc[`${property}.${innerKey}`] = innerValue;
      });
    } else {
      acc[property] = processedValue;
    }
    return acc;
  }, {} as Record<string, any>);
};
