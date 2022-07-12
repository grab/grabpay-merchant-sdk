import { MerchantIntegrationOffline } from '../../../MerchantIntegrationOffline';
import * as Utils from '../../../utils/Utils';

const env = 'STG';
const country = 'PH';
const partnerId = process.env.PH_STG_POS_PARTNER_ID!;
const partnerSecret = process.env.PH_STG_POS_PARTNER_SECRET!;
const merchantId = process.env.PH_STG_POS_MERCHANT_ID!;
const terminalId = process.env.PH_STG_POS_TERMINAL_ID!;

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

  it('Test createQrCode', async () => {
    const amount = 2000;
    const currency = 'PHP';

    const response = await client.createQrCode({ msgID, partnerTxID, amount, currency });
    const { status, data } = response;
    expect(status).toBe(200);
    expect(data).toBeDefined();
    //expect(Object.keys(response).length).toEqual(6);

    expect(data.msgID).toEqual(msgID);
    expect(data.qrcode).isPrototypeOf(String);
    expect(data.txID).isPrototypeOf(String);
  });
});
