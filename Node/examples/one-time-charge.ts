/* eslint-disable no-console */
import dotenv from 'dotenv';
import readline from 'readline';
import { MerchantIntegrationOnline, Types, Utils } from '@grab/grabpay-merchant-sdk';
import { questionAsync } from './utils';

dotenv.config();

const currency = 'VND';
const partnerTxID = Utils.generateRandomString(32);
const partnerGroupTxID = Utils.generateRandomString(32);
const items = [
  {
    itemName: 'Hand bag1',
    quantity: 123,
  },
];
async function onlineAcceptanceExample() {
  const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout,
  });

  try {
    const client = new MerchantIntegrationOnline({
      env: 'stg',
      country: process.env.STG_OTC_COUNTRY as Types.TCountry,
      partnerId: process.env.STG_OTC_PARTNER_ID!,
      partnerSecret: process.env.STG_OTC_PARTNER_SECRET!,
      merchantId: process.env.STG_OTC_MERCHANT_ID!,
      clientId: process.env.STG_OTC_CLIENT_ID!,
      clientSecret: process.env.STG_OTC_CLIENT_SECRET!,
      redirectUrl: 'http://localhost:8888/result',
    });

    const authorizeUrl = await client.generateWebUrl({
      partnerTxID,
      partnerGroupTxID,
      amount: 2000,
      currency,
      description: 'testing otc',
      items,
      state: Utils.generateRandomString(7),
    });
    console.log(`Plz copy and paste it on browser:\n${authorizeUrl}`);
    const code = await questionAsync(rl, 'Plz input code: ');

    // --------Start get access token and confirm payment
    const { access_token: accessToken } = await client.oauth2Token({ partnerTxID, code });
    const respChargeComplete = await client.chargeComplete({ partnerTxID, accessToken });
    console.log('respChargeComplete ', respChargeComplete);
    const refundPartnerTxID = Utils.generateRandomString(32);
    const refundAmount = await questionAsync(rl, `Please enter ${currency} refund amount: `);
    const respRefund = await client.refund({
      partnerTxID: refundPartnerTxID,
      partnerGroupTxID,
      amount: Number(refundAmount),
      currency: 'VND',
      originTxID: respChargeComplete['txID'],
      description: 'test refund',
      accessToken,
    });
    console.log('respRefund ', respRefund);
    const respGetRefundStatus = await client.getRefundStatus({ partnerTxID, currency: 'VND', accessToken });
    console.log('respRefund status ', respGetRefundStatus);
  } catch (e) {
    console.log('Error: ', e);
  } finally {
    rl.close();
  }
}

onlineAcceptanceExample();
