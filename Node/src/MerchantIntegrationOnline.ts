import Requester from './utils/Requester';
import { SDK_VERSION } from './configs/Version';
import { initializeWithConfig, OnlineClientConfig, OnlineClientRawConfig } from './configs/Config';
import { TCurrency, TPaymentMethod, TTxStatus } from './types';
import * as Utils from './utils/Utils';

export type ChargeInitParams = {
  partnerTxID: string;
  partnerGroupTxID: string;
  amount: number;
  currency: TCurrency;
  description: string;
  hidePaymentMethods?: TPaymentMethod[];
  metaInfo?: {
    brandName?: string;
    location?: Partial<{
      ipAddress: string;
      longitude: number;
      latitude: number;
      accuracy: number;
    }>;
    device?: Partial<{
      deviceID: string;
      deviceModel: string;
      deviceBrand: string;
      iosUDID: string;
      imei: string;
    }>;
    subMerchant?: Partial<{
      name: string;
      category: string;
    }>;
    partnerUserInfo?: Partial<{
      id: string;
      phone: string;
      email: string;
    }>;
    echo?: string;
  };
  items?: {
    supplierName?: string;
    itemName?: string;
    category?: string;
    quantity?: number;
    price?: number;
    itemCategory?: string;
    url?: string;
    imageURL?: string;
    supplierDetails?: {
      supplierID?: string;
      supplierName?: string;
      supplierURL?: string;
      legalID?: string;
      supplierPhone?: string;
      supplierEmail?: string;
      supplierAddress?: string;
      supplierRating?: number;
    };
    shippingDetails?: {
      firstName?: string;
      lastName?: string;
      address?: string;
      city?: string;
      postalCode?: string;
      phone?: string;
      email?: string;
      countryCode?: string;
    };
  }[];
};

export type ChargeInitResponse = {
  partnerTxID: string;
  request: string;
};

export type GetOAuth2TokenParams = {
  code: string;
  codeVerifier: string;
};

export type GetOAuth2TokenResponse = {
  access_token: string;
  token_type: string;
  expires_in: number;
  id_token: string;
};

export type ChargeCompleteParams = {
  partnerTxID: string;
  accessToken: string;
};

export type ChargeCompleteResponse = {
  partnerTxID: string;
};

export type GetChargeStatusParams = {
  partnerTxID: string;
  currency: TCurrency;
  accessToken: string;
};

export type GetChargeStatusResponse = {
  txID: string;
  paymentMethod: TPaymentMethod;
  /**
   * @deprecated
   */
  status: string;
  description: string;
  txStatus: TTxStatus;
  reason: string;
};

export type RefundParams = {
  partnerTxID: string;
  partnerGroupTxID: string;
  amount: number;
  currency: TCurrency;
  originTxID: string;
  description: string;
  accessToken: string;
};

export type RefundResponse = {
  txID: string;
  /**
   * @deprecated
   */
  status: string;
  paymentMethod: TPaymentMethod;
  description: string;
  txStatus: TTxStatus;
  reason: string;
};

export type GetRefundStatusParams = {
  partnerTxID: string;
  currency: TCurrency;
  accessToken: string;
};

export type GetRefundStatusResponse = {
  txID: string;
  /**
   * @deprecated
   */
  status: string;
  paymentMethod: TPaymentMethod;
  description: string;
  txStatus: TTxStatus;
  reason: string;
  echo?: string;
};

export type GetOtcStatusParams = {
  partnerTxID: string;
  currency: TCurrency;
};

export type GetOtcStatusResponse = {
  txID: string;
  oAuthCode: string;
  paymentMethod: TPaymentMethod;
  /**
   * @deprecated
   */
  status: string;

  txStatus: TTxStatus;
  reason: string;
};

export class MerchantIntegrationOnline {
  _requester: Requester;
  _config: OnlineClientConfig;

  /**
   * Constructor
   */
  constructor(rawConfig: OnlineClientRawConfig, requester: Requester | null = null) {
    this._config = initializeWithConfig(rawConfig);
    this._requester = requester ?? new Requester(this._config);
  }

  /**
   * Init a payment
   */
  chargeInit(payload: ChargeInitParams) {
    const data = {
      ...payload,
      merchantID: this._config.merchantId,
    };

    return this._requester.hmacRequest<ChargeInitResponse, typeof data>({
      data,
      method: 'POST',
      path: this._config.urlSet.ONA_CHARGE_INIT,
    });
  }

