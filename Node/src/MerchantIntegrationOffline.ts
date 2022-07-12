import Requester, { IRequester } from './utils/Requester';
import { initializeWithConfig, OfflineClientConfig } from './configs/Config';
import { SDK_VERSION } from './configs/Version';
import { OfflineClientRawConfig } from './configs/Config';
import * as Utils from './utils/Utils';
import { TCurrency } from './types';

export type CreateQrCodeParams = {
  msgID?: string;
  partnerTxID: string;
  amount: number;
  currency: TCurrency;
};

export type CreateQrCodeResponse = {
  msgID: string;
  qrcode: string;
  txID: string;
};

export type PerformQrCodeTxnParams = {
  msgID?: string;
  partnerTxID: string;
  amount: number;
  currency: TCurrency;
  code: string;
  additionalInfo?: 'amountBreakdown'[];
};

export type PerformQrCodeTxnResponse = {
  msgID: string;
  txID: string;
  status: 'success' | 'failed' | 'unknown' | 'pending' | 'bad_debt';
  currency: TCurrency;
  amount: number;
  updated: number;
  errMsg: string;
  additionalInfo?: {
    amountBreakdown?: Record<string, number | string>;
  };
};

export type CancelTxnParams = {
  msgID?: string;
  partnerTxID: string;
  origPartnerTxID: string;
  origTxID: string;
  currency: TCurrency;
};

export type CancelTxnResponse = {};

export type RefundTxnParams = {
  msgID?: string;
  partnerTxID: string;
  amount: number;
  currency: TCurrency;
  origPartnerTxID: string;
  reason: string;
};

export type RefundTxnResponse = {
  msgID: string;
  txID: string;
  originTxID: string;
  status: 'success' | 'failed' | 'unknown' | 'pending' | 'bad_debt';
  description: string;
  additionalInfo?: {
    amountBreakdown?: Record<string, number | string>;
  };
};

export type GetTxnDetailsParams = {
  msgID?: string;
  partnerTxID: string;
  currency: string;
};

export type GetTxnDetailsResponse = {
  msgID: string;
  txID: string;
  status: 'success' | 'failed' | 'unknown' | 'pending' | 'bad_debt';
  currency: TCurrency;
  amount: number;
  updated: number;
  errMsg: string;
  additionalInfo?: {
    amountBreakdown?: Record<string, number | string>;
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
   * Create QR Code
   */
  createQrCode(payload: CreateQrCodeParams) {
    const data = configurePayload(payload, this._config);
    return this._requester.hmacRequest<CreateQrCodeResponse, typeof data>({
      path: this._config.urlSet.POS_CREATE_QR_CODE,
      method: 'POST',
      data,
    });
  }

  /**
   * Perform a Transaction
   */
  performQrCode(payload: PerformQrCodeTxnParams) {
    const data = configurePayload(payload, this._config);
    return this._requester.hmacRequest<PerformQrCodeTxnResponse, typeof data>({
      path: this._config.urlSet.POS_PERFORM_TRANSACTION,
      method: 'POST',
      data,
    });
  }

  /**
   * Cancel a Transaction
   */
  cancel({ origPartnerTxID, ...payload }: CancelTxnParams) {
    const data = configurePayload(payload, this._config);
    const path = Utils.replaceUrl(this._config.urlSet.POS_CANCEL_TRANSACTION, {
      origPartnerTxID,
    });

    return this._requester.hmacRequest<CancelTxnResponse, typeof data>({
      path,
      method: 'PUT',
      data,
    });
  }

  /**
   * Refund a POS Payment
   */
  refund({ origPartnerTxID, ...payload }: RefundTxnParams) {
    const data = configurePayload(payload, this._config);
    const path = Utils.replaceUrl(this._config.urlSet.POS_REFUND_TRANSACTION, {
      origPartnerTxID,
    });

    return this._requester.hmacRequest<RefundTxnResponse, typeof data>({
      path,
      method: 'PUT',
      data,
    });
  }

  /**
   * Check Transaction Details
   */
  getTxnDetails({ partnerTxID, ...payload }: GetTxnDetailsParams) {
    const configuredPayload = {
      ...configurePayload(payload, this._config),
      txType: 'P2M',
    };
    const path = Utils.replaceUrl(this._config.urlSet.POS_GET_TXN_DETAIL, {
      partnerTxID,
    });

    return this._requester.hmacRequest<GetTxnDetailsResponse, never>({
      path,
      method: 'GET',
      query: configuredPayload,
    });
  }

  /**
   * Check Refund Details
   */
  getRefundDetails({ partnerTxID, ...payload }: GetTxnDetailsParams) {
    const configuredPayload = {
      ...configurePayload(payload, this._config),
      txType: 'Refund',
    };
    const path = Utils.replaceUrl(this._config.urlSet.POS_GET_TXN_DETAIL, {
      partnerTxID,
    });

    return this._requester.hmacRequest<CancelTxnResponse, never>({
      path,
      method: 'GET',
      query: configuredPayload,
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

const configurePayload = <T extends unknown & { msgID?: string }>(
  { msgID, ...payload }: T,
  config: OfflineClientConfig,
) => ({
  ...payload,
  // we use the provided msgID if mex provided one, otherwise we generate one for the request
  msgID: msgID ? msgID : Utils.generateMsgId(),
  grabID: config.merchantId,
  terminalID: config.terminalId,
});
