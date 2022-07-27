import { MerchantIntegrationOffline } from '../../../MerchantIntegrationOffline';
import * as Utils from '../../../utils/Utils';

const env = 'STG';
const country = 'SG';
const partnerId = process.env.SG_STG_POS_PARTNER_ID!;
const partnerSecret = process.env.SG_STG_POS_PARTNER_SECRET!;
const merchantId = process.env.SG_STG_POS_MERCHANT_ID!;
const terminalId = process.env.SG_STG_POS_TERMINAL_ID!;

describe('Test Regional MerchantIntegrationOffline', () => {
  let client: MerchantIntegrationOffline;
  let partnerTxID: string;
  let partnerGroupTxID: string;
  let msgID: string;
  let origPartnerTxID: string;

  beforeAll(() => {
    client = new MerchantIntegrationOffline({ env, country, partnerId, partnerSecret, merchantId, terminalId });
  });

  beforeEach(() => {
    partnerTxID = Utils.generateRandomString(32);
    partnerGroupTxID = Utils.generateRandomString(32);
    msgID = Utils.generateMsgId();
    origPartnerTxID = Utils.generateRandomString(32);
  });

  it('initiate payment', async () => {
    const amount = 2000;
    const currency = 'SGD';

    const response = await client.initiate({
      transactionDetails: {
        paymentChannel: 'MPQR',
        partnerTxID,
        amount,
        currency,
        paymentExpiryTime: Math.floor(new Date().getTime() / 1000) + 60,
      },
    });
    const { status, data } = response;
    if (status >= 400) {
      // disabling no-console because the response returned an error and we want to know
      // what went wrong

      // eslint-disable-next-line no-console
      console.log(data);
    }
    expect(status).toBe(201);
    expect(data).toBeDefined();

    expect(data.transactionDetails?.paymentChannel).toBe('MPQR');
    expect(data.transactionDetails?.grabTxID).isPrototypeOf(String);
    expect(data.transactionDetails?.currency).toBe(currency);
    expect(data.POSDetails?.qrPayload).toBeTruthy();
  });

  it('initiate payment to fail for CPQR', async () => {
    const amount = 2000;
    const currency = 'SGD';

    const response = await client.initiate({
      transactionDetails: {
        paymentChannel: 'CPQR',
        partnerTxID,
        billRefNumber: '123',
        amount,
        currency,
        paymentExpiryTime: Math.floor(new Date().getTime() / 1000) + 60,
      },
    });
    const { status, data } = response;
    expect(status).not.toBe(200);
    expect(data).toBeDefined();
  });
});
