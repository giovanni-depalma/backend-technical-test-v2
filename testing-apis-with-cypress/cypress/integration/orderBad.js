import cloneDeep from 'lodash.clonedeep';
import {API_URL_ORDER} from "../support/util";
const baseRequest = {
  "pilotes": 5,
  "delivery": {
    "street": "Poco Mas Drive",
    "postcode": "24205",
    "city": "Frisco",
    "country": "TX"
  },
  "customer": {
    "email": "EufrasioAlmanzaCepeda@armyspy.com",
    "firstName": "Eufrasio Almanza",
    "lastName": "Cepeda",
    "telephone": "214-474-0425"
  }
};

const randomStr = (length) => {
  let a,b;
  for(a = '', b = 36; a.length < length;) a += (Math.random() * b | 0).toString(b);
  return a;
}

describe('Orders API', () => {

  it('Not create invalid pilotes', () => {
    let updateRequest = cloneDeep(baseRequest);
    updateRequest.pilotes = 2;
    cy.request({ method: 'POST',  url : `${API_URL_ORDER}`, failOnStatusCode: false, body: updateRequest }).then((response) => {
      expect(response.status).to.eq(400);
    })
  })

  it('Not create invalid postcode', () => {
    let updateRequest = cloneDeep(baseRequest);
    updateRequest.delivery.postcode = 2;
    cy.request({ method: 'POST',  url : `${API_URL_ORDER}`, failOnStatusCode: false, body: updateRequest }).then((response) => {
      expect(response.status).to.eq(400);
    })
  })

  it('Not create invalid email', () => {
    let updateRequest = cloneDeep(baseRequest);
    updateRequest.customer.email = "invalid_mail";
    cy.request({ method: 'POST',  url : `${API_URL_ORDER}`, failOnStatusCode: false, body: updateRequest }).then((response) => {
      expect(response.status).to.eq(400);
    })
  })

  it('Not create invalid too long', () => {
    let updateRequest = cloneDeep(baseRequest);
    updateRequest.customer.firstName = randomStr(101);
    cy.request({ method: 'POST',  url : `${API_URL_ORDER}`, failOnStatusCode: false, body: updateRequest }).then((response) => {
      expect(response.status).to.eq(400);
    })
  })

})