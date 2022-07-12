import { initializeWithConfig, OnlineClientRawConfig } from '../../configs/Config';
import { MerchantIntegrationOnline } from '../../MerchantIntegrationOnline';
import Requester from '../../utils/Requester';
import * as Utils from '../../utils/Utils';

const partnerTxID = 'rrSwr3vetp9pRONvRsL0cOK6SzFTKU3d'; // generated using `Utils.generateRandomString(32)`
const partnerGroupTxID = '18csg3DVcCQH9vsA0s2G3zz4CAWE13vi'; // generated using `Utils.generateRandomString(32)`

describe('MerchantIntegrationOnline', () => {
  const config: OnlineClientRawConfig = {
    env: 'STG',
    country: 'VN',
    partnerId: 'partner-id',
    partnerSecret: 'partner-secret',
    merchantId: 'merchant-id',
    clientId: 'client-id',
    clientSecret: 'client-secret',
    redirectUrl: 'http://localhost:8888/result',
  };
  const requester = new Requester(initializeWithConfig(config));
  const axiosRequestSpy = jest
    .spyOn(requester._requester, 'request')
    .mockReturnValue(Promise.resolve({ status: 200, headers: {}, data: {} }));
  let client: MerchantIntegrationOnline;

  beforeEach(() => {
    client = new MerchantIntegrationOnline(config, requester);
    jest.useFakeTimers().setSystemTime(0);
  });

  afterEach(() => {
    jest.clearAllMocks();
    jest.useRealTimers();
  });

  describe('chargeInit', () => {
    it('happy path', async () => {
      await client.chargeInit({
        partnerTxID,
        partnerGroupTxID,
        amount: 100000,
        currency: 'VND',
        description: 'testing otc',
        metaInfo: {
          brandName: 'Store1',
        },
      });
      expect(axiosRequestSpy).toHaveBeenCalledTimes(1);
      expect(axiosRequestSpy.mock.calls).toMatchSnapshot();
    });

    it('hiding postpaid method', async () => {
      await client.chargeInit({
        partnerTxID,
        partnerGroupTxID,
        amount: 100000,
        currency: 'VND',
        description: 'testing otc',
        hidePaymentMethods: ['POSTPAID'],
      });
      expect(axiosRequestSpy).toHaveBeenCalledTimes(1);
      expect(axiosRequestSpy.mock.calls).toMatchSnapshot();
    });
  });

  it('generateWebUrl', async () => {
    const generateRandomStringSpy = jest.spyOn(Utils, 'generateRandomString').mockReturnValue('randomNonce');
    const state = 'someSta'; // generated using `Utils.generateRandomString(7)`
    axiosRequestSpy.mockReturnValue(Promise.resolve({ status: 200, headers: {}, data: { request: 'token' } }));
    const generatedUrl = await client.generateWebUrl({
      partnerTxID,
      partnerGroupTxID,
      amount: 100000,
      currency: 'VND',
      description: 'testing otc',
      metaInfo: {
        brandName: 'Store1',
      },
      codeVerifier: 'abc123',
      state,
    });
    expect(generateRandomStringSpy).toHaveBeenCalledTimes(1);
    expect(axiosRequestSpy).toHaveBeenCalledTimes(1);
    expect(axiosRequestSpy.mock.calls).toMatchSnapshot();
    expect(generatedUrl).toMatchSnapshot();
  });

  it('oauth2Token', async () => {
    await client.oauth2Token({
      code: 'testing',
      codeVerifier: 'abc123',
    });
    expect(axiosRequestSpy).toHaveBeenCalledTimes(1);
    expect(axiosRequestSpy.mock.calls).toMatchSnapshot();
  });

  it('chargeComplete', async () => {
    const payload = { partnerTxID, accessToken: 'testing' };
    await client.chargeComplete(payload);
    expect(axiosRequestSpy).toHaveBeenCalledTimes(1);
    expect(axiosRequestSpy.mock.calls).toMatchSnapshot();
  });

  it('getChargeStatus', async () => {
    await client.getChargeStatus({ partnerTxID, currency: 'VND', accessToken: 'testing' });
    expect(axiosRequestSpy).toHaveBeenCalledTimes(1);
    expect(axiosRequestSpy.mock.calls).toMatchSnapshot();
  });

  it('refund', async () => {
    await client.refund({
      accessToken: 'token',
      partnerTxID,
      partnerGroupTxID,
      amount: 100000,
      currency: 'VND',
      description: 'test',
      originTxID: '34234',
    });
    expect(axiosRequestSpy).toHaveBeenCalledTimes(1);
    expect(axiosRequestSpy.mock.calls).toMatchSnapshot();
  });

  it('getOtcStatus', async () => {
    await client.getOtcStatus({
      currency: 'VND',
      partnerTxID,
    });
    expect(axiosRequestSpy).toHaveBeenCalledTimes(1);
    expect(axiosRequestSpy.mock.calls).toMatchSnapshot();
  });
});