  /**
   * Generate Web URL for Authorization
   */
  async generateWebUrl(payload: ChargeInitParams & { state: string; codeVerifier: string }) {
    const { state, codeVerifier, ...chargeInitPayload } = payload;
    const {
      data: { request },
    } = await this.chargeInit(chargeInitPayload);

    const { clientId, redirectUrl, scope, country } = this._config;
    const { currency } = payload;
    const codeChallenge = Utils.base64URLEncode(Utils.hashSha256ToBase64(codeVerifier));

    const params = {
      acr_values: `consent_ctx:countryCode=${country},currency=${currency}`,
      client_id: clientId,
      code_challenge: codeChallenge,
      code_challenge_method: 'S256',
      nonce: Utils.generateRandomString(16),
      redirect_uri: redirectUrl,
      request,
      response_type: 'code',
      scope,
      state,
    };

    return [this._config.envVars.baseUrl, `/grabid/v1/oauth2/authorize?`, new URLSearchParams(params).toString()].join(
      '',
    );
  }

  /**
   * Get OAuth token
   */
  oauth2Token(payload: GetOAuth2TokenParams) {
    const { code, codeVerifier } = payload;
    const { clientId, clientSecret, redirectUrl } = this._config;

    const data = {
      grant_type: 'authorization_code',
      client_id: clientId,
      client_secret: clientSecret,
      code_verifier: codeVerifier,
      redirect_uri: redirectUrl,
      code,
    };

    return this._requester.request<GetOAuth2TokenResponse, typeof data>({
      data,
      headers: {
        'Content-Type': 'application/json',
      },
      method: 'POST',
      path: this._config.urlSet.OAUTH_TOKEN,
    });
  }

  /**
   * Complete payments
   */
  chargeComplete({ accessToken, ...data }: ChargeCompleteParams) {
    return this._requester.request<ChargeCompleteResponse, { partnerTxID: string }>({
      path: this._config.urlSet.ONA_CHARGE_COMPLETE,
      method: 'POST',
      headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json',
        Date: Utils.getGMTString(),
        'X-GID-AUX-POP': Utils.generatePOPSignature(accessToken, this._config.clientSecret),
        Authorization: `Bearer ${accessToken}`,
      },
      data,
    });
  }

  /**
   * Get Partner Charge Status
   */
  getChargeStatus(payload: GetChargeStatusParams) {
    const { accessToken, currency, partnerTxID } = payload;
    const path = Utils.replaceUrl(this._config.urlSet.ONA_CHARGE_STATUS, {
      partnerTxID,
    });
    return this._requester.request<GetChargeStatusResponse, never>({
      headers: {
        Accept: 'application/json',
        Authorization: `Bearer ${accessToken}`,
        'Content-Type': 'application/json',
        Date: Utils.getGMTString(),
        'X-GID-AUX-POP': Utils.generatePOPSignature(accessToken, this._config.clientSecret),
      },
      method: 'GET',
      query: {
        currency,
      },
      path,
    });
  }

  /**
   * Generate refunds
   */
  refund({ accessToken, ...payload }: RefundParams) {
    const data = {
      ...payload,
      merchantID: this._config.merchantId,
    };

    return this._requester.request<RefundResponse, typeof data>({
      data,
      headers: {
        Accept: 'application/json',
        Authorization: `Bearer ${accessToken}`,
        'Content-Type': 'application/json',
        Date: Utils.getGMTString(),
        'X-GID-AUX-POP': Utils.generatePOPSignature(accessToken, this._config.clientSecret),
      },
      method: 'POST',
      path: this._config.urlSet.ONA_REFUND,
    });
  }

  /**
   * Get refund status
   */
  getRefundStatus({ accessToken, partnerTxID, currency }: GetRefundStatusParams) {
    const path = Utils.replaceUrl(this._config.urlSet.ONA_REFUND_STATUS, {
      partnerTxID,
    });

    return this._requester.request<GetRefundStatusResponse, never>({
      headers: {
        Accept: 'application/json',
        Authorization: `Bearer ${accessToken}`,
        'Content-Type': 'application/json',
        Date: Utils.getGMTString(),
        'X-GID-AUX-POP': Utils.generatePOPSignature(accessToken, this._config.clientSecret),
      },
      method: 'GET',
      query: {
        currency,
      },
      path,
    });
  }

  /**
   * Get One Time Charge Status
   */
  getOtcStatus({ partnerTxID, currency }: GetOtcStatusParams) {
    const path = Utils.replaceUrl(this._config.urlSet.ONA_ONE_TIME_CHARGE_STATUS, {
      partnerTxID,
    });

    return this._requester.hmacRequest<GetOtcStatusResponse, never>({
      method: 'GET',
      query: {
        currency,
      },
      path,
    });
  }

  /**
   * Get SDK Version
   */
  version() {
    return SDK_VERSION;
  }
}
