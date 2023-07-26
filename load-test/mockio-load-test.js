import http from 'k6/http';
import { check, group, sleep } from 'k6';
import { createToken, getUser } from './apis/mock-io-api.js';

const RATE = __ENV.RATE || 1;
const INITIAL_VUS = __ENV.INITIAL_VUS || 1;
const MAX_VUS = __ENV.MAX_VUS || 1;
const DURATION = __ENV.DURATION;

export const options = {
    scenarios: {
        constant_request_rate: {
            executor: 'constant-arrival-rate',
            rate: RATE,
            timeUnit: '1s', // 1000 iterations per second, i.e. 1000 RPS
            duration: DURATION,
            preAllocatedVUs: INITIAL_VUS, // how large the initial pool of VUs would be
            maxVUs: MAX_VUS, // if the preAllocatedVUs are not enough, we can initialize more
        }
    }
};

export default () => {
    group('Mock IO', () => {
        group('Should create a valid IO token', () => {
            const response = createToken("", "FAKE_CF");
            check(response, {
                'Is OK': (r) => r.status === 200 || r.status === 201,
            });

            if (response.status === 200) {
                group('Should authorize a valid token', () => {
                    const token = response.body;
                    check(getUser("", token), {
                        'Is OK': (r) => r.status === 200,
                    });
                });
            }
        });
    });
}