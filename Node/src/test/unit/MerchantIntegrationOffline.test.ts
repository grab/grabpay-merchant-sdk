import { initializeWithConfig, OfflineClientRawConfig } from '../../configs/Config';
import Requester from '../../utils/Requester';
import * as Utils from '../../utils/Utils';
import { MerchantIntegrationOffline } from '../../MerchantIntegrationOffline';

const partnerTxID = 'ROe9fTsr8WYuyZHsuy738QXNtfyNyj9k'; // generated with `Utils.generateRandomString(32)`
const origPartnerTxID = 'fBTcFk0T1OekPrLtbQoiIB33TyzwrWZl'; // generated with `Utils.generateRandomString(32)`

describe('MerchantIntegrationOffline', () => {
  const config: OfflineClientRawConfig = {
    env: 'STG',
    country: 'VN',
    partnerId: 'partner-id',
    partnerSecret: 'partner-secret',
    merchantId: 'merchant-id',
    terminalId: 'terminal-id',
  };
  let client: MerchantIntegrationOffline;
  const requester = new Requester(initializeWithConfig(config));
  const axiosRequestSpy = jest
    .spyOn(requester._requester, 'request')
    .mockReturnValue(Promise.resolve({ status: 200, headers: {}, data: {} }));

  beforeEach(() => {
    client = new MerchantIntegrationOffline(config, requester);
    jest.useFakeTimers().setSystemTime(0);
  });

  afterEach(() => {
    jest.clearAllMocks();
    jest.useRealTimers();
  });

  describe('initiate payment', () => {
    it('should work in happy path', async () => {
      await client.initiate({
        transactionDetails: {
          paymentChannel: 'CPQR',
          partnerTxID,
          billRefNumber: '123',
          amount: 10000,
          currency: 'VND',
          paymentExpiryTime: 0,
        },
      });
      expect(axiosRequestSpy.mock.calls).toMatchSnapshot();
    });
  });

  describe('inquire payment', () => {
    it('should work in happy path', async () => {
      await client.inquiry({
        transactionDetails: {
          paymentChannel: 'CPQR',
          txType: 'PAYMENT',
          currency: 'VND',
          txRefType: 'GRABTXID',
          txID: '123',
        },
      });
      expect(axiosRequestSpy.mock.calls).toMatchSnapshot();
    });
  });

  describe('refund payment', () => {
    it('should work in happy path', async () => {
      await client.refund({
        transactionDetails: {
          paymentChannel: 'CPQR',
          originPartnerTxID: partnerTxID,
          partnerTxID,
          currency: 'VND',
          amount: 1000,
          reason: 'goods refund',
        },
      });
      expect(axiosRequestSpy.mock.calls).toMatchSnapshot();
    });
  });

  describe('cancel payment', () => {
    it('should work in happy path', async () => {
      await client.cancel({
        transactionDetails: {
          paymentChannel: 'CPQR',
          originPartnerTxID: partnerTxID,
          currency: 'VND',
        },
      });
      expect(axiosRequestSpy.mock.calls).toMatchSnapshot();
    });
  });
});
