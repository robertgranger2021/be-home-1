# Invoice Management API
This file serves to document the various REST endpoints for the CacheFlow Invoice Management programming project.

## Table of Contents

* [Create an Invoice](#create-an-invoice)
* [Update an Invoice](#update-an-invoice)
* [Add a Line Item to an Invoice](#add-a-line-item-to-an-invoice)
* [Remove a Line Item from an Invoice](#remove-a-line-item-from-an-invoice)
* [Retrieve Invoices](#retrieve-invoices)

## Create an Invoice
Create a new invoice

### Endpoint
`POST /invoices`

### Sample Request
`POST /invoices`

#### Request Body
```json
{
  "customer_email": "john@test.com",
  "customer_name": "John Daly",
  "description": "For services rendered",
  "due_date": "2022-02-10",
  "status": "draft",
  "total": 35000
}
```

### Request Structure
| Field           | Type       | Description                                                                               |
|-----------------|------------|-------------------------------------------------------------------------------------------|
| customer_email  | String     | Email address of invoice's customer (required)                                            |
| customer_name   | String     | Name of invoice's customer (required)                                                     |
| description     | String     | Description of invoice  (required)                                                        |
| due_date        | Date       | Due date of invoice payment. Formatted as YYYY-MM-DD (required)                           |
| status          | Enum       | Status of invoice. Possible values are "draft", "approved", "sent", or "paid"  (required) |
| total           | BigDecimal | Total cost of invoice  (required)                                                         |

### Sample Responses
#### Success
Status `201 Created`

Header `Location: http://localhost/invoices/{invoiceId}`

#### Failure
Status `422 Unprocessable Entity` (Missing required fields in request)

Status `422 Unprocessable Entity` (Invalid value for field e.g. malformed email address, non-numeric total)

## Update an Invoice
Update an existing invoice

### Endpoint
`PUT /invoices/{invoiceId}`

### Sample Request to change the description
`PUT /invoices/1`

#### Request Body
```json
{
  "description": "Repair bathroom sink"
}
```
### Sample Request to change the status
`PUT /invoices/1`

#### Request Body
```json
{
  "status": "approved"
}
```

### Request Structure
Note : At least one field is required

| Field           | Type       | Description                                                                              |
|-----------------|------------|------------------------------------------------------------------------------------------|
| customer_email  | String     | Email address of invoice's customer                                                      |
| customer_name   | String     | Name of invoice's customer                                                               |
| description     | String     | Description of invoice                                                                   |
| due_date        | Date       | Due date of invoice payment. Formatted as `YYYY-MM-DD                                    |
| total           | BigDecimal | Total cost of invoice.                                                                   |
| status          | Enum       | Status of invoice. Possible values are `draft`, `approved`, `sent`, or `paid` (required) |

### Sample Responses
#### Success
Status `200 OK`

#### Failure
Status `404 Not Found` (Invalid invoice id)

Status `422 Unprocessable Entity` (Missing required fields in request)

Status `422 Unprocessable Entity` (Updating invoice when it is not in draft status)

## Add a Line Item to an Invoice
Add one or more line item(s) to an existing invoice

### Endpoint
`POST /invoices/{invoiceId}/line-items`

### Sample Request
`POST /invoices/1/line-items`

#### Request Body
```json
[
  {
    "line_item": "Remove old sink",
    "cost": 250.00
  },
  {
    "line_item": "Replace wall sink valves",
    "cost": 100.00
  }
]
```

#### Request Structure
| Field     | Type       | Description                           |
|-----------|------------|---------------------------------------|
| line_item | String     | Description of line item (required)   |
| cost      | BigDecimal | Cost of line item (optional)          |

### Sample Responses
#### Success
Status `201 Created`

Header `Location: http://localhost/invoices/{invoiceId}/line-items/{lineItemId}`

#### Failure
Status `404 Not Found` (Invalid invoice id)

Status `422 Unprocessable Entity` (Missing required fields in request)

Status `422 Unprocessable Entity` (Invalid cost)

## Remove a Line Item from an Invoice
Remove a line item from an existing invoice

### Endpoint
`DELETE /invoices/{invoiceId}/line-items/{lineItemId}`

### Sample Request
`DELETE /invoices/1/line-items/1`

### Sample Responses
#### Success
Status `204 No Content` (Line item removed from invoice)

#### Failure
Status `404 Not Found` (Invalid invoice id)

Status `404 Not Found` (Invalid line item id)

## Retrieve Invoices
Retrieve a list of invoices by status

### Endpoint
`GET /invoices?status={invoice status}`

### Sample Request
`GET /invoices?status={paid}`

### Sample Responses
#### Success
Status `200 OK`

#### Response Body
```json
[
  {
    "id": 1,
    "customer_email": "john@test.com",
    "customer_name": "John Daly",
    "description": "For services rendered",
    "due_date": "2022-02-10",
    "status": "paid",
    "total": 35000.00,
    "line_items": [
      {
        "line_item_id": 1,
        "line_item": "Remove old sink",
        "cost": 250.00
      },
      {
        "line_item_id": 2,
        "line_item": "Replace wall sink valves",
        "cost": 100.00
      }
    ]
  },
  {
    "id": 2,
    "customer_email": "rob@plubers.com",
    "customer_name": "Rob Plumber",
    "description": "Plumbing parts",
    "due_date": "2022-01-05",
    "status": "paid",
    "total": 500.00,
    "line_items": []
  }
]
```

### Response Structure
| Field          | Type       | Description                                                                               |
|----------------|------------|-------------------------------------------------------------------------------------------|
| id             | Long       | Invoice id                                                                                |
| customer_email | String     | Email address of invoice's customer (required)                                            |
| customer_name  | String     | Name of invoice's customer (required)                                                     |
| description    | String     | Description of invoice  (required)                                                        |
| due_date       | Date       | Due date of invoice payment. Formatted as YYYY-MM-DD (required)                           |
| status         | Enum       | Status of invoice. Possible values are "draft", "approved", "sent", or "paid"  (required) |
| total          | BigDecimal | Total cost of invoice  (required)                                                         |
| line_items     | List       | List of line items for invoice                                                            |
| line_item_id   | Long       | Id of individual line item                                                                |
| line_item      | String     | Description of line item                                                                  |
| cost           | BigDecimal | Cost of line item                                                                         |

#### Failure
Status `400 Bad Request` (Invalid status value)
