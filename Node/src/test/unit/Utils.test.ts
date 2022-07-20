import * as Utils from '../../utils/Utils';

describe('Utils tests', () => {
  beforeEach(() => {
    jest.useFakeTimers().setSystemTime(0);
  });

  afterEach(() => {
    jest.useRealTimers();
  });

  it('Test base64URLEncode', () => {
    var input = '+++';
    var output = '---';
    expect(Utils.base64URLEncode(input)).toMatch(output);

    input = 'abs';
    output = 'abs';
    expect(Utils.base64URLEncode(input)).toMatch(output);

    input = '';
    output = '';
    expect(Utils.base64URLEncode(input)).toMatch(output);

    input = 'poiuqwe+lkjasdfn12=234-//asfdjh12+';
    output = 'poiuqwe-lkjasdfn12234-__asfdjh12-';
    expect(Utils.base64URLEncode(input)).toMatch(output);
  });

  it('Test generateHmac', () => {
    const requestBody = {
      john: 'wick',
    };
    const timestamp = 'Thu, 17 Jan 2019 02:45:06 GMT';
    const secret = 'secret';
    const hmacOutput = Utils.generateHmac({
      httpMethod: 'POST',
      requestUrl: '/gateway/partner',
      contentType: 'application/json',
      timestamp,
      requestBody: JSON.stringify(requestBody),
      secret,
    });
    expect(hmacOutput).toBe('CuD7m+X/xmdDNiRqW2st4OMZVxG+Q6FDiRoMZCwHq5A=');
  });

  it('Test generatePOPSignature', () => {
    const access_token = 'access_token';
    const client_secret = 'client_secret';

    const signature = Utils.generatePOPSignature(access_token, client_secret);
    expect(signature).toBe(
      'eyJzaWciOiJxaUt2T3R3VnBVQzZBZy1xSVNJZ2ZwWmw0emZlclVrSi1JUkZ0Qkt4eXk0IiwidGltZV9zaW5jZV9lcG9jaCI6MH0',
    );
  });

  it('Test generateRandomString', () => {
    const len = 32;
    expect(Utils.generateRandomString(len).length).toEqual(len);

    const base = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    const output = Utils.generateRandomString(len);
    for (var i = 0; i < output.length; i++) {
      expect(base).toMatch(output[i]);
    }
  });

  it('Test generateMsgId', () => {
    expect(Utils.generateMsgId().length).toEqual(32);
  });

  it('Test replaceUrl', () => {
    const url = '/this/is/{item}/need/to/be/replace';
    const item = { item: 'changedItem' };
    const output = Utils.replaceUrl(url, item);
    expect(output).toEqual('/this/is/changedItem/need/to/be/replace');
    const item2 = {};
    const output2 = Utils.replaceUrl(url, item2);
    expect(output2).toEqual('/this/is//need/to/be/replace');
  });

  it('Test getGMTString', () => {
    const output = Utils.getGMTString();
    expect(output).toBe('Thu, 01 Jan 1970 00:00:00 GMT');
  });
});
