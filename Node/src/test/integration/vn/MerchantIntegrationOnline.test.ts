import { MerchantIntegrationOnline } from '../../../MerchantIntegrationOnline';
import * as Utils from '../../../utils/Utils';

const env = 'STG';
const country = 'VN';
const partnerId = process.env.VN_STG_OTC_PARTNER_ID!;
const partnerSecret = process.env.VN_STG_OTC_PARTNER_SECRET!;
const merchantId = process.env.VN_STG_OTC_MERCHANT_ID!;
const clientId = process.env.VN_STG_OTC_CLIENT_ID!;
const clientSecret = process.env.VN_STG_OTC_CLIENT_ID!;
const redirectUrl = 'http://localhost:8888/result';

describe('Test VN MerchantIntegrationOnline', () => {
  let client: MerchantIntegrationOnline;
  let partnerTxID: string;
  let partnerGroupTxID: string;

  beforeAll(() => {
    client = new MerchantIntegrationOnline({
      env,
      country,
      partnerId,
      partnerSecret,
      merchantId,
      clientId,
      clientSecret,
      redirectUrl,
    });
  });

  beforeEach(() => {
    partnerTxID = Utils.generateRandomString(32);
    partnerGroupTxID = Utils.generateRandomString(32);
  });

  it('Test chargeInit', async () => {
    const amount = 2000;
    const currency = 'VND';
    const description = 'this is testing';
    const response = await client.chargeInit({
      partnerTxID,
      partnerGroupTxID,
      amount,
      currency,
      description,
      metaInfo: { brandName: 'Brand1' },
    });
    const { status, data } = response;
    expect(status).toBe(200);
    expect(data).toBeDefined();
    expect(data.request).toBeDefined();
    expect(data.request.length).toBeGreaterThan(0);
    expect(data.partnerTxID).toEqual(partnerTxID);
  });

  it('Test chargeInit with postpaid hidden as payment method', async () => {
    const amount = 2000;
    const currency = 'VND';
    const description = 'this is testing';
    const response = await client.chargeInit({
      partnerTxID,
      partnerGroupTxID,
      amount,
      currency,
      description,
      hidePaymentMethods: ['POSTPAID'],
    });
    const { status, data } = response;
    expect(status).toBe(200);
    expect(data).toBeDefined();
    expect(data.request).toBeDefined();
    expect(data.request.length).toBeGreaterThan(0);
    expect(data.partnerTxID).toEqual(partnerTxID);
  });
});
