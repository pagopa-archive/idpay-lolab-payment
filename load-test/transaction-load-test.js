import http from 'k6/http';
import { check, group, sleep } from 'k6';
import { TransactionApi } from './apis/lolab-transaction-api.js';

const RATE = __ENV.RATE;
const INITIAL_VUS = __ENV.INITIAL_VUS;
const MAX_VUS = __ENV.MAX_VUS;

export const options = {
  scenarios: {
    constant_request_rate: {
      executor: 'constant-arrival-rate',
      rate: RATE,
      timeUnit: '1s', // 1000 iterations per second, i.e. 1000 RPS
      duration: '10s',
      preAllocatedVUs: INITIAL_VUS, // how large the initial pool of VUs would be
      maxVUs: MAX_VUS, // if the preAllocatedVUs are not enough, we can initialize more
    }
  }
};

const transactionApi = new TransactionApi("https://dev01.rtd.internal.dev.cstar.pagopa.it/idpaylolabpayment/transaction/");

const generator = {
    currentKey: 0,
    next(prefix) {
        return prefix + this.currentKey++;
    }
}

export default () => {
    const amount = generator.next(1);
    const response = transactionApi.createTransaction(amount, "merchant1")
    check(
        response,
        {
            'Is OK': (r) => r.status === 200 || r.status === 201,
        }
    );

    const transactionId = response.body
    check(
      transactionApi.get(transactionId),
      {
        'Is ok': (r) => r.status == 200
      }
    )
};