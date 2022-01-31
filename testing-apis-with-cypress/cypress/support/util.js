export const INVALID_ID = '1fe35579-5ce7-46ec-89e0-7e7236700297';
export const BASE_URL = 'http://localhost:8081';
export const API_URL_PURCHASER = `${BASE_URL}/purchaserOrders`;
export const API_URL_ADMIN = `${BASE_URL}/orders`;

const authToken = "http://localhost:8080/auth/realms/tui-pilotes/protocol/openid-connect/token";
const username = "tui-admin";
const password = "tui-admin";
const client_secret = "8JNumTbP0pj8iYoCNgrycb3srA4jBHly";
const client_id = "tui-gateway";
const grant_type = "password";

export function login(){
    return cy.request({
        method: 'POST',
        url: authToken,
        form: true,
        body: {
            username,
            password,
            grant_type,
            client_secret,
            client_id
        }
    })
        .as('loginResponse')
        .then((response) => {
            Cypress.env('token', response.body.access_token);
            return response;
        })
        .its('status')
        .should('eq', 200);
}