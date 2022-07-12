/* eslint-disable no-console */
import dotenv from 'dotenv';
import axios from 'axios';
import readline from 'readline';
import { MerchantIntegrationOffline, Types, Utils } from '@grab/grabpay-merchant-sdk';
import { questionAsync } from './utils';

dotenv.config();

const partnerTxID = Utils.generateRandomString(32);
const msgID = Utils.generateMsgId();
const currency = 'VND';

async function pointOfSaleExample() {
  const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout,
  });
  try {
    const amount = Number(await questionAsync(rl, `Please enter ${currency} amount: `));
    const client = new MerchantIntegrationOffline({
      env: 'stg',
      country: process.env.STG_OTC_COUNTRY as Types.TCountry,
      partnerId: process.env.STG_POS_PARTNER_ID!,
      partnerSecret: process.env.STG_POS_PARTNER_SECRET!,
      merchantId: process.env.STG_POS_MERCHANT_ID!,
      terminalId: process.env.STG_POS_TERMINAL_ID!,
    });
    const respCreateQRCode = await client.createQrCode({ msgID, partnerTxID, amount, currency });
    console.log('respCreateQRCode ', respCreateQRCode);
    const respGetStatus = await client.getTxnDetails({ partnerTxID, currency });
    console.log('respGetStatus ', respGetStatus);
  } catch (e) {
    if (axios.isAxiosError(e)) {
      console.log('Request error ', e.response?.data);
    } else {
      console.log(e);
    }
  } finally {
    rl.close();
  }
}

pointOfSaleExample();
