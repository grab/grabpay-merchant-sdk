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

  describe('createQrCode', () => {
    it('use generated msgID', async () => {
      const generateMsgIdSpy = jest.spyOn(Utils, 'generateMsgId').mockReturnValue('generated-msgID');
      await client.createQrCode({ partnerTxID, amount: 100000, currency: 'VND' });
      expect(generateMsgIdSpy).toHaveBeenCalled();
      expect(axiosRequestSpy.mock.calls).toMatchSnapshot();
    });
    it('use provided msgID', async () => {
      const generateMsgIdSpy = jest.spyOn(Utils, 'generateMsgId').mockReturnValue('generated-msgID');
      await client.createQrCode({ msgID: 'provided-msgID', partnerTxID, amount: 100000, currency: 'VND' });
      expect(generateMsgIdSpy).not.toHaveBeenCalled();
      expect(axiosRequestSpy.mock.calls).toMatchSnapshot();
    });
  });

  describe('performQrCode', () => {
    it('use generated msgID', async () => {
      const generateMsgIdSpy = jest.spyOn(Utils, 'generateMsgId').mockReturnValue('generated-msgID');
      await client.performQrCode({ partnerTxID, amount: 100000, currency: 'VND', code: 'code' });
      expect(generateMsgIdSpy).toHaveBeenCalled();
      expect(axiosRequestSpy.mock.calls).toMatchSnapshot();
    });
    it('use provided msgID', async () => {
      const generateMsgIdSpy = jest.spyOn(Utils, 'generateMsgId').mockReturnValue('generated-msgID');
      await client.performQrCode({
        msgID: 'provided-msgID',
        partnerTxID,
        amount: 100000,
        currency: 'VND',
        code: 'code',
      });
      expect(generateMsgIdSpy).not.toHaveBeenCalled();
      expect(axiosRequestSpy.mock.calls).toMatchSnapshot();
    });
  });

  describe('cancel', () => {
    it('use generated msgID', async () => {
      const generateMsgIdSpy = jest.spyOn(Utils, 'generateMsgId').mockReturnValue('generated-msgID');
      await client.cancel({ partnerTxID, origPartnerTxID, origTxID: 'txID', currency: 'VND' });
      expect(generateMsgIdSpy).toHaveBeenCalled();
      expect(axiosRequestSpy.mock.calls).toMatchSnapshot();
    });
    it('use provided msgID', async () => {
      const generateMsgIdSpy = jest.spyOn(Utils, 'generateMsgId').mockReturnValue('generated-msgID');
      await client.cancel({
        msgID: 'provided-msgID',
        partnerTxID,
        origPartnerTxID,
        origTxID: 'txID',
        currency: 'VND',
      });
      expect(generateMsgIdSpy).not.toHaveBeenCalled();
      expect(axiosRequestSpy.mock.calls).toMatchSnapshot();
    });
  });

  describe('refund', () => {
    it('use generated msgID', async () => {
      const generateMsgIdSpy = jest.spyOn(Utils, 'generateMsgId').mockReturnValue('generated-msgID');
      await client.refund({ partnerTxID, origPartnerTxID, amount: 10000, currency: 'VND', reason: 'refund' });
      expect(generateMsgIdSpy).toHaveBeenCalled();
      expect(axiosRequestSpy.mock.calls).toMatchSnapshot();
    });
    it('use provided msgID', async () => {
      const generateMsgIdSpy = jest.spyOn(Utils, 'generateMsgId').mockReturnValue('generated-msgID');
      await client.refund({
        msgID: 'provided-msgID',
        amount: 10000,
        partnerTxID,
        origPartnerTxID,
        currency: 'VND',
        reason: 'refund',
      });
      expect(generateMsgIdSpy).not.toHaveBeenCalled();
      expect(axiosRequestSpy.mock.calls).toMatchSnapshot();
    });
  });

  describe('getTxnDetails', () => {
    it('use generated msgID', async () => {
      const generateMsgIdSpy = jest.spyOn(Utils, 'generateMsgId').mockReturnValue('generated-msgID');
      await client.getTxnDetails({ partnerTxID, currency: 'VND' });
      expect(generateMsgIdSpy).toHaveBeenCalled();
      expect(axiosRequestSpy.mock.calls).toMatchSnapshot();
    });
    it('use provided msgID', async () => {
      const generateMsgIdSpy = jest.spyOn(Utils, 'generateMsgId').mockReturnValue('generated-msgID');
      await client.getTxnDetails({
        msgID: 'provided-msgID',
        partnerTxID,
        currency: 'VND',
      });
      expect(generateMsgIdSpy).not.toHaveBeenCalled();
      expect(axiosRequestSpy.mock.calls).toMatchSnapshot();
    });
  });

  describe('getRefundDetails', () => {
    it('use generated msgID', async () => {
      const generateMsgIdSpy = jest.spyOn(Utils, 'generateMsgId').mockReturnValue('generated-msgID');
      await client.getRefundDetails({ partnerTxID, currency: 'VND' });
      expect(generateMsgIdSpy).toHaveBeenCalled();
      expect(axiosRequestSpy.mock.calls).toMatchSnapshot();
    });
    it('use provided msgID', async () => {
      const generateMsgIdSpy = jest.spyOn(Utils, 'generateMsgId').mockReturnValue('generated-msgID');
      await client.getRefundDetails({
        msgID: 'provided-msgID',
        partnerTxID,
        currency: 'VND',
      });
      expect(generateMsgIdSpy).not.toHaveBeenCalled();
      expect(axiosRequestSpy.mock.calls).toMatchSnapshot();
    });
  });
});
