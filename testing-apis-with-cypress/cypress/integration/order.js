import cloneDeep from 'lodash.clonedeep';
  import {INVALID_ID, API_URL_ORDER, login} from "../support/util";
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


describe('Orders API', () => {

  before(() => {
    login();
  })

  it('Add a new item', () => {
    cy.request('POST', API_URL_ORDER, baseRequest ).then((response) => {
      expect(response.status).to.eq(201)
      newId = response.body.id;
      expect(response.body).to.have.property('total');
      expect(response.body).to.have.property('createdAt');
      expect(response.body).to.have.property('editableUntil');
    })
  })

  it('Update the item', () => {
    let updateRequest = cloneDeep(baseRequest);
    updateRequest.pilotes = 15;
    cy.request('PUT', `${API_URL_ORDER}/${newId}`, updateRequest).then((response) => {
      expect(response.status).to.eq(200);
      expect(response.body).to.have.property('pilotes', 15);
    })
  })


  it('Find by customer', () => {
    const token = Cypress.env('token');
    const authorization = `bearer ${token}`;
    cy.request({
      method: 'POST', url: `${API_URL_ORDER}/findByCustomer`, headers: {
        authorization,
      }, body: {
        "email": "Almanza",
      }
    }).then((response) => {
      expect(response.status).to.eq(200);
      expect(response.body).to.be.a('array');
      const orders = response.body;
      expect(orders.length).gt(0);
    });
  })

  it('Not find by customer', () => {
    const token = Cypress.env('token');
    const authorization = `bearer ${token}`;
    cy.request({
      method: 'POST', url: `${API_URL_ORDER}/findByCustomer`, headers: {
        authorization,
      }, body: {
        "lastName": "NotPresent",
      }
    }).then((response) => {
      expect(response.status).to.eq(200);
    });
  })

  it('Not update invalid id', () => {
    cy.request({ method: 'PUT',  url : `${API_URL_ORDER}/${INVALID_ID}`, failOnStatusCode: false, body: baseRequest }).then((response) => {
      expect(response.status).to.eq(404);
    })
  })



})