import { URL } from 'https://jslib.k6.io/url/1.0.0/index.js'
import http from "k6/http";

export function createToken(baseUrl, fiscalCode) {
    const myUrl = new URL(`https://dev01.rtd.internal.dev.cstar.pagopa.it/cstarmockbackendio/bpd/pagopa/api/v1/login`)
    myUrl.searchParams.append('fiscalCode', fiscalCode)
    const res = http.post(myUrl.toString(), null)
    __ENV.REQ_DUMP === undefined || console.log(JSON.stringify(res, null, 2))
    return res
}

export function getUser(baseUrl, token) {
    const myUrl = new URL(`https://dev01.rtd.internal.dev.cstar.pagopa.it/cstarmockbackendio/bpd/pagopa/api/v1/user`)
    myUrl.searchParams.append('token', token)
    const res = http.get(myUrl.toString())
    __ENV.REQ_DUMP === undefined || console.log(JSON.stringify(res, null, 2))
    return res
}