import Requester, { RequestParams } from '../../utils/Requester';
import { initializeWithConfig } from '../../configs/Config';
import { SDK_SIGNATURE, SDK_VERSION } from '../../configs/Version';
import * as Utils from '../../utils/Utils';
import sinon from 'sinon';
import { AxiosRequestConfig } from 'axios';
import { ClientConfig } from '../../configs/Config';

describe('Requests tests', () => {
  let testRequest: Requester;
  let config: ClientConfig;

  beforeAll(() => {
    config = initializeWithConfig({
      env: 'STG',
      country: 'VN',
      partnerId: 'partnerId',
      partnerSecret: 'partnerSecret',
      merchantId: 'merchantId',
      terminalId: '',
      clientId: 'clientId',
      clientSecret: 'clientSecret',
      redirectUrl: 'redirectUrl',
    });
    testRequest = new Requester(config);
  });

  it('Test request', async () => {
    const data = {
      partnerID: 'partner_id',
      partnerSecret: 'partner_secret',
    };

    const header: Record<string, string> = {
      Accept: 'application/json',
      'Content-Type': 'application/json',
      'X-Sdk-Version': SDK_VERSION,
      'X-Sdk-Country': config.country,
      'X-Sdk-Language': 'nodejs',
      'X-Sdk-Signature': SDK_SIGNATURE,
    };

    const inputConfig: RequestParams<typeof data> = {
      path: '/this/is/url',
      method: 'POST',
      headers: header,
      data,
    };

    const spy = jest
      .spyOn(testRequest._requester, 'request')
      .mockReturnValue(Promise.resolve({ status: 200, headers: {}, data: {} }));
    await testRequest.request(inputConfig);
    expect(spy).toHaveBeenCalledWith({
      url: '/this/is/url',
      method: 'POST',
      data: JSON.stringify(data),
      headers: header,
    });
  });

  describe('hmacRequest', () => {
    const data = {
      partnerID: 'partner_id',
      partnerSecret: 'partner_secret',
    };
    let inputConfig: RequestParams<typeof data>;

    beforeAll(() => {
      const date = 'Mon, 22 Dec 2021 11:55:00 GMT';
      const hmac = 'this_is_hmac';
      inputConfig = {
        path: '/this/is/url',
        method: 'POST',
        headers: {
          Accept: 'application/json',
          'Content-Type': 'application/json',
          Date: date,
          Authorization: `partnerId:${hmac}`,
          'X-Sdk-Version': SDK_VERSION,
          'X-Sdk-Country': config.country,
          'X-Sdk-Language': 'nodejs',
          'X-Sdk-Signature': SDK_SIGNATURE,
        },
        data,
      };
      sinon.stub(Utils, 'generateHmac').returns(hmac);
      sinon.stub(Utils, 'getGMTString').returns(date);
    });

    it('test POST', async () => {
      const spy = jest
        .spyOn(testRequest._requester, 'request')
        .mockReturnValue(Promise.resolve({ status: 200, headers: {}, data: {} }));

      // test post
      await testRequest.hmacRequest(inputConfig);
      expect(spy).toHaveBeenCalledWith({
        url: '/this/is/url',
        method: 'POST',
        data: JSON.stringify(data),
        headers: inputConfig.headers,
      });
    });

    it('test GET', async () => {
      const spy = jest
        .spyOn(testRequest._requester, 'request')
        .mockReturnValue(Promise.resolve({ status: 200, headers: {}, data: {} }));
      //test get
      inputConfig.method = 'GET';
      await testRequest.hmacRequest(inputConfig);
      expect(spy).toHaveBeenCalledWith({
        url: '/this/is/url',
        method: 'GET',
        headers: inputConfig.headers,
      });
    });
  });
});
