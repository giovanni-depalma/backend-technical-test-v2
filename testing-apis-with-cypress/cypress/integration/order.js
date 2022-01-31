import cloneDeep from 'lodash.clonedeep';
import {INVALID_ID, API_URL_PURCHASER, API_URL_ADMIN, login} from "../support/util";
let newId;
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


describe('Purchaser API', () => {

  before(() => {
    login();
  })

  it('Add a new item', () => {
    cy.request('POST', API_URL_PURCHASER, baseRequest ).then((response) => {
      expect(response.status).to.eq(200)
      newId = response.body.id;
      expect(response.body).to.have.property('total');
      expect(response.body).to.have.property('createdAt');
      expect(response.body).to.have.property('editableUntil');
    })
  })

  it('Update the item', () => {
    let updateRequest = cloneDeep(baseRequest);
    updateRequest.pilotes = 15;
    cy.request('PUT', `${API_URL_PURCHASER}/${newId}`, updateRequest).then((response) => {
      expect(response.status).to.eq(200);
      expect(response.body).to.have.property('pilotes', 15);
    })
  })

  it('Read the item', () => {
    const token = Cypress.env('token');
    const authorization = `bearer ${token}`;
    cy.request({
      method: 'GET', url: `${API_URL_ADMIN}/${newId}`, headers: {
        authorization,
      }
    }).then((response) => {
      expect(response.status).to.eq(200);
      expect(response.body).to.have.property('pilotes', 15);
    });
  })

  it('Read all the item', () => {
    const token = Cypress.env('token');
    const authorization = `bearer ${token}`;
    cy.request({
      method: 'GET', url: `${API_URL_ADMIN}`, headers: {
        authorization,
      }
    }).then((response) => {
      expect(response.status).to.eq(200);
      expect(response.body).to.have.nested.property('_embedded.orders');
      expect(response.body).nested.property('_embedded.orders.length').gt(0);
    });
  })

  it('Find by customer', () => {
    const token = Cypress.env('token');
    const authorization = `bearer ${token}`;
    cy.request({
      method: 'POST', url: `${API_URL_ADMIN}/findByCustomer`, headers: {
        authorization,
      }, body: {
        "email": "Almanza",
      }
    }).then((response) => {
      expect(response.status).to.eq(200);
      expect(response.body).to.have.nested.property('_embedded.orders');
      expect(response.body).nested.property('_embedded.orders.length').gt(0);
    });
  })

  it('Not find by customer', () => {
    const token = Cypress.env('token');
    const authorization = `bearer ${token}`;
    cy.request({
      method: 'POST', url: `${API_URL_ADMIN}/findByCustomer`, headers: {
        authorization,
      }, body: {
        "lastName": "NotPresent",
      }
    }).then((response) => {
      expect(response.status).to.eq(200);
      expect(response.body).not.to.have.nested.property('_embedded.orders');
    });
  })

  it('Not update invalid id', () => {
    cy.request({ method: 'PUT',  url : `${API_URL_PURCHASER}/${INVALID_ID}`, failOnStatusCode: false, body: baseRequest }).then((response) => {
      expect(response.status).to.eq(404);
    })
  })



})