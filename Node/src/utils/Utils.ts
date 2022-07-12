import { THttpMethod } from '../types';
const Buffer = require('buffer/').Buffer;
import { createHash, createHmac, randomBytes } from 'crypto';

const btoa = (str: string) => Buffer.from(str).toString('base64');

export function base64URLEncode(str: string) {
  return str.replace(/=/g, '').replace(/\+/g, '-').replace(/\//g, '_');
}

export type GenerateHmacParams = {
  httpMethod: THttpMethod;
  requestUrl: string;
  contentType: string;
  timestamp: string;
  requestBody: string;
  secret: string;
};

export function hashSha256ToBase64(message: string) {
  return createHash('sha256').update(message, 'utf-8').digest('base64');
}

export function hmacSha256ToBase64(message: string, secret: string) {
  return createHmac('sha256', secret).update(message, 'utf-8').digest('base64');
}

export function generateHmac({
  httpMethod,
  requestUrl,
  contentType,
  timestamp,
  requestBody,
  secret,
}: GenerateHmacParams) {
  let hashedPayload = '';
  if (httpMethod !== 'GET' && requestBody) {
    hashedPayload = hashSha256ToBase64(requestBody);
  }

  const requestData = [httpMethod, contentType, timestamp, requestUrl, hashedPayload, ''].join('\n');

  return hmacSha256ToBase64(requestData, secret);
}

export function generatePOPSignature(accessToken: string, clientSecret: string) {
  const timestamp = new Date();
  const timestampUnix = Math.round(timestamp.getTime() / 1000);
  const message = timestampUnix.toString() + accessToken;
  const signature = hmacSha256ToBase64(message, clientSecret);
  const sub = base64URLEncode(signature);
  const payload = {
    sig: sub,
    time_since_epoch: timestampUnix,
  };
  const payloadBytes = JSON.stringify(payload);

  return base64URLEncode(btoa(payloadBytes));
}

const DEFAULT_RANDOM_STRING_CHAR_SET = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';

export function generateRandomString(length: number, charSet: string = DEFAULT_RANDOM_STRING_CHAR_SET) {
  const buf = randomBytes(length);
  return [...buf.values()]
    .map((bufValue) => Math.floor((bufValue / 256) * charSet.length))
    .map((charIdx) => charSet.charAt(charIdx))
    .join('');
}

export function generateMsgId() {
  return 'xxxxxxxxxxxx4xxxyxxxxxxxxxxxxxxx'.replace(/[xy]/g, (c) => {
    const r = (Math.random() * 16) | 0,
      v = c == 'x' ? r : (r & 0x3) | 0x8;
    return v.toString(16);
  });
}

export function replaceUrl(url: string, args: Record<string, string>) {
  const regex = /{([a-zA-Z]+)}/gm;

  return url.replace(regex, (matches, item) => args[item] || '');
}

export function getGMTString() {
  return new Date().toUTCString();
}
